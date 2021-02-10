package cellsociety;

/**
 * Purpose: Represents a cell for the Segregation simulation. Extends the Cell class.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Jessica Yang
 */
public class SegregationCell extends Cell {

  public static final int AGENT_A = 0;
  public static final int AGENT_B = 1;
  public static final int EMPTY = 2;
  private final double myThreshold;

  /**
   * Purpose: Constructor for SegregationCell class.
   * Assumptions: TODO
   * Parameters: int state, double threshold.
   * Exceptions: TODO
   * Returns: SegregationCell object.
   */
  public SegregationCell(int state, double threshold) {
    super(state);
    myThreshold = threshold;
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: TODO
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: int type. Describes what needs to be moved, if any.
   * Rules taken from https://www2.cs.duke.edu/courses/compsci308/current/assign/02_simulation/nifty/mccown-schelling-model-segregation/
   */
  public int prepareNewState(int[] neighborStates) {
    int nonEmpty = 0;
    int sameState = 0;

    for (int state : neighborStates) {
      if (state != EMPTY) {
        nonEmpty++;
        if (state == myState) {
          sameState++;
        }
      }
    }

    double similar = (double) sameState / nonEmpty;

    if (similar >= myThreshold) {
      nextState = myState;
      return NO_MOVEMENT;
    } else {
      nextState = EMPTY;
      return myState;
    }
  }
}
