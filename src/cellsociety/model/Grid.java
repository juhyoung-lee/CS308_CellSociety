package cellsociety.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps track of Cells in a Grid.
 */
public abstract class Grid extends GridHelper {

  private Map<String, Integer>[] issues;

  public Grid(List<String> cellArrangement, String shape, Map<String, Integer> parameters)
      throws Exception {
    super(cellArrangement, shape, parameters);
    issues = new HashMap[getGrid().size()];
  }

  /**
   * Clears issues array. Runs prepareNextState() on all cells. Handles any returned issues where
   * cells wish to "move". Runs updateState() on all cells, finalizing state changes. For a cell to
   * properly "move" or pass information to another cell, moveCell() will need to be overwritten.
   * Assumptions: No movement of cells.
   */
  public void updateCells() {
    clearIssues();
    prepareCellUpdates();
    handleIssues();
    pushCellUpdates();
  }

  /**
   * Used by prepareCellUpdates(). Compiles neighboring cell states. Necessary as neighbors field
   * variable only stores cell indexes.
   * Assumptions: Cell passes state as int. Only state is required to update a cell.
   *
   * @param index center cell position
   * @return states of neighboring cells
   */
  private int[] pullNeighborStates(int index) {
    Cell currentCell = getGrid().get(index);
    Cell neighborCell;
    int[] neighborIndexes = getNeighbors(currentCell);
    int[] neighborStates = new int[neighborIndexes.length];

    for (int i = 0; i < neighborIndexes.length; i++) {
      neighborCell = getGrid().get(neighborIndexes[i]);
      neighborStates[i] = neighborCell.getState();
    }
    return neighborStates;
  }

  /**
   * Clears issues in preparation of a new cycle.
   * Assumptions: Issues has already been instantiated.
   */
  private void clearIssues() {
    Arrays.fill(issues, null);
  }

  /**
   * Runs prepareNextState() on all cells and catalogs any issues or moving cells that arise.
   * Assumptions: Cells will return a HashMap with -1 for "state" if no movement occurs
   */
  private void prepareCellUpdates() {
    for (int i = 0; i < getGrid().size(); i++) {
      int[] neighborStates = pullNeighborStates(i);
      Map<String, Integer> movement = getGrid().get(i).prepareNextState(neighborStates);
      if (movement.get("state") != -1) {
        issues[i] = movement;
      }
    }
  }

  /**
   * Finds any issues cataloged and calls moveCell() on it to be handled.
   * Assumptions: MoveCell has been overwritten if cells do move or pass information around.
   */
  private void handleIssues() {
    for (int i = 0; i < issues.length; i++) {
      if (issues[i] != null) {
        moveCell(i);
      }
    }
  }

  /**
   * Runs updateState() on all cells and finalizes the cycle.
   * Assumptions: Every cell has their proper preparedState and all issues have been handled.
   */
  private void pushCellUpdates() {
    for (Cell cell : getGrid()) {
      cell.updateState();
    }
  }

  /**
   * Handles cell movement or information passing by calling receiveUpdate() on whichever
   * neighboring cell will be passed the information. If no cell can receive an update, the cell
   * will not move. Requires overriding of findPotentialMoves() to properly function.
   * Assumptions: Any cell wishing to move or pass information has been cataloged in issues.
   *
   * @param index cell trying to move or pass information
   */
  private void moveCell(int index) {
    List<Integer> places = findPotentialMoves(index);

    Collections.shuffle(places);
    Map<String, Integer> state = getIssues(index);
    for (Integer neighborIndex : places) {
      if (getGrid().get(neighborIndex).receiveUpdate(state)) {
        return;
      }
    }
    getGrid().get(index).receiveUpdate(state);
  }

  /**
   * Must be overwritten to function. Returns array list of indexes that should be checked to
   * receive a moving cell.
   *
   * @param index of cell trying to move
   * @return indexes to be checked for receiving update
   */
  protected List<Integer> findPotentialMoves(int index) {
    return new ArrayList<>();
  }

  /**
   * Gives direct access to issues field variable, as it is only needed if subclass cell wants to
   * move or pass information. Exists as feature to use if needed.
   *
   * @return HashMap parameters corresponding to index/cell of issue.
   */
  protected Map<String, Integer> getIssues(int index) {
    return this.issues[index];
  }

}
