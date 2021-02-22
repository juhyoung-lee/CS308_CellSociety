package cellsociety.model.bylsloop;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.List;
import java.util.Map;

public class BylsLoopGrid extends Grid {

  /**
   * Constructor fills fields using XML data. Assumptions: parameters contains all the information
   * required to create appropriate cell. cellArrangement represents a valid square tesselation grid.
   *
   * @param cellArrangement cell grid from XML
   * @param cellParameters      game settings from XML
   * @throws Exception invalid cell state
   */
  public BylsLoopGrid(List<String> cellArrangement, String[] gridParameters,
      Map<String, Integer> cellParameters) throws Exception {
    super(cellArrangement, gridParameters, cellParameters);
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
    return new BylsLoopCell(parameters);
  }

  /**
   * Used by pullNeighborIndexes(). Returns array of values to be added to center index to get
   * neighboring indexes. Assumptions: Grid is a square tesselation.
   *
   * @param index center index
   * @return values for computing neighboring indexes
   */
  @Override
  protected int[] decideNeighborhood(int index) throws Exception {
    return switch (getShape()) {
      case "square" -> square();
      case "triangle" -> triangle(index);
      case "hexagon" -> hexagon(index);
      default -> throw new Exception("Invalid shape: " + getShape());
    };
  }

  private int[] square() {
    int width = getInitialDimensions()[0];
    if (getNeighborhoodSize() == 4) {
      // return new int[]{-1, -1 * width, 1, width}; //clockwise
      return new int[]{-1, width, 1, -1 * width};
    }
    return new int[]{};
  }

  private int[] triangle(int index) {
    int w = getInitialDimensions()[0];
    boolean trianglePointy = isTriangleTopPointy(index);

    if (trianglePointy && getNeighborhoodSize() == 3) {
      // return new int[]{-1, 1, w}; //clockwise
      return new int[]{-1, w, 1};
    } else if (getNeighborhoodSize() == 3) {
      // return new int[]{-1, -1 * w, 1}; //clockwise
      return new int[]{-1, 1, -1 * w};
    } else {
      return new int[]{};
    }
  }

  public int[] hexagon(int index) {
    if (getNeighborhoodSize() != 6) {
      return new int[]{};
    }
    int w = getInitialDimensions()[0];
    boolean evenRow = (index / w) % 2 == 0;
    if (evenRow) {
      // return new int[]{-1, -1 - w, -1 * w, 1, w, -1 + w}; //clockwise
      return new int[]{-1, -1 + w, w, 1, -1 * w, -1 - w};
    } else {
      // return new int[]{-1, -1 * w, 1 - w, 1, 1 + w, w}; //clockwise
      return new int[]{-1, w, 1 + w, 1, 1 - w, -1 * w};
    }
  }
}
