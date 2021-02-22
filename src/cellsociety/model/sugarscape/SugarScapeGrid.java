package cellsociety.model.sugarscape;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;

public class SugarScapeGrid extends Grid {

  public SugarScapeGrid(List<String> cellArrangement, String[] gridParameters,
      Map<String, Integer> cellParameters) throws Exception {
    super(cellArrangement, gridParameters, cellParameters);
  }

  /**
   * Used by setupGrid(). Create cell object matching Grid type. Assumptions: Parameters contains
   * XML information and cell state
   *
   * @param parameters cell state and game parameters from XML
   * @return appropriate cell object
   */
  @Override
  protected Cell chooseCell(Map<String, Integer> parameters) throws Exception {
    return new SugarScapeCell(parameters);
  }

  /**
   * Must be overwritten to function. Returns array list of indexes that should be checked to
   * receive a moving cell.
   *
   * @param index of cell trying to move
   * @return indexes to be checked for receiving update
   */
  @Override
  protected List<Integer> findPotentialMoves(int index) {
    // highest patch sugar
    Cell cell = getGrid().get(index);
    int[] neighbors = getNeighbors(cell);

    List<Pair<Integer, Integer>> orderedNeighbors = new ArrayList<>();
    for (int i : neighbors) {
      SugarScapeCell neighbor = (SugarScapeCell) getGrid().get(i);
      int patchSugar = neighbor.getPatchSugar();
      orderedNeighbors.add(new Pair<>(i, patchSugar));
    }
    orderedNeighbors.sort(Comparator.comparingInt(Pair::getValue));

    List<Integer> potentialMoves = new ArrayList<>();
    for (Pair<Integer, Integer> p : orderedNeighbors) {
      potentialMoves.add(p.getKey());
    }
    return potentialMoves;
  }





}
