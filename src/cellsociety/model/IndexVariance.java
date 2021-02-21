package cellsociety.model;

public class IndexVariance {

  private final int index;
  private final int width;
  private final int height;
  private final int neighborhoodSize;

  /**
   * Constructor.
   * Assumptions: Parameters are valid inputs: 0 <= index < width * height, width > 0, height > 0,
   * neighborhoodSize > 0.
   *
   * @param index center cell position
   * @param width number of columns
   * @param height number of rows
   * @param neighborhoodSize number of neighbors to look for
   */
  public IndexVariance(int index, int width, int height, int neighborhoodSize) {
    this.index = index;
    this.width = width;
    this.height = height;
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
   * Calculates the values to add to center cell's index to get neighboring indexes for a grid of
   * triangles. Distinguishes between pointy top triangles and pointy bottom triangles using index
   * values. If assumptions hold true, in even rows the even indexes (first in row is 0) are pointy
   * top triangles. In odd rows, the odd indexes are pointy top triangles. Therefore, adding row
   * number and index within the row will give whether the triangle is pointy top or pointy bottom.
   * Returns empty array if neighborhood size is not an option.
   * Assumptions: Row 0 Index 0 is a triangle pointing up. Each row has the same number of
   * triangles.
   *
   * @return int[] of calculations to get neighboring indexes
   */
  public int[] triangle() {
    int rowNumber = this.index / this.height;
    int indexInRow = this.index % this.height;
    boolean pointyTop = (rowNumber + indexInRow) % 2 == 0;

    int w = this.width;
    int[] variance;
    if (pointyTop) {
      variance = switch (this.neighborhoodSize) {
        case 3 -> new int[]{-1, 1, w};
        case 12 -> new int[]{-1 - w, -1 * w, 1 - w, -2, -1, 1, 2, -2 + w, -1 + w, w, 1 + w, 2 + w};
        default -> new int[]{};
      };
    } else {
      variance = switch (this.neighborhoodSize) {
        case 3 -> new int[]{-1 * w, -1, 1};
        case 12 -> new int[]{-2 - w, -1 - w, -1 * w, 1 - w, 2 - w, -2, -1, 1, 2, -1 + w, w, 1 + w};
        default -> new int[]{};
      };
    }
    return variance;
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
    return new int[]{-1 * w, 1 - w, -1, 1, w, 1 + w};
  }
}
