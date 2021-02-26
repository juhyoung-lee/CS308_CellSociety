package cellsociety.model.wator;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * WaTor Grid implementation. Allows for the simulation of WaTor.
 * Assumptions: game will follow the rules laid out in WaTorCell.
 * Dependencies: GridHelper, Grid, WatorCell, Cell, java.util.*
 * Examples:
 * '''
 * Grid grid = new WaTorGrid(cellA, gridP, cellP);
 * grid.updateCells();
 * '''
 */
public class WaTorGrid extends Grid {

  /**
   * Constructor creates the grid and instantiates all the cells as WaTorCells.
   *
   * @param cellArrangement cell grid from XML
   * @param gridParameters grid settings from XML
   * @param cellParameters game settings from XML
   * @throws Exception when parameters are invalid
   */
  public WaTorGrid(List<String> cellArrangement, String[] gridParameters,
      Map<String, Integer> cellParameters) throws Exception {
    super(cellArrangement, gridParameters, cellParameters);
  }

  /**
   * Creates matching game type cell.
   *
   * @param parameters cell state and game parameters from XML
   * @return appropriate cell object
   */
  @Override
  protected Cell chooseCell(Map<String, Integer> parameters) throws Exception {
    return new WaTorCell(parameters);
  }

  /**
   * Grabs neighboring cells as potential places for a cell to move to.
   *
   * @param index of cell trying to move
   * @return [neighboring cell indexes]
   */
  @Override
  protected List<Integer> findPotentialMoves(int index) {
    Cell center = getGrid().get(index);
    int[] neighbors = getNeighbors(center);
    List<Integer> places = new ArrayList<>();
    for (int i : neighbors) {
      places.add(i);
    }
    return places;
  }
}
