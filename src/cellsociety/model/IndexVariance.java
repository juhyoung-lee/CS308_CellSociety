package cellsociety.model;

/**
 * Helps find neighboring indexes of a cell on a grid.
 */
public class IndexVariance {

  private final int index;
  private final int width;
  private final int neighborhoodSize;

  /**
   * Constructor.
   * Assumptions: Parameters are valid inputs: 0 <= index < width * height, width > 0, height > 0,
   * neighborhoodSize > 0.
   *
   * @param index center cell position
   * @param width number of columns
   * @param neighborhoodSize number of neighbors to look for
   */
  public IndexVariance(int index, int width, int neighborhoodSize) {
    this.index = index;
    this.width = width;
    this.neighborhoodSize = neighborhoodSize;
  }

  /**
   * Calculates the values to add to center cell's index to get neighboring indexes for a grid of
   * squares. Returns empty array if neighborhood size is not an option.
   * Assumptions: Each row has the same number of squares.
   *
   * @return int[] of calculations to get neighboring indexes
   */
  public int[] square() {
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
  public int[] squareCardinal() {
    if (this.neighborhoodSize != 4) {
      return new int[]{};
    }
    int w = this.width;
    return new int[]{-1 * w, -1, 1, w};
  }

  /**
   * Calculates the values to add to center cell's index to get neighboring indexes for a grid of
   * triangles.Returns empty array if neighborhood size is not an option.
   * Assumptions: Row 0 Index 0 is a triangle pointing up. Each row has the same number of
   * triangles.
   *
   * @return int[] of calculations to get neighboring indexes
   */
  public int[] triangle() {
    int w = this.width;
    if (isTriangleTopPointy(this.index, this.width)) {
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
   * @return int[] of calculations to get neighboring indexes
   */
  public int[] triangleImmediate() {
    int w = this.width;
    boolean trianglePointy = IndexVariance.isTriangleTopPointy(this.index, w);

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
   * @param width number of cells in a row in the grid
   * @return whether a triangle is pointy side up or down
   */
  public static boolean isTriangleTopPointy(int index, int width) {
    int rowNumber = index / width;
    int indexInRow = index % width;
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
   * @return int[] of calculations to get neighboring indexes
   */
  public int[] hexagon() {
    if (this.neighborhoodSize != 6) {
      return new int[]{};
    }
    int w = this.width;
    boolean evenRow = (this.index / this.width) % 2 == 0;
    if (evenRow) {
      return new int[]{-1 - w, -1 * w, -1, 1, -1 + w, w};
    } else {
      return new int[]{-1 * w, 1 - w, -1, 1, w, 1 + w};
    }
  }
}
