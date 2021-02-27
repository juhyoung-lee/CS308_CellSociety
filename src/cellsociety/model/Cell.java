package cellsociety.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Purpose: Represents a cell within the cell automata simulation.
 * Assumptions: Extended by child classes that are called by a Grid child class.
 * Dependencies: HashMap and Map libraries.
 * Example of use: Extended by child class WaTorCell.
 *
 * @author Jessica Yang, Juhyoung Lee
 */
public abstract class Cell {

  public static final int NO_MOVEMENT = -1;
  public static final String STATE_KEY = "state";
  public static final String PARAMETER_EXCEPTION_MESSAGE = "Cell parameter invalid.";
  private final Map<String, Integer> moveState = new HashMap<>();
  private int myState;
  private int nextState;
  private int maxStateValue;

  /**
   * Purpose: Constructor for Cell class.
   * Assumptions: Map config includes a STATE_KEY key.
   * Parameters: Map config.
   * Exceptions: None.
   * Returns: Cell object.
   */
  public Cell(Map<String, Integer> config) {
    myState = config.get(STATE_KEY);
  }

  /**
   * Purpose: Checks if assigned state is valid for the Cell subclass.
   * Assumptions: setMaxStateValue has already been called in the constructor of the subclass.
   * Parameters: None.
   * Exceptions: None.
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
   * Assumptions: None.
   * Parameters: int[] neighborStates.
   * Exceptions: None.
   * Returns: Map object. Describes what needs to be moved, if any.
   */
  public abstract Map<String, Integer> prepareNextState(int[] neighborStates);

  /**
   * Purpose: Update current cell state, and return value for other methods to use.
   * Assumptions: None.
   * Parameters: None.
   * Exceptions: None.
   * Returns: int object.
   */
  public void updateState() {
    myState = nextState;
    nextState = -1;
  }

  /**
   * Purpose: Accepts Map information with new state information. Will default to return false.
   * Assumptions: Grid should call this method only on Cells with movement simulations.
   * Parameters: Map object.
   * Exceptions: None.
   * Returns: boolean type.
   */
  public boolean receiveUpdate(Map<String, Integer> newInfo) {
    return false;
  }

  /**
   * Purpose: Returns default state for Cell upon grid size expansion.
   * Assumptions: None.
   * Parameters: None.
   * Exceptions: None
   * Returns: int type.
   */
  public int getBaseState() {
    return 0;
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
   * Purpose: Sets maxStateValue of the cell.
   * Assumptions: None.
   * Parameters: int type.
   * Exceptions: None.
   * Returns: None.
   */
  protected void setMaxStateValue(int maxState) {
    maxStateValue = maxState;
  }

  /**
   * Purpose: Returns nextState of the cell.
   * Assumptions: None.
   * Parameters: None.
   * Exceptions: None.
   * Returns: int state.
   */
  protected int getNextState() {
    return nextState;
  }

  /**
   * Purpose: Sets nextState of the cell.
   * Assumptions: None.
   * Parameters: int type.
   * Exceptions: None.
   * Returns: None.
   */
  protected void setNextState(int newState) {
    nextState = newState;
  }

  /**
   * Purpose: Sets value in moveState.
   * Assumptions: None.
   * Parameters: int type.
   * Exceptions: None.
   * Returns: None.
   */
  protected void setMoveStateValue(String key, int value) {
    moveState.put(key, value);
  }

  /**
   * Purpose: Returns deep copy of moveState.
   * Assumptions: None.
   * Parameters: None.
   * Exceptions: None.
   * Returns: Map object.
   */
  protected Map<String, Integer> getMoveStateCopy() {
    Map<String, Integer> deepCopy = new HashMap<>();

    for (String key : moveState.keySet()) {
      deepCopy.put(key, moveState.get(key));
    }

    return deepCopy;
  }
}
