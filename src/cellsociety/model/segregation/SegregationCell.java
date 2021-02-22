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

  public static final int EMPTY = 0;
  public static final int AGENT_A = 1;
  public static final int AGENT_B = 2;
  private final String thresholdKey = "threshold";
  private double myThreshold = 0.5;

  /**
   * Purpose: Constructor for SegregationCell class.
   * Assumptions: config will include the key "threshold", with an integer value.
   * Parameters: Map config.
   * Exceptions: TODO
   * Returns: SegregationCell object.
   */
  public SegregationCell(Map<String, Integer> config) throws Exception {
    super(config);
    setMaxStateValue(AGENT_B);
    checkParameters(config);
  }

  private void checkParameters(Map<String, Integer> config) throws Exception {
    try {
      myThreshold = (double) config.get(thresholdKey) / 100;
    } catch (Exception e) {
      throw new Exception(PARAMETER_EXCEPTION_MESSAGE);
    }
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
      setMoveStateValue(STATE_KEY, NO_MOVEMENT);
    } else {
      double similar = calculateSimilarity(neighborStates);

      if (similar >= myThreshold) {
        setNextState(getState());
        setMoveStateValue(STATE_KEY, NO_MOVEMENT);
      } else {
        setNextState(EMPTY);
        setMoveStateValue(STATE_KEY, getState());
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
    int incomingState = newInfo.get(STATE_KEY);
    if (getNextState() != EMPTY) {
      return false;
    } else {
      setNextState(incomingState);
      return true;
    }
  }
}
