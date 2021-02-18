package cellsociety.model.percolation;

import cellsociety.model.Cell;
import java.util.Map;

/**
 * Purpose: Represents a cell for the Percolation simulation. Extends the Cell class.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Jessica Yang
 */
public class PercolationCell extends Cell {

  public static final int OPEN = 1;
  public static final int PERCOLATED = 2;
  public static final int BLOCKED = 0;

  /**
   * Purpose: Constructor for PercolationCell class.
   * Assumptions: TODO
   * Parameters: Map config.
   * Exceptions: TODO
   * Returns: PercolationCell object.
   */
  public PercolationCell(Map<String, Integer> config) {
    super(config);
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: TODO
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: Map object. Describes what needs to be moved, if any.
   * Rules taken from https://www2.cs.duke.edu/courses/compsci308/current/assign/02_simulation/PercolationCA.pdf
   */
  public Map<String, Integer> prepareNextState(int[] neighborStates) {
    setNextState(getState());

    if (getState() == OPEN) {
      for (int state : neighborStates) {
        if (state == PERCOLATED) {
          setNextState(PERCOLATED);
          break;
        }
      }
    }

    setMoveStateValue("state", NO_MOVEMENT);
    return getMoveStateCopy();
  }
}
