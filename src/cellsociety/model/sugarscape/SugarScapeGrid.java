package cellsociety.model.sugarscape;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;

/**
 * Allows for the simulation of SugarScape.
 * Assumptions: game will follow the rules laid out in SugarScapeCell.
 * Dependencies: java.util.*, GridHelper, Grid, Cell, SugarScapeCell
 * Examples:
 * '''
 * Grid grid = new SugarScapeGrid(cellA, gridP, cellP);
 * grid.updateCells();
 * '''
 */
public class SugarScapeGrid extends Grid {

  /**
   * Constructor fills grid and instantiates cells.
   *
   * @param cellArrangement cell grid from XML
   * @param gridParameters grid settings from XML
   * @param cellParameters cell settings from XML
   * @throws Exception when parameters are invalid
   */
  public SugarScapeGrid(List<String> cellArrangement, String[] gridParameters,
      Map<String, Integer> cellParameters) throws Exception {
    super(cellArrangement, gridParameters, cellParameters);
  }

  /**
   * Returns SugarScapeCell object. Assumptions: Parameters contains
   * XML information and cell state
   *
   * @param parameters cell state and game parameters
   * @return SugarScapeCell
   */
  @Override
  protected Cell chooseCell(Map<String, Integer> parameters) throws Exception {
    return new SugarScapeCell(parameters);
  }

  /**
   * Orders neighboring cells by their sugar patch level as agents will move to the highest level.
   * Assumptions: index is within the grid
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
