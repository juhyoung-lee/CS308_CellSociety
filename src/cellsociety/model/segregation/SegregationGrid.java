package cellsociety.model.segregation;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.ArrayList;
import java.util.HashMap;

public class SegregationGrid extends Grid {

  private ArrayList<HashMap<String,Integer>> issue;

  /**
   * Constructor.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters      game settings from XML
   */
  public SegregationGrid(ArrayList<String> cellArrangement,
      HashMap<String, Integer> parameters) {
    super(cellArrangement, parameters);
    issues = new int[super.grid.size()];
  }

  /**
   * Used by super.setupGrid(). Creates cell object matching Grid type.
   * Assumptions: Parameters contains XML information and cell state
   *
   * @param parameters cell state and game parameters from XML
   * @return appropriate cell object
   */
  @Override
  protected Cell chooseCell(HashMap<String, Integer> parameters) {
    return new SegregationCell(parameters);
  }

  /**
   * Loads updates for cells, handles movement, pushes updates.
   */
  @Override
  public void updateCells() {
    for (int i = 0; i < issues.length; i++) {
      issues[i] = -1;
    }

    for (int i = 0; i < grid.size(); i++) {
      int[] neighborStates = pullNeighborStates(i);
      HashMap<String, Integer> movement = grid.get(i).prepareNewState(neighborStates);
      if (movement.get("state") != -1) {
        issues[i] = movement;
      }
    }

    for (Cell cell : grid) {
      cell.updateState();
    }
  }
}