package cellsociety.model.bylsloop;

import cellsociety.model.Cell;
import java.util.Map;

/**
 * Purpose: Represents a cell for the Byl's Loop simulation. Extends the Cell class.
 * Assumptions: Requires the original states t
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Jessica Yang
 */
public class BylsLoopCell extends Cell {

  public static final int EMPTY = 0;
  public static final int DATA_PATH = 1;
  public static final int WALL = 2;
  public static final int EXTEND_SIGNAL = 3;
  public static final int LEFT_SIGNAL = 4;
  public static final int DISCONNECT_SIGNAL = 5;

  /**
   * Purpose: Constructor for BylsLoopCell class.
   * Assumptions: TODO
   * Parameters: Map config.
   * Exceptions: TODO
   * Returns: BylsLoopCell object.
   */
  public BylsLoopCell(Map<String, Integer> config) {
    super(config);
    setMaxStateValue(DISCONNECT_SIGNAL);
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: neighborStates will be passed in clockwise order.
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: Map object. There should never be any movement.
   * Rules taken from https://fab.cba.mit.edu/classes/865.18/replication/Byl.pdf
   */
  public Map<String, Integer> prepareNextState(int[] neighborStates) {
    String minRotationKey = getMinRotationKey(neighborStates);

  }

  /** Transition table requires neighbor states in clockwise, minimum value order. */
  private String getMinRotationKey(int[] neighborStates) {
    int minRotation = 6000; // number greater than largest possible rotation
    int temp;

    for (int i = 0; i < neighborStates.length; i++) {
      temp = (neighborStates[i % 4] * 1000) + (neighborStates[(1 + i) % 4] * 100)
          + (neighborStates[(2 + i) % 4] * 10) + (neighborStates[(3 + i) % 4]);
      if (temp < minRotation) {
        minRotation = temp;
      }
    }

    return Integer.toString(minRotation);
  }
}
