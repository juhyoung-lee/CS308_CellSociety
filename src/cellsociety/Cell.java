package cellsociety;

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
   * Parameters: int state.
   * Exceptions: TODO
   * Returns: Cell object.
   */
  public Cell(int state) {
    myState = state;
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: TODO
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: int type. Describes what needs to be moved, if any.
   */
  public abstract HashMap<String, Integer> prepareNewState(int[] neighborStates);

  /**
   * Purpose: Updates state field in moveState.
   * Assumptions: TODO
   * Parameters: int state.
   * Exceptions: TODO
   * Returns: None.
   */
  protected void updateStateField(int state) {
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
    return myState;
  }

  /**
   * Purpose: Accepts HashMap information with new state information.
   * Assumptions: TODO
   * Parameters: HashMap object.
   * Exceptions: TODO
   * Returns: None.
   */
  public abstract void recieveUpdate(HashMap<String, Integer> newInfo);

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
