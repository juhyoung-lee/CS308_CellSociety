package cellsociety.model.wator;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class WaTorGrid extends Grid {

  /**
   * Constructor.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters      game settings from XML
   */
  public WaTorGrid(ArrayList<String> cellArrangement,
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
    return new WaTorCell(parameters);
  }

  // TODO: update arraylist to queue.
  protected void moveCell(int index) {
    Cell center = getGrid().get(index);
    int[] neighbors = getNeighbors(center);
    ArrayList<Integer> places = new ArrayList<>();
    for (int i : neighbors) {
      places.add(i);
    }
    Collections.shuffle(places);

    HashMap state = getIssues(index);
    for (Integer neighborIndex : places) {
      if (getGrid().get(neighborIndex).receiveUpdate(state)) {
        return;
      }
    }
    getGrid().get(index).receiveUpdate(state);
  }
}
