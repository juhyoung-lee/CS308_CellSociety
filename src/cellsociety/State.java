package cellsociety;

import java.util.HashMap;

/**
 * Purpose: Represents the state of a cell within a cell automata simulation.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Jessica Yang
 */
public class State {

  private int myState;
  private HashMap<String, Integer> myInfo;

  /**
   * Purpose: Constructor for State class.
   * Assumptions: TODO
   * Parameters: int state.
   * Exceptions: TODO
   * Returns: State object.
   */
  public State(int currentState, HashMap<String, Integer> stateInfo) {
    myState = currentState;
    myInfo = stateInfo;
  }

  /**
   * Purpose: Returns state value.
   * Assumptions: TODO
   * Parameters: None.
   * Exceptions: TODO
   * Returns: int myState.
   */
  public int getState() {
    return myState;
  }

  /**
   * Purpose: Returns myInfo.
   * Assumptions: TODO
   * Parameters: None.
   * Exceptions: TODO
   * Returns: HashMap<String, Integer> myInfo.
   */
  public HashMap<String, Integer> getInfo() {
    return myInfo;
  }
}
