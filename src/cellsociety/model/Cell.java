package cellsociety.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Purpose: Represents a cell within the cell automata simulation.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Juhyoung Lee, Jessica Yang
 */
public abstract class Cell {

  public static final int NO_MOVEMENT = -1;
  private Map<String, Integer> moveState = new HashMap<>();
  private int myState;
  private int nextState;
  private int maxStateValue;

  // TODO: make isValid method

  /**
   * Purpose: Constructor for Cell class.
   * Assumptions: TODO
   * Parameters: HashMap config.
   * Exceptions: TODO
   * Returns: Cell object.
   */
  public Cell(Map<String, Integer> config) {
    myState = config.get("state");
  }

  /**
   * Purpose: Checks if assigned state is valid for the Cell subclass.
   * Assumptions: setMaxStateValue has already been called in the constructor of the subclass.
   * Parameters: None.
   * Exceptions: TODO
   * Returns: boolean type.
   */
  public boolean isValidState() {
    for (int i = 0; i <= maxStateValue; i++) {
      if (getState() == i) {
        return true;
      }
    }

    return false;
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: TODO
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: HashMap object. Describes what needs to be moved, if any.
   */
  public abstract Map<String, Integer> prepareNextState(int[] neighborStates);

  /**
   * Purpose: Update current cell state, and return value for other methods to use.
   * Assumptions: TODO
   * Parameters: None.
   * Exceptions: None.
   * Returns: int object.
   */
  public int updateState() {
    myState = nextState;
    nextState = -1;
    return myState;
  }

  /**
   * Purpose: Accepts HashMap information with new state information. Will default to return false.
   * Assumptions: Grid should call this method only on Cells with movement simulations.
   * Parameters:
   * HashMap object.
   * Exceptions: TODO
   * Returns: boolean type.
   */
  public boolean receiveUpdate(Map<String, Integer> newInfo) {
    return false;
  }

  /**
   * Purpose: Sets maxStateValue of the cell.
   * Assumptions: TODO
   * Parameters: int type.
   * Exceptions: None.
   * Returns: None.
   */
  protected void setMaxStateValue(int maxState) {
    maxStateValue = maxState;
  }

  /**
   * Purpose: Returns state of the cell.
   * Assumptions: TODO
   * Parameters: None.
   * Exceptions: None.
   * Returns: int state.
   */
  public int getState() {
    return myState;
  }

  /**
   * Purpose: Returns nextState of the cell.
   * Assumptions: TODO
   * Parameters: None.
   * Exceptions: None.
   * Returns: int state.
   */
  protected int getNextState() {
    return nextState;
  }

  /**
   * Purpose: Sets nextState of the cell.
   * Assumptions: TODO
   * Parameters: int type.
   * Exceptions: None.
   * Returns: None.
   */
  protected void setNextState(int newState) {
    nextState = newState;
  }

  /**
   * Purpose: Sets value in moveState.
   * Assumptions: TODO
   * Parameters: int type.
   * Exceptions: None.
   * Returns: None.
   */
  protected void setMoveStateValue(String key, int value) {
    moveState.put(key, value);
  }

  /**
   * Purpose: Returns deep copy of moveState.
   * Assumptions: TODO
   * Parameters: None.
   * Exceptions: None.
   * Returns: HashMap object.
   */
  protected Map<String, Integer> getMoveStateCopy() {
    Map<String, Integer> deepCopy = new HashMap<>();

    for (String key : moveState.keySet()) {
      deepCopy.put(key, moveState.get(key));
    }

    return deepCopy;
  }
}
