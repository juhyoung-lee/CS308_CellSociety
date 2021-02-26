package cellsociety.model.rps;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.List;
import java.util.Map;

/**
 * Allows for the simulation of RPS.
 * Assumptions: game will follow the rules laid out in RPSCell.
 * Dependencies: java.util.*, GridHelper, Grid, Cell, RPSCell
 * Examples:
 * '''
 * Grid grid = new RPSGrid(cellA, gridP, cellP);
 * grid.updateCells();
 * '''
 */
public class RPSGrid extends Grid {

  /**
   * Constructor fills fields using XML data. Assumptions: parameters contains all the information
   * required to create appropriate cell. cellArrangement represents a valid square tesselation grid.
   *
   * @param cellArrangement cell grid from XML
   * @param gridParameters  game settings from XML
   * @param cellParameters cell settings from XML
   * @throws Exception when parameters are invalid
   */
  public RPSGrid(List<String> cellArrangement, String[] gridParameters,
      Map<String, Integer> cellParameters) throws Exception {
    super(cellArrangement, gridParameters, cellParameters);
  }

  /**
   * Returns RPS cell object.
   *
   * @param parameters cell state and game parameters from XML
   * @return RPS cell object
   */
  @Override
  protected Cell chooseCell(Map<String, Integer> parameters) throws Exception {
    return new RPSCell(parameters);
  }
}
