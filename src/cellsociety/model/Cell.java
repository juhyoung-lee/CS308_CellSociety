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
  protected HashMap<String, Integer> moveState = new HashMap<>();
  protected int myState;
  protected int nextState;

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
   * Purpose: Updates state field in moveState.
   * Assumptions: TODO
   * Parameters: int state.
   * Exceptions: TODO
   * Returns: None.
   */
  protected void updateMoveStateField(int state) {
    moveState.put("state", state);
  }

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
   * Assumptions: Grid should never call this method.
   * Parameters: HashMap object.
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
}
