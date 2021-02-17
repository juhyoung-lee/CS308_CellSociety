package cellsociety.model.segregation;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SegregationGrid extends Grid {

  private HashMap<String, Integer>[] issues;

  /**
   * Constructor.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters      game settings from XML
   */
  public SegregationGrid(ArrayList<String> cellArrangement,
      HashMap<String, Integer> parameters) {
    super(cellArrangement, parameters);
    issues = new HashMap[getGrid().size()];
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

  /**
   * Loads updates for cells, handles movement, pushes updates.
   * TODO: moveCell() is only abstract method?
   */
  @Override
  public void updateCells() {
    clearIssues();
    prepareUpdates();
    handleIssues();
    pushCellUpdates();
  }

  private void clearIssues() {
    for (int i = 0; i < issues.length; i++) {
      issues[i] = null;
    }
  }

  private void prepareUpdates() {
    for (int i = 0; i < getGrid().size(); i++) {
      int[] neighborStates = pullNeighborStates(i);
      HashMap<String, Integer> movement = getGrid().get(i).prepareNextState(neighborStates);
      if (movement.get("state") != -1) {
        issues[i] = movement;
      }
    }
  }

  private void handleIssues() {
    for (int i = 0; i < issues.length; i++) {
      if (issues[i] != null) {
        moveCell(i);
      }
    }
  }

  private void pushCellUpdates() {
    for (Cell cell : getGrid()) {
      cell.updateState();
    }
  }

  private void moveCell(int index) {
    ArrayList<Integer> places = new ArrayList<>();
    for (int i = 0; i < issues.length; i++) {
      if (i == index) {
        continue;
      }
      places.add(i);
    }
    Collections.shuffle(places);

    HashMap state = issues[index];
    for (Integer neighborIndex : places) {
      if (getGrid().get(neighborIndex).receiveUpdate(state)) {
        return;
      }
    }
    getGrid().get(index).receiveUpdate(state);
  }
}