package cellsociety.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Keeps track of Cells in a Grid.
 */
public abstract class Grid {

  private HashMap<Cell, int[]> neighbors;
  private ArrayList<Cell> grid;
  private HashMap<String, Integer>[] issues;
  private final int height;
  private final int width;

  /**
   * Constructor fills fields using XML data.
   * Assumptions: parameters contains all the information required to create appropriate cell.
   * cellArrangement represents a valid square tesselation grid.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters game settings from XML
   */
  public Grid(ArrayList<String> cellArrangement, HashMap<String, Integer> parameters) {
    this.height = parameters.get("height");
    this.width = parameters.get("width");
    setupGrid(cellArrangement, parameters);
    setupNeighbors();
    issues = new HashMap[this.grid.size()];
  }

  /**
   * Clears issues array. Runs prepareNextState() on all cells. Handles any returned issues where
   * cells wish to "move". Runs updateState() on all cells, finalizing state changes. For a cell to
   * properly "move" or pass information to another cell, moveCell() will need to be overwritten.
   * Assumptions: No movement of cells.
   */
  public void updateCells() {
    clearIssues();
    prepareCellUpdates();
    handleIssues();
    pushCellUpdates();
  }

  /**
   * Returns geometric representation of cells and their states for printing/viewing.
   *
   * @return integer array list of cell states
   */
  public ArrayList<Integer> viewGrid() {
    ArrayList<Integer> cellStates = new ArrayList<>();
    for (Cell cell : this.grid) {
      cellStates.add(cell.getState());
    }
    return cellStates;
  }

  /**
   * Returns grid size.
   *
   * @return [width, height]
   */
  public int[] getDimensions() {
    return new int[]{this.width, this.height};
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

  /**
   * Returns an immutable version of grid.
   *
   * @return grid
   */
  protected ArrayList<Cell> getGrid() {
    return (ArrayList<Cell>) Collections.unmodifiableList(this.grid);
  }

  /**
   * Gives direct access to issues field variable, as it is only needed if subclass cell wants to
   * move or pass information. Exists as feature to use if needed.
   *
   * @return HashMap parameters corresponding to index/cell of issue.
   */
  protected HashMap<String, Integer> getIssues(int index) {
    return this.issues[index];
  }

  /**
   * Used by constructor. Creates cell objects and populates grid field.
   * Assumptions: cellArrangement forms a square tesselation grid. Strings contain only integer
   * values reflecting a cell's state.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters game parameters from XML
   */
  private void setupGrid(ArrayList<String> cellArrangement, HashMap<String, Integer> parameters) {
    this.grid = new ArrayList<>();
    for (String s : cellArrangement) {
      String[] row = s.split("");
      for (String state : row) {
        parameters.put("state", Integer.parseInt(state));
        Cell cell = chooseCell(parameters);
        this.grid.add(cell);
      }
    }
  }

  /**
   * Used by constructor. Populates neighbor field.
   * Assumptions: setupGrid() has already been called.
   */
  private void setupNeighbors() {
    this.neighbors = new HashMap<>();
    for (int i = 0; i < grid.size(); i++) {
      this.neighbors.put(this.grid.get(i), pullNeighborIndexes(i));
    }
  }

  /**
   * Used by setupGrid(). Create cell object matching Grid type.
   * Assumptions: Parameters contains XML information and cell state
   *
   * @param parameters cell state and game parameters from XML
   * @return appropriate cell object
   */
  protected abstract Cell chooseCell(HashMap<String, Integer> parameters);

  /**
   * Used by setupNeighbors(). Finds neighboring indexes in arraylist representation of grid.
   * Assumptions: Grid is a square tesselation. Looking for 8 neighbors.
   *
   * @param index center index
   * @return neighboring indexes
   */
  private int[] pullNeighborIndexes(int index) {
    int[] variance = neighborVariances(index);
    ArrayList<Integer> possibleIndexes = new ArrayList<>();
    for (int i : variance) {
      possibleIndexes.add(i + index);
    }
    ArrayList<Integer> validIndexes = removeInvalidIndexes(index, possibleIndexes);
    return convertIntArrayList(validIndexes);
  }

  /**
   * Used by pullNeighborIndexes(). Returns array of values to be added to center index to get
   * neighboring indexes.
   * Assumptions: counts surrounding eight cells as neighbors. Grid is a square tesselation.
   *
   * @param index center index
   * @return values for computing neighboring indexes
   */
  protected int[] neighborVariances(int index) {
    int width = getDimensions()[0];
    return new int[]{-1 - width, -1 * width, 1 - width, -1, 1, -1 + width, width, 1 + width};
  }

  /**
   * Used by pullNeighborIndexes(). Turns an Integer ArrayList into int[] for viewing.
   * Assumptions: validIndexes is an Integer ArrayList
   *
   * @param validIndexes Integer ArrayList
   * @return int[]
   */
  private int[] convertIntArrayList(ArrayList<Integer> validIndexes) {
    int[] neighbors = new int[validIndexes.size()];
    for (int i = 0; i < neighbors.length; i++) {
      neighbors[i] = validIndexes.get(i);
    }
    return neighbors;
  }

  /**
   * Used by pullNeighborIndexes(). Removes indexes outside of grid boundaries and checks that
   * indexes to the right and left are still on the same row. Logic is only checked against
   * four and eight neighbors.
   * Assumptions: Grid is a square tesselation. PossibleIndexes is either length 4 or 8.
   *
   * @param centerIndex center cell
   * @param possibleIndexes all calculated index values
   * @return valid neighboring indexes
   */
  private ArrayList<Integer> removeInvalidIndexes(int centerIndex,
      ArrayList<Integer> possibleIndexes) {
    ArrayList<Integer> validIndexes = new ArrayList<>(possibleIndexes);
    for (int i : possibleIndexes) {
      if (i < 0 || i >= this.width * this.height) {
        validIndexes.remove((Integer) i);
      }
      if (i + 1 == centerIndex || i - 1 == centerIndex) {
        if (i / this.width != centerIndex / this.width) {
          validIndexes.remove((Integer) i);
        }
      }
    }
    return validIndexes;
  }

  /**
   * Used by prepareCellUpdates(). Compiles neighboring cell states.
   * Assumptions: Cell passes state as int. Only state is required to update a cell.
   *
   * @param index center cell position
   * @return states of neighboring cells
   */
  protected int[] pullNeighborStates(int index) {
    Cell currentCell = this.grid.get(index);
    Cell neighborCell;
    int[] neighborIndexes = this.neighbors.get(currentCell);
    int[] neighborStates = new int[neighborIndexes.length];

    for (int i = 0; i < neighborIndexes.length; i++) {
      neighborCell = this.grid.get(neighborIndexes[i]);
      neighborStates[i] = neighborCell.getState();
    }
    return neighborStates;
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
      HashMap<String, Integer> movement = getGrid().get(i).prepareNextState(neighborStates);
      if (movement.get("state") != -1) {
        issues[i] = movement;
      }
    }
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
   * Runs updateState() on all cells and finalizes the cycle.
   * Assumptions: Every cell has their proper preparedState and all issues have been handled.
   */
  private void pushCellUpdates() {
    for (Cell cell : getGrid()) {
      cell.updateState();
    }
  }

  /**
   * Override to use. Handles cell movement or information passing by calling receiveUpdate() on
   * whichever neighboring cell will be passed the information.
   * Assumptions: Any cell wishing to move or pass information has been cataloged in issues.
   *
   * @param index cell trying to move or pass information
   */
  protected void moveCell(int index) {}

  // TODO: extract methods. only variation so far is in which neighbors are considered.
}
