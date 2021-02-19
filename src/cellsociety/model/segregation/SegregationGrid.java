package cellsociety.model.segregation;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SegregationGrid extends Grid {

  /**
   * Constructor.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters      game settings from XML
   */
  public SegregationGrid(List<String> cellArrangement,
      Map<String, Integer> parameters) throws Exception {
    super(cellArrangement, parameters);
  }

  /**
   * Used by super.setupGrid(). Creates cell object matching Grid type. Assumptions: Parameters
   * contains XML information and cell state
   *
   * @param parameters cell state and game parameters from XML
   * @return appropriate cell object
   */
  @Override
  protected Cell chooseCell(Map<String, Integer> parameters) {
    return new SegregationCell(parameters);
  }

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
