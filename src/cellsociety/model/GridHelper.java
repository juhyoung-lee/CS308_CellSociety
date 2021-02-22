package cellsociety.model;

import cellsociety.configuration.Simulation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GridHelper {

  public static final String NEIGHBORHOOD_SIZE = "neighborhoodSize";
  public static final String SQUARE = "square";
  public static final int SQUARE_SIDES_MIN = 4;
  public static final int SQUARE_SIDES_MAX = 8;
  public static final String TRIANGLE = "triangle";
  public static final int TRIANGLE_SIDES_MIN = 3;
  public static final int TRIANGLE_SIDES_MAX = 12;
  public static final String HEXAGON = "hexagon";
  public static final int HEXAGON_SIDES = 6;
  public static final String INVALID_GRID_PARAMETERS = "Invalid Grid Parameters";
  public static final String INVALID_CELL_STATE = "Invalid Cell State";
  public static final String INVALID_NEIGHBORHOOD_SIZE = "Invalid Neighborhood Size";
  public static final String INVALID_SHAPE = "Invalid Shape";

  private Map<Cell, int[]> neighbors;
  private List<Cell> grid;
  private int height;
  private int width;
  private final String shape;
  private final String gridType;
  private final int neighborhoodSize;

  /**
   * Constructor fills fields using XML data.
   * Assumptions: parameters contains all the information required to create appropriate cell.
   * cellArrangement represents a valid square tesselation grid.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters game settings from XML
   * @throws Exception invalid grid parameters or cell state
   */
  public GridHelper(List<String> cellArrangement, String[] params, Map<String, Integer> parameters)
      throws Exception {
    try {
      this.shape = params[0];
      this.gridType = params[1];
      this.height = parameters.get(Simulation.HEIGHT);
      this.width = parameters.get(Simulation.WIDTH);
      this.neighborhoodSize = parameters.get(NEIGHBORHOOD_SIZE);
      setupGrid(cellArrangement, parameters);
      setupNeighbors();
    } catch (Exception e) {
      throw new Exception(INVALID_GRID_PARAMETERS);
    }
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
   * Returns the map storing cells and their neighbors.
   *
   * @return map with cell and neighbors
   */
  protected int[] getNeighbors(Cell cell) {
    return this.neighbors.get(cell);
  }

  protected Map<Cell, int[]> getAllNeighbors() {
    return this.neighbors;
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

  protected String getGridType() {
    return this.gridType;
  }

  protected void expand(int width, int height, List<Cell> grid) {
    this.width = width;
    this.height = height;
    this.grid = grid;
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
        parameters.put(Cell.STATE_KEY, Integer.parseInt(state));
        Cell cell = chooseCell(parameters);
        if (cell.isValidState()) {
          this.grid.add(cell);
        } else {
          throw new Exception(INVALID_CELL_STATE);
        }
      }
    }
  }

  /**
   * Used by setupGrid(). Create cell object matching Grid type.
   * Assumptions: Parameters contains XML information and cell state
   *
   * @param parameters cell state and game parameters from XML
   * @return appropriate cell object
   */
  protected abstract Cell chooseCell(Map<String, Integer> parameters) throws Exception;

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
    if (gridType.equals(Simulation.GRID_OPTIONS.get(2))) {
      wrapNeighbors();
    }
  }

  private void wrapNeighbors() {
    for (int i = 0; i < this.grid.size(); i++) {
      Cell cell = this.grid.get(i);
      int[] neighbors = this.neighbors.get(cell);
      List<Integer> wrappedNeighbors = edgeCellNeighbors(i);
      for (int j : neighbors) {
        wrappedNeighbors.add(j);
      }
      this.neighbors.put(cell, convertListToIntArray(wrappedNeighbors));
    }
  }

  private List<Integer> edgeCellNeighbors(int index) {
    List<Integer> wrappedNeighbors = new ArrayList<>();
    boolean top = index < this.width;
    boolean bottom = index / this.width == this.height - 1;
    boolean left = (index - 1) / this.width != index / this.width;
    boolean right = (index + 1) / this.width != index / this.width;
    if (top) {
      wrappedNeighbors.add(this.width * this.height - this.width + index);
    }
    if (bottom) {
      wrappedNeighbors.add(index % this.width);
    }
    if (left) {
      wrappedNeighbors.add(index + this.width - 1);
    }
    if (right) {
      wrappedNeighbors.add(index - this.width + 1);
    }
    return wrappedNeighbors;
  }

  /**
   * Used by setupNeighbors(). Finds neighboring indexes in arraylist representation of grid. Throws
   * an exception if neighborhood size is not a valid size. This is the case when
   * Assumptions: Grid is a square tesselation. Looking for 8 Moore neighbors.
   *
   * @param index center index
   * @return neighboring indexes
   * @throws Exception neighborhood size is inconsistent or shape is invalid
   */
  protected int[] pullNeighborIndexes(int index) throws Exception {
    int[] variance = decideNeighborhood(index);

    if (variance.length == 0) {
      throw new Exception(INVALID_NEIGHBORHOOD_SIZE);
    }

    ArrayList<Integer> possibleIndexes = new ArrayList<>();
    for (int i : variance) {
      possibleIndexes.add(i + index);
    }
    List<Integer> validIndexes = removeInvalidIndexes(index, possibleIndexes);
    return convertListToIntArray(validIndexes);
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
      case SQUARE -> square();
      case TRIANGLE -> triangle(index);
      case HEXAGON -> hexagon(index);
      default -> throw new Exception(INVALID_SHAPE);
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
      case SQUARE -> squareSmall();
      case TRIANGLE -> triangleSmall(index);
      case HEXAGON -> hexagon(index);
      default -> throw new Exception(INVALID_SHAPE);
    };
  }

  /**
   * Used by decideNeighborhood(). Calculates the values to add to center cell's index to get
   * neighboring indexes for a grid of squares. Returns empty array if neighborhood size is not an
   * option.
   * Assumptions: Each row has the same number of squares.
   *
   * @return int[] of calculations to get neighboring indexes
   */
  private int[] square() {
    int w = this.width;
    return switch (this.neighborhoodSize) {
      case SQUARE_SIDES_MIN -> new int[]{-1 * w, -1, 1, w};
      case SQUARE_SIDES_MAX -> new int[]{-1 - w, -1 * w, 1 - w, -1, 1, -1 + w, w, 1 + w};
      default -> new int[]{};
    };
  }

  /**
   * Used by decideSmallNeighborhood(). Same as square() but only cardinal direction neighbors.
   * Returns empty array if neighborhoodSize is not 4.
   *
   * @return cardinal neighbors
   */
  private int[] squareSmall() {
    if (this.neighborhoodSize != SQUARE_SIDES_MIN) {
      return new int[]{};
    }
    int w = this.width;
    return new int[]{-1 * w, -1, 1, w};
  }

  /**
   * Used by decideNeighborhood(). Calculates the values to add to center cell's index to get
   * neighboring indexes for a grid of triangles. Returns empty array if neighborhood size is not an
   * option.
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
        case TRIANGLE_SIDES_MIN -> new int[]{-1, 1, w};
        case TRIANGLE_SIDES_MAX -> new int[]{-1 - w, -1 * w, 1 - w, -2, -1, 1, 2, -2 + w, -1 + w, w, 1 + w, 2 + w};
        default -> new int[]{};
      };
    } else {
      return switch (this.neighborhoodSize) {
        case TRIANGLE_SIDES_MIN -> new int[]{-1 * w, -1, 1};
        case TRIANGLE_SIDES_MAX -> new int[]{-2 - w, -1 - w, -1 * w, 1 - w, 2 - w, -2, -1, 1, 2, -1 + w, w, 1 + w};
        default -> new int[]{};
      };
    }
  }

  /**
   * Used by decideSmallNeighborhood(). Like triangle() but only immediate 3 neighbors.
   *
   * @param index center triangle index
   * @return int[] of calculations to get neighboring indexes
   */
  private int[] triangleSmall(int index) {
    int w = this.width;
    boolean trianglePointy = isTriangleTopPointy(index);

    if (trianglePointy && neighborhoodSize == TRIANGLE_SIDES_MIN) {
      return new int[]{-1, 1, w};
    } else if (neighborhoodSize == TRIANGLE_SIDES_MIN) {
      return new int[]{-1 * w, -1, 1};
    } else {
      return new int[]{};
    }
  }

  /**
   * Used by triangle() and triangleSmall(). Distinguishes between pointy top triangles and pointy
   * bottom triangles using index values. If assumptions hold true, in even rows the even indexes
   * (first in row is 0) are pointy top triangles. In odd rows, the odd indexes are pointy top
   * triangles. Therefore, adding row number and index within the row will give whether the triangle
   * is pointy top or pointy bottom.
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
   * Used by decideNeighborhood() and decideSmallNeighborhood. Calculates the values to add to
   * center cell's index to get neighboring indexes for a grid of hexagon. Returns empty array if
   * neighborhood size is not an option.
   * Assumptions: Hexagon grid orients hexagons with corners up and down. Second row starts at the
   * bottom right of the first hexagon of the first row. Third row starts at the bottom left of the
   * first hexagon of the second row. Rows repeat in that manner. Each row has the same number of
   * hexagons.
   *
   * @param index center hexagon index
   * @return int[] of calculations to get neighboring indexes
   */
  private int[] hexagon(int index) {
    if (this.neighborhoodSize != HEXAGON_SIDES) {
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
  private int[] convertListToIntArray(List<Integer> validIndexes) {
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

}
