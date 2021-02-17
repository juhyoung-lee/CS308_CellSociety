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
public interface Cell {

  int NO_MOVEMENT = -1;

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: TODO
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: int type. Describes what needs to be moved, if any.
   */
  HashMap<String, Integer> prepareNextState(int[] neighborStates);

  /**
   * Purpose: Update current cell state, and return value for other methods to use.
   * Assumptions: TODO
   * Parameters: None.
   * Exceptions: None.
   * Returns: int object.
   */
  int updateState();

  /**
   * Purpose: Accepts HashMap information with new state information. Will default to return false.
   * Assumptions: Grid should call this method only on Cells with movement simulations.
   * Parameters: HashMap object.
   * Exceptions: TODO
   * Returns: boolean type.
   */
  boolean receiveUpdate(HashMap<String, Integer> newInfo);

  /**
   * Purpose: Returns state of the cell.
   * Assumptions: TODO
   * Parameters: None.
   * Exceptions: None.
   * Returns: int state.
   */
  int getState();
}
