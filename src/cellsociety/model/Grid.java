package cellsociety.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps track of Cells in a Grid.
 */
public abstract class Grid extends GridHelper {

  private Map<String, Integer>[] issues;
  private List<Cell> grid;
  private int width;
  private int height;
  private Map<String, Integer> cellParameter;
  private Map<Cell, int[]> neighbors;

  public Grid(List<String> cellArrangement, String[] gridParameters,
      Map<String, Integer> cellParameters)
      throws Exception {
    super(cellArrangement, gridParameters, cellParameters);
    this.grid = getInitialGrid();
    this.issues = new HashMap[this.grid.size()];
    this.width = getInitialDimensions()[0];
    this.height = getInitialDimensions()[1];
    this.cellParameter = cellParameters;
    this.neighbors = getInitialNeighbors();
  }

  /**
   * Returns geometric representation of cells and their states for printing/viewing.
   *
   * @return integer array list of cell states
   */
  public List<Integer> viewGrid() {
    List<Integer> cellStates = new ArrayList<>();
    for (Cell cell : this.grid) {
      cellStates.add(cell.getState());
    }
    return cellStates;
  }

  public int[] getDimensions() {
    return new int[]{this.width, this.height};
  }

  /**
   * Clears issues array. Runs prepareNextState() on all cells. Handles any returned issues where
   * cells wish to "move". Runs updateState() on all cells, finalizing state changes. For a cell to
   * properly "move" or pass information to another cell, moveCell() will need to be overwritten.
   * Assumptions: No movement of cells.
   */
  public void updateCells() {
    if (getGridType().equals("infinite")) {
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
    for (int i : edgeIndexes()) {
      int baseState = this.grid.get(i).getBaseState();
      if (this.grid.get(i).getState() != baseState) {
        expandGrid();
        break;
      }
    }
  }

  private List<Integer> edgeIndexes() {
    List<Integer> edgeIndexes = new ArrayList<>();
    int width = getDimensions()[0];
    int height = getDimensions()[1];
    for (int i = 0; i < width; i++) {
      edgeIndexes.add(i);
      edgeIndexes.add(width * height - width + i);
    }
    for (int i = 1; i < height - 1; i++) {
      edgeIndexes.add(i * width);
      edgeIndexes.add((i + 1) * width - 1);
    }
    return edgeIndexes;
  }

  /**
   * Returns a copy array of neighbor indexes.
   *
   * @param cell center cell
   * @return int[] of neighbor indexes
   */
  protected int[] getNeighbors(Cell cell) {
    return this.neighbors.get(cell).clone();
  }

  private void expandGrid() {
    int oldSize = this.width * this.height;
    this.width *= 2;
    this.height *= 2;
    int newSize = this.width * this.height;
    List<Cell> newGrid = new ArrayList<>();

    try {
      for (int i = 0; i < oldSize; i++) {
        newGrid.add(baseCell());
      }
      newGrid.addAll(this.grid);
      for (int i = 0; i < oldSize; i++) {
        newGrid.add(baseCell());
      }
      this.grid = newGrid;

      for (int i = 0; i < this.grid.size(); i++) {
        this.neighbors.put(this.grid.get(i), pullNeighborIndexes(i));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Cell baseCell() throws Exception {
    cellParameter.put("state", 0);
    Cell cell = chooseCell(cellParameter);
    int baseState = cell.getBaseState();
    if (baseState == 0) {
      return cell;
    } else {
      cellParameter.put("state", baseState);
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
    for (int i = 0; i < this.grid.size(); i++) {
      int[] neighborStates = pullNeighborStates(i);
      Map<String, Integer> movement = this.grid.get(i).prepareNextState(neighborStates);
      if (movement.get("state") != -1) {
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
    Cell currentCell = this.grid.get(index);
    Cell neighborCell;
    int[] neighborIndexes = getNeighbors(currentCell);
    int[] neighborStates = new int[neighborIndexes.length];

    for (int i = 0; i < neighborIndexes.length; i++) {
      neighborCell = this.grid.get(neighborIndexes[i]);
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
      if (this.grid.get(neighborIndex).receiveUpdate(state)) {
        return;
      }
    }
    this.grid.get(index).receiveUpdate(state);
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
    for (Cell cell : this.grid) {
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


  /**
   * Returns an immutable version of grid.
   *
   * @return grid
   */
  protected List<Cell> getGrid() {
    return Collections.unmodifiableList(this.grid);
  }

}
