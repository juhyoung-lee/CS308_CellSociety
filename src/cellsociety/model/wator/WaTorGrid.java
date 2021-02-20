package cellsociety.model.wator;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WaTorGrid extends Grid {

  /**
   * Constructor.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters      game settings from XML
   */
  public WaTorGrid(List<String> cellArrangement,
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
    return new WaTorCell(parameters);
  }

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
