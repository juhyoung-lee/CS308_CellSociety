package cellsociety.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Keeps track of Cells in a Grid.
 */
public abstract class Grid {

  protected HashMap<Cell, int[]> neighbors;
  protected ArrayList<Cell> grid;
  private final int height;
  private final int width;

  /**
   * Constructor.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters game settings from XML
   */
  public Grid(ArrayList<String> cellArrangement, HashMap<String, Integer> parameters) {
    this.height = parameters.get("height");
    this.width = parameters.get("width");
    setupGrid(cellArrangement, parameters);
    setupNeighbors();
  }

  /**
   * Loads updates for cells and then updates. If cells move or information beyond state needs to
   * be passed between cells, this method will need to handle that before cell.updateState().
   * Assumptions: No movement of cells.
   */
  public void updateCells() {
    for (int i = 0; i < grid.size(); i++) {
      int[] neighborStates = pullNeighborStates(i);
      grid.get(i).prepareNextState(neighborStates);
    }
    // if prepareNextState returns issue, it'll have to be added to an array field
    // and handled here
    for (Cell cell : grid) {
      cell.updateState();
    }
  }

  /**
   * Returns states of cells for printing/viewing.
   *
   * @return array list of cell states
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
   * For testing.
   */
  public void printGrid() {
    int rowCounter = 0;
    for (Cell cell : this.grid) {
      if (rowCounter == this.width) {
        System.out.println();
        rowCounter = 0;
      }
      System.out.print(cell.getState());
      rowCounter++;
    }
  }
}
