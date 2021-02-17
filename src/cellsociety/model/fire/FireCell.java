package cellsociety.model.fire;

import cellsociety.model.Cell;
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
public class FireCell implements Cell {

  public static final int TREE = 0;
  public static final int BURNING = 1;
  public static final int EMPTY = 2;
  private final String probKey = "prob";
  private final Random randFire = new Random();
  private final double catchThreshold;
  private final HashMap<String, Integer> moveState;
  private int myState;
  private int nextState;

  /**
   * Purpose: Constructor for FireCell class.
   * Assumptions: HashMap will contain the key "prob" with an integer value.
   * Parameters: HashMap config.
   * Exceptions: TODO
   * Returns: FireCell object.
   */
  public FireCell(HashMap<String, Integer> config) {
    moveState = new HashMap<>();
    myState = config.get("state");
    catchThreshold = (double) config.get(probKey) / 100;
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: neighborStates is passed with [N, S, E, W].
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: int type. Describes what needs to be moved, if any.
   * Rules taken from https://www2.cs.duke.edu/courses/compsci308/current/assign/02_simulation/nifty/shiflet-fire/
   */
  public HashMap<String, Integer> prepareNextState(int[] neighborStates) {
    boolean burningNeighbor = checkBurningNeighbor(neighborStates);
    double probFire = randFire.nextDouble();

    if (myState == EMPTY || myState == BURNING) {
      nextState = EMPTY;
    } else if (myState == TREE) {
      if (burningNeighbor && probFire >= catchThreshold) {
        nextState = BURNING;
      } else {
        nextState = TREE;
      }
    }

    moveState.put("state", NO_MOVEMENT);
    return moveState;
  }

  private boolean checkBurningNeighbor(int[] neighborStates) {
    for (int state : neighborStates) {
      if (state == BURNING) {
        return true;
      }
    }
    return false;
  }

  /**
   * Purpose: Accepts HashMap information with new state information. Will default to return false.
   * Assumptions: Grid should call this method only on Cells with movement simulations.
   * Parameters: HashMap object.
   * Exceptions: TODO
   * Returns: boolean type.
   */
  public boolean receiveUpdate(HashMap<String, Integer> newInfo) {
    return false;
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

  /**
   * Purpose: Returns state of the cell.
   * Assumptions: TODO
   * Parameters: None.
   * Exceptions: None.
   * Returns: int state.
   */
  public int getState() {
    return myState;
  }
}
