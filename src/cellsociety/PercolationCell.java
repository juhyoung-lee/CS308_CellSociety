package cellsociety;

import java.util.HashMap;

/**
 * Purpose: Represents a cell for the Percolation simulation. Extends the Cell class.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Jessica Yang
 */
public class PercolationCell extends Cell {

  public static final int OPEN = 0;
  public static final int PERCOLATED = 1;
  public static final int BLOCKED = 2;

  /**
   * Purpose: Constructor for PercolationCell class.
   * Assumptions: TODO
   * Parameters: HashMap config.
   * Exceptions: TODO
   * Returns: PercolationCell object.
   */
  public PercolationCell(HashMap<String, Integer> config) {
    super(config);
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: TODO
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: int type. Describes what needs to be moved, if any.
   * Rules taken from https://www2.cs.duke.edu/courses/compsci308/current/assign/02_simulation/PercolationCA.pdf
   */
  public HashMap<String, Integer> prepareNewState(int[] neighborStates) {
    nextState = myState;
    
    if (myState == OPEN) {
      for (int state : neighborStates) {
        if (state == PERCOLATED) {
          nextState = PERCOLATED;
          break;
        }
      }
    }

    updateStateField(NO_MOVEMENT);
    return moveState;
  }
}
