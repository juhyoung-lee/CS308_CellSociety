package cellsociety.model.percolation;

import cellsociety.model.Cell;
import java.util.HashMap;

/**
 * Purpose: Represents a cell for the Percolation simulation. Extends the Cell class.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Jessica Yang
 */
public class PercolationCell implements Cell {

  public static final int OPEN = 1;
  public static final int PERCOLATED = 2;
  public static final int BLOCKED = 0;
  private final HashMap<String, Integer> moveState;
  private int myState;
  private int nextState;

  /**
   * Purpose: Constructor for PercolationCell class.
   * Assumptions: TODO
   * Parameters: HashMap config.
   * Exceptions: TODO
   * Returns: PercolationCell object.
   */
  public PercolationCell(HashMap<String, Integer> config) {
    moveState = new HashMap<>();
    myState = config.get("state");
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: TODO
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: int type. Describes what needs to be moved, if any.
   * Rules taken from https://www2.cs.duke.edu/courses/compsci308/current/assign/02_simulation/PercolationCA.pdf
   */
  public HashMap<String, Integer> prepareNextState(int[] neighborStates) {
    nextState = myState;
    
    if (myState == OPEN) {
      for (int state : neighborStates) {
        if (state == PERCOLATED) {
          nextState = PERCOLATED;
          break;
        }
      }
    }

    moveState.put("state", NO_MOVEMENT);
    return moveState;
  }

  /**
   * Purpose: Accepts HashMap information with new state information. Will default to return false.
   * Assumptions: Grid should call this method only on Cells with movement simulations.
   * Parameters: HashMap object.
   * Exceptions: TODO
   * Returns: boolean type.
   */
  public boolean receiveUpdate(HashMap<String, Integer> newInfo) {
    return false;
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
