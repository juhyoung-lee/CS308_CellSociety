package cellsociety.model.gameoflife;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.List;
import java.util.Map;

/**
 * Allows for the simulation of GameOfLife.
 * Assumptions: game will follow the rules laid out in GameOfLifeCell.
 * Dependencies: java.util.*, GridHelper, Grid, Cell, GameOfLifeCell
 * Examples:
 * '''
 * Grid grid = new GameOfLifeGrid(cellA, gridP, cellP);
 * grid.updateCells();
 * '''
 */
public class GameOfLifeGrid extends Grid {

  /**
   * Fills grid and instantiates cells.
   *
   * @param cellArrangement cell states and position from XML
   * @param gridParameters grid settings from XML
   * @param cellParameters cell settings from XML
   * @throws Exception when parameters are invalid
   */
  public GameOfLifeGrid(List<String> cellArrangement, String[] gridParameters,
      Map<String, Integer> cellParameters) throws Exception {
    super(cellArrangement, gridParameters, cellParameters);
  }

  /**
   * Returns GameOfLife cell object.
   *
   * @param parameters cell state and game parameters from XML
   * @return GameOfLife cell object
   */
  @Override
  protected Cell chooseCell(Map<String, Integer> parameters) {
    return new GameOfLifeCell(parameters);
  }
}
