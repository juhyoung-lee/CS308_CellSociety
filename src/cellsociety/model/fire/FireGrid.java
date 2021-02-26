package cellsociety.model.fire;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.List;
import java.util.Map;

/**
 * Allows for the simulation of Fire.
 * Assumptions: game will follow the rules laid out in FireCell.
 * Dependencies: java.util.*, GridHelper, Grid, Cell, FireCell
 * Examples:
 * '''
 * Grid grid = new FireGrid(cellA, gridP, cellP);
 * grid.updateCells();
 * '''
 */
public class FireGrid extends Grid {

  /**
   * Initializes grid and cells.
   *
   * @param cellArrangement cell grid from XML
   * @param cellParameters game settings from XML
   * @param gridParameters grid settings from XML
   * @throws Exception when parameters are invalid
   */
  public FireGrid(List<String> cellArrangement, String[] gridParameters,
      Map<String, Integer> cellParameters) throws Exception {
    super(cellArrangement, gridParameters, cellParameters);
  }

  /**
   * Returns FireCell object
   *
   * @param parameters cell state and game parameters from XML
   * @return FireCell object
   */
  @Override
  protected Cell chooseCell(Map<String, Integer> parameters) throws Exception {
    return new FireCell(parameters);
  }

  /**
   * Forces small neighborhood size as fire can only spread cardinally.
   *
   * @param index center index
   * @return values for computing neighboring indexes
   * @throws Exception when neighborhood size does not match input from XML
   */
  @Override
  protected int[] decideNeighborhood(int index) throws Exception {
    return decideSmallNeighborhood(index);
  }
}
