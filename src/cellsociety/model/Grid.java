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
public abstract class Grid {

  private Map<Cell, int[]> neighbors;
  private List<Cell> grid;
  private Map<String, Integer>[] issues;
  private final int height;
  private final int width;
  private final String shape;
  private final int neighborhoodSize;

  /**
   * Constructor fills fields using XML data.
   * Assumptions: parameters contains all the information required to create appropriate cell.
   * cellArrangement represents a valid square tesselation grid.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters game settings from XML
   * @throws Exception invalid cell state
   */
  public Grid(List<String> cellArrangement, String shape, Map<String, Integer> parameters)
      throws Exception {
    this.shape = shape;
    this.height = parameters.get("height");
    this.width = parameters.get("width");
    this.neighborhoodSize = parameters.get("neighborhoodSize");
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
  public List<Integer> viewGrid() {
    List<Integer> cellStates = new ArrayList<>();
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
  protected List<Cell> getGrid() {
    return Collections.unmodifiableList(this.grid);
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
   * Allows subclasses to overwrite decideNeighborhood(), as neighborhoodSize is required to decide
   * how big the neighborhood should be.
   *
   * @return int neighborhood size
   */
  protected int getNeighborhoodSize() {
    return this.neighborhoodSize;
  }

  /**
   * Allows subclasses to overwrite decideNeighborhood(), as shape is required to decide what shape
   * the neighborhood should be made of.
   *
   * @return String shape of cells
   */
  protected String getShape() {
    return this.shape;
  }

  /**
   * Used by constructor. Creates cell objects and populates grid field.
   * Assumptions: cellArrangement forms a square tesselation grid. Strings contain only integer
   * values reflecting a cell's state.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters game parameters from XML
   * @throws Exception invalid cell state, shape, or inconsistent neighborhood size
   */
  private void setupGrid(List<String> cellArrangement, Map<String, Integer> parameters)
      throws Exception {
    this.grid = new ArrayList<>();
    for (String s : cellArrangement) {
      String[] row = s.split("");
      for (String state : row) {
        parameters.put("state", Integer.parseInt(state));
        Cell cell = chooseCell(parameters);
        if (cell.isValidState()) {
          this.grid.add(cell);
        } else {
          throw new Exception("Invalid Cell State");
        }
      }
    }
  }

  /**
   * Used by constructor. Populates neighbor field.
   * Assumptions: setupGrid() has already been called.
   *
   * @throws Exception invalid shape or inconsistent neighborhood size
   */
  private void setupNeighbors() throws Exception {
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
  protected abstract Cell chooseCell(Map<String, Integer> parameters);

  /**
   * Used by setupNeighbors(). Finds neighboring indexes in arraylist representation of grid. Throws
   * an exception if neighborhood size is not a valid size. This is the case when
   * Assumptions: Grid is a square tesselation. Looking for 8 Moore neighbors.
   *
   * @param index center index
   * @return neighboring indexes
   * @throws Exception neighborhood size is inconsistent or shape is invalid
   */
  private int[] pullNeighborIndexes(int index) throws Exception {
    int[] variance = decideNeighborhood(index);

    if (variance.length != this.neighborhoodSize) {
      throw new Exception("Grid neighborhood size (" + variance.length + ") and XML parameter ("
          + this.neighborhoodSize + ") inconsistent");
    }

    ArrayList<Integer> possibleIndexes = new ArrayList<>();
    for (int i : variance) {
      possibleIndexes.add(i + index);
    }
    List<Integer> validIndexes = removeInvalidIndexes(index, possibleIndexes);
    return convertToIntArrayList(validIndexes);
  }

  /**
   * Used by pullNeighborIndexes(). Returns array of values to be added to center index to get
   * neighboring indexes. Will need to implement different versions for different neighborhood
   * sizes. Throws exception if shape is not a valid shape.
   * Assumptions: Index is within grid.
   *
   * @param index center index
   * @return values for computing neighboring indexes
   * @throws Exception when shape is invalid
   */
  protected int[] decideNeighborhood(int index) throws Exception {
    return switch (this.shape) {
      case "square" -> square();
      case "triangle" -> triangle(index);
      case "hexagon" -> hexagon(index);
      default -> throw new Exception("Invalid shape: " + this.shape);
    };
  }

  /**
   * Predefined version of decideNeighborhood() where the only valid neighbor options are the ones
   * immediately touching. Overwrite decideNeighbor() and call this method from within that to use.
   *
   * @param index center cell index
   * @return values for computing immediate neighboring indexes
   * @throws Exception when shape is invalid
   */
  protected int[] decideSmallNeighborhood(int index) throws Exception {
    return switch (this.shape) {
      case "square" -> squareSmall();
      case "triangle" -> triangleSmall(index);
      case "hexagon" -> hexagon(index);
      default -> throw new Exception("Invalid shape: " + this.shape);
    };
  }

  /**
   * Calculates the values to add to center cell's index to get neighboring indexes for a grid of
   * squares. Returns empty array if neighborhood size is not an option.
   * Assumptions: Each row has the same number of squares.
   *
   * @return int[] of calculations to get neighboring indexes
   */
  private int[] square() {
    int w = this.width;
    return switch (this.neighborhoodSize) {
      case 4 -> new int[]{-1 * w, -1, 1, w};
      case 8 -> new int[]{-1 - w, -1 * w, 1 - w, -1, 1, -1 + w, w, 1 + w};
      default -> new int[]{};
    };
  }

  /**
   * Same as square() but only cardinal direction neighbors. Returns empty array if neighborhoodSize
   * is not 4.
   *
   * @return cardinal neighbors
   */
  private int[] squareSmall() {
    if (this.neighborhoodSize != 4) {
      return new int[]{};
    }
    int w = this.width;
    return new int[]{-1 * w, -1, 1, w};
  }

  /**
   * Calculates the values to add to center cell's index to get neighboring indexes for a grid of
   * triangles. Returns empty array if neighborhood size is not an option.
   * Assumptions: Row 0 Index 0 is a triangle pointing up. Each row has the same number of
   * triangles.
   *
   * @param index center triangle index
   * @return int[] of calculations to get neighboring indexes
   */
  private int[] triangle(int index) {
    int w = this.width;
    if (isTriangleTopPointy(index)) {
      return switch (this.neighborhoodSize) {
        case 3 -> new int[]{-1, 1, w};
        case 12 -> new int[]{-1 - w, -1 * w, 1 - w, -2, -1, 1, 2, -2 + w, -1 + w, w, 1 + w, 2 + w};
        default -> new int[]{};
      };
    } else {
      return switch (this.neighborhoodSize) {
        case 3 -> new int[]{-1 * w, -1, 1};
        case 12 -> new int[]{-2 - w, -1 - w, -1 * w, 1 - w, 2 - w, -2, -1, 1, 2, -1 + w, w, 1 + w};
        default -> new int[]{};
      };
    }
  }

  /**
   * Like triangle() but only immediate 3 neighbors.
   *
   * @param index center triangle index
   * @return int[] of calculations to get neighboring indexes
   */
  private int[] triangleSmall(int index) {
    int w = this.width;
    boolean trianglePointy = isTriangleTopPointy(index);

    if (trianglePointy && neighborhoodSize == 3) {
      return new int[]{-1, 1, w};
    } else if (neighborhoodSize == 3) {
      return new int[]{-1 * w, -1, 1};
    } else {
      return new int[]{};
    }
  }

  /**
   * Distinguishes between pointy top triangles and pointy bottom triangles using index
   * values. If assumptions hold true, in even rows the even indexes (first in row is 0) are pointy
   * top triangles. In odd rows, the odd indexes are pointy top triangles. Therefore, adding row
   * number and index within the row will give whether the triangle is pointy top or pointy bottom.
   *
   * @param index triangle cell index
   * @return if a triangle is pointy side up
   */
  protected boolean isTriangleTopPointy(int index) {
    int rowNumber = index / this.width;
    int indexInRow = index % this.width;
    return (rowNumber + indexInRow) % 2 == 0;
  }

  /**
   * Calculates the values to add to center cell's index to get neighboring indexes for a grid of
   * hexagon. Returns empty array if neighborhood size is not an option.
   * Assumptions: Hexagon grid orients hexagons with corners up and down. Second row starts at the
   * bottom right of the first hexagon of the first row. Third row starts at the bottom left of the
   * first hexagon of the second row. Rows repeat in that manner. Each row has the same number of
   * hexagons.
   *
   * @param index center hexagon index
   * @return int[] of calculations to get neighboring indexes
   */
  private int[] hexagon(int index) {
    if (this.neighborhoodSize != 6) {
      return new int[]{};
    }
    int w = this.width;
    boolean evenRow = (index / this.width) % 2 == 0;
    if (evenRow) {
      return new int[]{-1 - w, -1 * w, -1, 1, -1 + w, w};
    } else {
      return new int[]{-1 * w, 1 - w, -1, 1, w, 1 + w};
    }
  }

  /**
   * Used by pullNeighborIndexes(). Turns an Integer ArrayList into int[] for viewing.
   * Assumptions: validIndexes is an Integer ArrayList
   *
   * @param validIndexes Integer ArrayList
   * @return int[]
   */
  private int[] convertToIntArrayList(List<Integer> validIndexes) {
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
  private List<Integer> removeInvalidIndexes(int centerIndex,
      List<Integer> possibleIndexes) {
    List<Integer> validIndexes = new ArrayList<>(possibleIndexes);
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
      Map<String, Integer> movement = getGrid().get(i).prepareNextState(neighborStates);
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
   * Must be overwritten to function. Returns array list of indexes that should be checked to
   * receive a moving cell.
   *
   * @param index of cell trying to move
   * @return indexes to be checked for receiving update
   */
  protected List<Integer> findPotentialMoves(int index) {
    return new ArrayList<>();
  }
}
