package cellsociety.model.fire;

import cellsociety.model.Cell;
import java.util.Map;
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
  private final String probKey = "prob";
  private final Random randFire = new Random();
  private final double catchThreshold;

  /**
   * Purpose: Constructor for FireCell class.
   * Assumptions: Map will contain the key "prob" with an integer value.
   * Parameters: Map config.
   * Exceptions: TODO
   * Returns: FireCell object.
   */
  public FireCell(Map<String, Integer> config) {
    super(config);
    setMaxStateValue(EMPTY);
    catchThreshold = (double) config.get(probKey) / 100;
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: neighborStates is passed with [N, S, E, W].
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: Map object. Describes what needs to be moved, if any.
   * Rules taken from https://www2.cs.duke.edu/courses/compsci308/current/assign/02_simulation/nifty/shiflet-fire/
   */
  public Map<String, Integer> prepareNextState(int[] neighborStates) {
    boolean burningNeighbor = checkBurningNeighbor(neighborStates);
    double probFire = randFire.nextDouble();

    if (getState() == EMPTY || getState() == BURNING) {
      setNextState(EMPTY);
    } else if (getState() == TREE) {
      if (burningNeighbor && probFire >= catchThreshold) {
        setNextState(BURNING);
      } else {
        setNextState(TREE);
      }
    }

    setMoveStateValue(STATE_KEY, NO_MOVEMENT);
    return getMoveStateCopy();
  }

  private boolean checkBurningNeighbor(int[] neighborStates) {
    for (int state : neighborStates) {
      if (state == BURNING) {
        return true;
      }
    }
    return false;
  }
}
