package cellsociety.model.percolation;

import cellsociety.model.Cell;
import java.util.Map;

/**
 * Purpose: Represents a cell for the Percolation simulation. Extends the Cell class.
 * Assumptions: None.
 * Dependencies: Cell class and Map library.
 * Example of use: Cell percolation = new PercolationCell(params).
 *
 * @author Jessica Yang
 */
public class PercolationCell extends Cell {

  public static final int BLOCKED = 0;
  public static final int OPEN = 1;
  public static final int PERCOLATED = 2;

  /**
   * Purpose: Constructor for PercolationCell class.
   * Assumptions: None.
   * Parameters: Map config.
   * Exceptions: None.
   * Returns: PercolationCell object.
   */
  public PercolationCell(Map<String, Integer> config) {
    super(config);
    setMaxStateValue(PERCOLATED);
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: None.
   * Parameters: int[] neighborStates.
   * Exceptions: None.
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

    setMoveStateValue(STATE_KEY, NO_MOVEMENT);
    return getMoveStateCopy();
  }
}
