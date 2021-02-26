package cellsociety.model;

import cellsociety.configuration.Simulation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Updates cells in a grid by running cell's update method. Catalogs cases where cells want to pass
 * information back and forth during an update cycle and resolves those cases. Keeps track of grid
 * size so grid can expand when necessary.
 * Assumptions: Cells are not null and any implementation of Grid will overwrite the necessary
 * methods according to the game type.
 * Dependencies: Cell, java.util.*
 * Examples: GameOfLifeGrid extends Grid and implements the necessary abstract methods. WaTorGrid
 * goes further to implement necessary methods and also overwrite the methods that define movement
 * of cells.
 */
public abstract class Grid extends GridHelper {

  private Map<String, Integer>[] issues;
  private Map<String, Integer> cellParameter;

  /**
   * Constructor. Holds cell states and positions. Updates cells as needed.
   * Assumptions: grid and cell parameters are all correctly read from properly formatted XML
   *
   * @param cellArrangement list of states that define cell locations
   * @param gridParameters parameters that define grid
   * @param cellParameters parameters that define cell
   * @throws Exception when parameters or cell arrangement are not valid
   */
  public Grid(List<String> cellArrangement, String[] gridParameters,
      Map<String, Integer> cellParameters)
      throws Exception {
    super(cellArrangement, gridParameters, cellParameters);
    this.issues = new HashMap[getGrid().size()];
    this.cellParameter = cellParameters;
  }

  /**
   * Returns geometric representation of cells and their states for printing/viewing.
   *
   * @return integer array list of cell states
   */
  public List<Integer> viewGrid() {
    List<Integer> cellStates = new ArrayList<>();
    for (Cell cell : getGrid()) {
      cellStates.add(cell.getState());
    }
    return cellStates;
  }

  /**
   * Clears issues array. Runs prepareNextState() on all cells. Handles any returned issues where
   * cells wish to "move". Runs updateState() on all cells, finalizing state changes. For a cell to
   * properly "move" or pass information to another cell, moveCell() will need to be overwritten.
   * Assumptions: Cells are not null
   */
  public void updateCells() {
    if (getGridType().equals(Simulation.GRID_OPTIONS.get(1))) {
      checkGridExpansion();
    }
    clearIssues();
    prepareCellUpdates();
    handleIssues();
    pushCellUpdates();
  }

  // assumes grid is not wrapping
  // assumes 0 is default, inactive state
  private void checkGridExpansion() {
    int baseState = getGrid().get(0).getBaseState();
    for (int i : edgeIndexes()) {
      if (getGrid().get(i).getState() != baseState) {
        expandGrid();
        break;
      }
    }
  }

  // breaks when grid is too small??
  private List<Integer> edgeIndexes() {
    int width = getDimensions()[0];
    int height = getDimensions()[1];
    List<Integer> edgeIndexes = new ArrayList<>();
    for (int i = 0; i < width; i++) {
      edgeIndexes.add(i);
      edgeIndexes.add(i + width);
      edgeIndexes.add(width * height - width + i);
      edgeIndexes.add(width * height - width + i - width);
    }
    for (int i = 2; i < height - 2; i++) {
      edgeIndexes.add(i * width);
      edgeIndexes.add(i * width + 1);
      edgeIndexes.add((i + 1) * width - 1);
      edgeIndexes.add((i + 1) * width - 2);
    }
    return edgeIndexes;
  }

