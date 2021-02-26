package cellsociety.model.segregation;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Allows for the simulation of Segregation.
 * Assumptions: game will follow the rules laid out in SegregationCell.
 * Dependencies: java.util.*, GridHelper, Grid, Cell, SegregationCell
 * Examples:
 * '''
 * Grid grid = new SegregationGrid(cellA, gridP, cellP);
 * grid.updateCells();
 * '''
 */
public class SegregationGrid extends Grid {

  /**
   * Constructs the grid by filling out and instantiating cells.
   *
   * @param cellArrangement position and states of cells from XML
   * @param gridParameters grid settings from XML
   * @param cellParameters cell settings from XML
   * @throws Exception when parameters are invalid
   */
  public SegregationGrid(List<String> cellArrangement, String[] gridParameters,
      Map<String, Integer> cellParameters) throws Exception {
    super(cellArrangement, gridParameters, cellParameters);
  }

  /**
   * Creates SegregationCell.
   *
   * @param parameters cell state and game parameters from XML
   * @return Segregation cell object
   */
  @Override
  protected Cell chooseCell(Map<String, Integer> parameters) throws Exception {
    return new SegregationCell(parameters);
  }

  /**
   * Returns random cells to try and move to.
   *
   * @param index of cell trying to move
   * @return all cells except index in random order
   */
  @Override
  protected List<Integer> findPotentialMoves(int index) {
    List<Integer> places = new ArrayList<>();
    for (int i = 0; i < getGrid().size(); i++) {
      if (i != index) {
        places.add(i);
      }
    }
    return places;
  }
}
