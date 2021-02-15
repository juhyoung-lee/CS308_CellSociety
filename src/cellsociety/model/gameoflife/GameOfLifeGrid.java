package cellsociety.model.gameoflife;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.ArrayList;
import java.util.HashMap;

public class GameOfLifeGrid extends Grid {

  /**
   * Constructor.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters      game settings from XML
   */
  public GameOfLifeGrid(ArrayList<String> cellArrangement,
      HashMap<String, Integer> parameters) {
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
  protected Cell chooseCell(HashMap<String, Integer> parameters) {
    return new GameOfLifeCell(parameters);
  }

  /**
   * Used by pullNeighborIndexes(). Returns array of values to be added to center index to get
   * neighboring indexes. Ex: (-1 * this.width, -1, 1, this.width)
   *
   * @param index center index
   * @return values for computing neighboring indexes
   */
  @Override
  protected int[] neighborVariances(int index) {
    int width = getDimensions()[0];
    return new int[]{-1 - width, -1 * width, 1 - width, -1, 1, -1 + width, width, 1 + width};
  }
}