  private void expandGrid() {
    List<Cell> newGrid = new ArrayList<>();
    int width = getDimensions()[0];
    int height = getDimensions()[1];

    try {
      for (int i = 0; i < width * 3 * height; i++) {
        newGrid.add(baseCell());
      }
      for (int i = 0; i < width * height; i++) {
        if (i % width == 0) {
          for (int j = 0; j < width; j++) {
            newGrid.add(baseCell());
          }
        }
        newGrid.add(getGrid().get(i));
        if (i % width == width - 1) {
          for (int j = 0; j < width; j++) {
            newGrid.add(baseCell());
          }
        }
      }
      for (int i = 0; i < width * 3 * height; i++) {
        newGrid.add(baseCell());
      }

      width *= 3;
      height *= 3;
      expand(width, height, newGrid);
      this.issues = new Map[width * height];
      for (int i = 0; i < getGrid().size(); i++) {
        getAllNeighbors().put(getGrid().get(i), pullNeighborIndexes(i));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Cell baseCell() throws Exception {
    cellParameter.put(Cell.STATE_KEY, 0);
    Cell cell = chooseCell(cellParameter);
    int baseState = cell.getBaseState();
    if (baseState == 0) {
      return cell;
    } else {
      cellParameter.put(Cell.STATE_KEY, baseState);
      return chooseCell(cellParameter);
    }
  }

  /**
   * Clears issues in preparation of a new cycle.
   * Assumptions: Issues has already been instantiated.
   */
  private void clearIssues() {
    Arrays.fill(issues, null);
  }

  /**
   * Runs prepareNextState() on all cells and catalogs any issues or moving cells that arise.
   * Assumptions: Cells will return a HashMap with -1 for "state" if no movement occurs
   */
  private void prepareCellUpdates() {
    for (int i = 0; i < getGrid().size(); i++) {
      int[] neighborStates = pullNeighborStates(i);
      Map<String, Integer> movement = getGrid().get(i).prepareNextState(neighborStates);
      if (movement.get(Cell.STATE_KEY) != Cell.NO_MOVEMENT) {
        issues[i] = movement;
      }
    }
  }

  /**
   * Used by prepareCellUpdates(). Compiles neighboring cell states. Necessary as neighbors field
   * variable only stores cell indexes.
   * Assumptions: Cell passes state as int. Only state is required to update a cell.
   *
   * @param index center cell position
   * @return states of neighboring cells
   */
  private int[] pullNeighborStates(int index) {
    Cell currentCell = getGrid().get(index);
    Cell neighborCell;
    int[] neighborIndexes = getNeighbors(currentCell);
    int[] neighborStates = new int[neighborIndexes.length];

    for (int i = 0; i < neighborIndexes.length; i++) {
      neighborCell = getGrid().get(neighborIndexes[i]);
      neighborStates[i] = neighborCell.getState();
    }
    return neighborStates;
  }

  /**
   * Finds any issues cataloged and calls moveCell() on it to be handled.
   * Assumptions: MoveCell has been overwritten if cells do move or pass information around.
   */
  private void handleIssues() {
    for (int i = 0; i < issues.length; i++) {
      if (issues[i] != null) {
        moveCell(i);
      }
    }
  }

  /**
   * Handles cell movement or information passing by calling receiveUpdate() on whichever
   * neighboring cell will be passed the information. If no cell can receive an update, the cell
   * will not move. Requires overriding of findPotentialMoves() to properly function.
   * Assumptions: Any cell wishing to move or pass information has been cataloged in issues.
   *
   * @param index cell trying to move or pass information
   */
  private void moveCell(int index) {
    List<Integer> places = findPotentialMoves(index);

    Collections.shuffle(places);
    Map<String, Integer> state = getIssues(index);
    for (Integer neighborIndex : places) {
      if (getGrid().get(neighborIndex).receiveUpdate(state)) {
        return;
      }
    }
    getGrid().get(index).receiveUpdate(state);
  }

  /**
   * Used by moveCell(). Must be overwritten to function. Returns array list of indexes that should
   * be checked to receive a moving cell.
   *
   * @param index of cell trying to move
   * @return indexes to be checked for receiving update
   */
  protected List<Integer> findPotentialMoves(int index) {
    return new ArrayList<>();
  }

  /**
   * Runs updateState() on all cells and finalizes the cycle.
   * Assumptions: Every cell has their proper preparedState and all issues have been handled.
   */
  private void pushCellUpdates() {
    for (Cell cell : getGrid()) {
      cell.updateState();
    }
  }

  /**
   * Gives direct access to issues field variable, as it is only needed if subclass cell wants to
   * move or pass information. Exists as feature to use if needed.
   *
   * @return HashMap parameters corresponding to index/cell of issue.
   */
  protected Map<String, Integer> getIssues(int index) {
    return this.issues[index];
  }
}
