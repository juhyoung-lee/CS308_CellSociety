package cellsociety.model.fire;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.ArrayList;
import java.util.HashMap;

public class FireGrid extends Grid {

  /**
   * Constructor.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters      game settings from XML
   */
  public FireGrid(ArrayList<String> cellArrangement,
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
    return new FireCell(parameters);
  }

  @Override
  protected int[] neighborVariances(int index) {
    int width = getDimensions()[0];
    // percolation only looks at cells above and next
    return new int[]{-1 * width, -1, 1, width};
  }
}
