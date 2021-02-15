package cellsociety.model.wator;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class WaTorGrid extends Grid {

  private HashMap<String, Integer>[] issues;

  /**
   * Constructor.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters      game settings from XML
   */
  public WaTorGrid(ArrayList<String> cellArrangement,
      HashMap<String, Integer> parameters) {
    super(cellArrangement, parameters);
    issues = new HashMap[super.grid.size()];
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

  /**
   * Loads updates for cells, handles movement, pushes updates.
   */
  @Override
  public void updateCells() {
    clearIssues();
    prepareUpdates();
    handleIssues();
    pushCellUpdates();
  }

  /**
   * Sets issues to null
   */
  private void clearIssues() {
    for (int i = 0; i < issues.length; i++) {
      issues[i] = null;
    }
  }

  /**
   * Prepares cell updates and stores issues if they arise
   */
  private void prepareUpdates() {
    for (int i = 0; i < grid.size(); i++) {
      int[] neighborStates = pullNeighborStates(i);
      HashMap<String, Integer> movement = grid.get(i).prepareNewState(neighborStates);
      if (movement.get("state") != -1) {
        issues[i] = movement;
      }
    }
  }

  /**
   *
   */
  private void handleIssues() {
    for (int i = 0; i < issues.length; i++) {
      if (issues[i] != null) {
        moveCell(i);
      }
    }
  }

  private void pushCellUpdates() {
    for (Cell cell : grid) {
      cell.updateState();
    }
  }

  // TODO: update arraylist to queue.
  private void moveCell(int index) {
    Cell center = super.grid.get(index);
    int[] neighbors = super.neighbors.get(center);
    ArrayList<Integer> places = new ArrayList<>();
    for (int i : neighbors) {
      places.add(i);
    }
    Collections.shuffle(places);

    HashMap state = issues[index];
    for (Integer neighborIndex : places) {
      if (super.grid.get(neighborIndex).receiveUpdate(state)) {
        return;
      }
    }
    super.grid.get(index).receiveUpdate(state);
  }
}
