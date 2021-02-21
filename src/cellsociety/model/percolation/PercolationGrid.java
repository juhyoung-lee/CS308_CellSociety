package cellsociety.model.percolation;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import cellsociety.model.IndexVariance;
import java.util.List;
import java.util.Map;

public class
PercolationGrid extends Grid {

  /**
   * Constructor.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters      game settings from XML
   */
  public PercolationGrid(List<String> cellArrangement, String shape,
      Map<String, Integer> parameters) throws Exception {
    super(cellArrangement, shape, parameters);
  }

  /**
   * Used by setupGrid(). Create cell object matching Grid type. Assumptions: Parameters contains
   * XML information and cell state
   *
   * @param parameters cell state and game parameters from XML
   * @return appropriate cell object
   */
  @Override
  protected Cell chooseCell(Map<String, Integer> parameters) {
    return new PercolationCell(parameters);
  }

  /**
   * Used by pullNeighborIndexes(). Returns array of values to be added to center index to get
   * neighboring indexes. Ex: (-1 * this.width, -1, 1, this.width)
   *
   * @param index center index
   * @return values for computing neighboring indexes
   */
  @Override
  protected int[] decideNeighborhood(int index) throws Exception {
    // percolation only looks at cells above and next
    int neighborhoodSize = getNeighborhoodSize();

    return switch (getShape()) {
      case "square" -> square();
      case "triangle" -> triangle(index);
      case "hexagon" -> hexagon(index);
      default -> throw new Exception("Invalid shape: " + getShape());
    };
  }

  private int[] square() {
    int width = getDimensions()[0];
    return new int[]{-1 * width, -1, 1};
  }

  private int[] triangle(int index) {
    int w = getDimensions()[0];
    int height = getDimensions()[1];
    boolean trianglePointy = IndexVariance.isTriangleTopPointy(index, height);

    if (trianglePointy && getNeighborhoodSize() == 3) {
      return new int[]{-1, 1};
    } else if (getNeighborhoodSize() == 3) {
      return new int[]{-1 * w, -1, 1};
    } else {
      return new int[]{};
    }
  }

  private int[] hexagon(int index) {
    if (getNeighborhoodSize() != 6) {
      return new int[]{};
    }

    int w = getDimensions()[0];
    boolean evenRow = (index / w) % 2 == 0;
    if (evenRow) {
      return new int[]{-1 - w, -1 * w, -1, 1, -1 + w, w};
    } else {
      return new int[]{-1 * w, 1 - w, -1, 1, w, 1 + w};
    }
  }
}
