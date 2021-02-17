package cellsociety.model.segregation;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SegregationGrid extends Grid {

  /**
   * Constructor.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters      game settings from XML
   */
  public SegregationGrid(ArrayList<String> cellArrangement,
      HashMap<String, Integer> parameters) {
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
  protected Cell chooseCell(HashMap<String, Integer> parameters) {
    return new SegregationCell(parameters);
  }

  @Override
  protected ArrayList<Integer> findPotentialMoves(int index) {
    ArrayList<Integer> places = new ArrayList<>();
    for (int i = 0; i < getGrid().size(); i++) {
      if (i == index) {
        continue;
      }
      places.add(i);
    }
    return places;
  }
}
