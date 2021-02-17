package cellsociety.model;

import java.util.HashMap;

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
  private HashMap<String, Integer> moveState = new HashMap<>();
  private int myState;
  private int nextState;

  /**
   * Purpose: Constructor for Cell class.
   * Assumptions: TODO
   * Parameters: HashMap config.
   * Exceptions: TODO
   * Returns: Cell object.
   */
  public Cell(HashMap<String, Integer> config) {
    myState = config.get("state");
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: TODO
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: int type. Describes what needs to be moved, if any.
   */
  public abstract HashMap<String, Integer> prepareNextState(int[] neighborStates);

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
  public boolean receiveUpdate(HashMap<String, Integer> newInfo) {
    return false;
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
   * Purpose: Sets state of the cell.
   * Assumptions: TODO
   * Parameters: int type.
   * Exceptions: None.
   * Returns: None.
   */
  protected void setState(int newState) {
    myState = newState;
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
   * Purpose: Returns value from moveState.
   * Assumptions: TODO
   * Parameters: String key.
   * Exceptions: None.
   * Returns: int value.
   */
  protected int getMoveStateValue(String key) {
    return moveState.get(key);
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
  protected HashMap<String, Integer> getMoveStateCopy() {
    HashMap<String, Integer> deepCopy = new HashMap<>();

    for (String key : moveState.keySet()) {
      deepCopy.put(key, moveState.get(key));
    }

    return deepCopy;
  }
}
