package cellsociety.model.segregation;

import cellsociety.model.Cell;
import java.util.Map;

/**
 * Purpose: Represents a cell for the Segregation simulation. Extends the Cell class.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Jessica Yang
 */
public class SegregationCell extends Cell {

  public static final int AGENT_A = 1;
  public static final int AGENT_B = 2;
  public static final int EMPTY = 0;
  private final String thresholdKey = "threshold";
  private final double myThreshold;

  /**
   * Purpose: Constructor for SegregationCell class.
   * Assumptions: config will include the key "threshold", with an integer value.
   * Parameters: Map config.
   * Exceptions: TODO
   * Returns: SegregationCell object.
   */
  public SegregationCell(Map<String, Integer> config) {
    super(config);
    myThreshold = (double) config.get(thresholdKey) / 100;
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: TODO
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: Map object. Describes what needs to be moved, if any.
   * Rules taken from https://www2.cs.duke.edu/courses/compsci308/current/assign/02_simulation/nifty/mccown-schelling-model-segregation/
   */
  public Map<String, Integer> prepareNextState(int[] neighborStates) {
    if (getState() == EMPTY) {
      setNextState(EMPTY);
      setMoveStateValue("state", NO_MOVEMENT);
    } else {
      double similar = calculateSimilarity(neighborStates);

      if (similar >= myThreshold) {
        setNextState(getState());
        setMoveStateValue("state", NO_MOVEMENT);
      } else {
        setNextState(EMPTY);
        setMoveStateValue("state", getState());
      }
    }

    return getMoveStateCopy();
  }

  private double calculateSimilarity(int[] neighborStates) {
    int nonEmpty = 0;
    int sameState = 0;

    for (int state : neighborStates) {
      if (state != EMPTY) {
        nonEmpty++;
        if (state == getState()) {
          sameState++;
        }
      }
    }

    return (double) sameState / nonEmpty;
  }

  /**
   * Purpose: Accepts Map information with new state information. Will default to return false.
   * Assumptions: Grid will not pass call this method when the 'state' field is NO_MOVEMENT (-1).
   * Parameters: Map object.
   * Exceptions: TODO
   * Returns: boolean type.
   */
  @Override
  public boolean receiveUpdate(Map<String, Integer> newInfo) {
    int incomingState = newInfo.get("state");
    if (getNextState() != EMPTY) {
      return false;
    } else {
      setNextState(incomingState);
      return true;
    }
  }
}
