package cellsociety.model.segregation;

import cellsociety.model.Cell;
import java.util.HashMap;

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
  private HashMap<String, Integer> moveState;
  private int nextState;
  private int myState;
  private final double myThreshold;

  /**
   * Purpose: Constructor for SegregationCell class.
   * Assumptions: config will include the key "threshold", with an integer value.
   * Parameters: HashMap config.
   * Exceptions: TODO
   * Returns: SegregationCell object.
   */
  public SegregationCell(HashMap<String, Integer> config) {
    super(config);
    moveState = new HashMap<>();
    myState = super.getState();
    nextState = -1;
    myThreshold = (double) config.get(thresholdKey) / 100;
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: TODO
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: int type. Describes what needs to be moved, if any.
   * Rules taken from https://www2.cs.duke.edu/courses/compsci308/current/assign/02_simulation/nifty/mccown-schelling-model-segregation/
   */
  public HashMap<String, Integer> prepareNextState(int[] neighborStates) {
    if (myState == EMPTY) {
      nextState = EMPTY;
      moveState.put("state", NO_MOVEMENT);
    } else {
      double similar = calculateSimilarity(neighborStates);

      if (similar >= myThreshold) {
        nextState = myState;
        moveState.put("state", NO_MOVEMENT);
      } else {
        nextState = EMPTY;
        moveState.put("state", myState);
      }
    }

    return moveState;
  }

  private double calculateSimilarity(int[] neighborStates) {
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

    return (double) sameState / nonEmpty;
  }

  /**
   * Purpose: Accepts HashMap information with new state information. Will default to return false.
   * Assumptions: Grid will not pass call this method when the 'state' field is NO_MOVEMENT (-1).
   * Parameters: HashMap object.
   * Exceptions: TODO
   * Returns: boolean type.
   */
  @Override
  public boolean receiveUpdate(HashMap<String, Integer> newInfo) {
    int incomingState = newInfo.get("state");
    if (nextState != EMPTY) {
      return false;
    } else {
      nextState = incomingState;
      return true;
    }
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
}
