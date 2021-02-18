package cellsociety.model.gameoflife;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.List;
import java.util.Map;

public class GameOfLifeGrid extends Grid {

  /**
   * Constructor.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters      game settings from XML
   */
  public GameOfLifeGrid(List<String> cellArrangement,
      Map<String, Integer> parameters) {
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
    return new GameOfLifeCell(parameters);
  }
}
