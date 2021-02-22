package cellsociety.model.rps;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.List;
import java.util.Map;

public class RPSGrid extends Grid {

  /**
   * Constructor fills fields using XML data. Assumptions: parameters contains all the information
   * required to create appropriate cell. cellArrangement represents a valid square tesselation grid.
   *
   * @param cellArrangement cell grid from XML
   * @param gridParameters  game settings from XML
   */
  public RPSGrid(List<String> cellArrangement, String[] gridParameters,
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
  protected Cell chooseCell(Map<String, Integer> parameters) throws Exception {
    return new RPSCell(parameters);
  }
}
