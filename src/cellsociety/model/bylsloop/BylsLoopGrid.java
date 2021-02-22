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
      case SQUARE -> square();
      default -> throw new Exception(INVALID_SHAPE);
    };
  }

  private int[] square() {
    int width = getDimensions()[0];
    if (getNeighborhoodSize() == SQUARE_SIDES_MIN) {
      return new int[]{-1, width, 1, -1 * width};
    }
    return new int[]{};
  }
}
