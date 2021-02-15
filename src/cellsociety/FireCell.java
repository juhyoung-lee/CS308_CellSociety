package cellsociety;

import java.util.HashMap;
import java.util.Random;

/**
 * Purpose: Represents a cell for the Fire simulation. Extends the Cell class.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Jessica Yang
 */
public class FireCell extends Cell {

  public static final int TREE = 0;
  public static final int BURNING = 1;
  public static final int EMPTY = 2;
  private final Random randFire = new Random();
  private final double catchThreshold;

  /**
   * Purpose: Constructor for FireCell class.
   * Assumptions: HashMap will contain the key "prob".
   * Parameters: HashMap config.
   * Exceptions: TODO
   * Returns: FireCell object.
   */
  public FireCell(HashMap<String, Integer> config) {
    super(config);
    catchThreshold = config.get(parameterString.getString("prob"));
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: neighborStates is passed with [N, S, E, W].
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: int type. Describes what needs to be moved, if any.
   * Rules taken from https://www2.cs.duke.edu/courses/compsci308/current/assign/02_simulation/nifty/shiflet-fire/
   */
  public HashMap<String, Integer> prepareNewState(int[] neighborStates) {
    boolean burningNeighbor = false;
    double probFire = randFire.nextDouble();

    for (int state : neighborStates) {
      if (state == BURNING) {
        burningNeighbor = true;
        break;
      }
    }

    if (myState == EMPTY || myState == BURNING) {
      nextState = EMPTY;
    } else if (myState == TREE) {
      if (burningNeighbor && probFire >= catchThreshold) {
        nextState = BURNING;
      } else {
        nextState = TREE;
      }
    }

    updateStateField(NO_MOVEMENT);
    return moveState;
  }
}
