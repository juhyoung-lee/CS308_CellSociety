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
   * @param parameters      game settings from XML
   * @throws Exception invalid cell state
   */
  public BylsLoopGrid(List<String> cellArrangement,
      Map<String, Integer> parameters) throws Exception {
    super(cellArrangement, parameters);
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
   * Counterlockwise pattern starting from index directly bottom.
   * Used by pullNeighborIndexes(). Returns array of values to be added to center index to get
   * neighboring indexes. Assumptions: counts surrounding 8 Moore cells as neighbors. Grid is a
   * square tesselation.
   *
   * @param index center index
   * @return values for computing neighboring indexes
   */
  @Override
  protected int[] neighborVariances(int index) {
    int width = getDimensions()[0];
    return new int[]{width, 1, -1 * width, -1};
  }
}
