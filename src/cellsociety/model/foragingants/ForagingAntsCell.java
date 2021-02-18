package cellsociety.model.foragingants;

import cellsociety.model.Cell;
import java.util.HashMap;

/**
 * Purpose: Represents a cell for the Foraging Ants simulation. Extends the Cell class.
 * Assumptions: The nest is set at the top left corner of the grid, while food is at the bottom
 *    right corner.
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Jessica Yang
 */
public class ForagingAntsCell extends Cell {
  public static final int EMPTY = 0;
  public static final int FOOD = 1;
  public static final int HOME = 2;
  public static final int ANT = 3;
  private int foodPheromone;
  private int homePheromone;
  private int hasFood;

  /**
   * Purpose: Constructor for ForagingAntsCell class.
   * Assumptions: TODO
   * Parameters: HashMap config.
   * Exceptions: TODO
   * Returns: ForagingAntsCell object.
   */
  public ForagingAntsCell(HashMap<String, Integer> config) {
    super(config);
    foodPheromone = 0;
    homePheromone = 0;
    hasFood = 0;
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: Grid will use values in moveState to determine where to move an ant.
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: HashMap object.
   * Rules taken from https://greenteapress.com/complexity/html/thinkcomplexity013.html
   */
  public HashMap<String, Integer> prepareNextState(int[] neighborStates) {
    if (getState() == HOME) {
      setNextState(HOME);
      setMoveStateValue("state", ANT);
      setMoveStateValue("hasFood", hasFood);
    } else if (getState() == ANT) {
      antPrepareNextState(neighborStates);
    } else {
      setNextState(getState());
      setMoveStateValue("state", NO_MOVEMENT);
    }

    updateMoveStateParam();
    return getMoveStateCopy();
  }

  private void antPrepareNextState(int[] neighborStates) {
    boolean containsHome = false;
    boolean containsFood = false;

    for (int state : neighborStates) {
      if (state == HOME) {
        containsHome = true;
      }

      if (state == FOOD) {
        containsFood = true;
      }
    }

    if (hasFood == 0) { //basically reached food
      if (containsFood) {
        hasFood = 1;
      }
      homePheromone++;
    } else {
      if (containsHome) {//basically reached home
        hasFood = 0;
      }
      foodPheromone += 2;
    }
    setNextState(EMPTY);
    setMoveStateValue("state", ANT);
  }

  private void updateMoveStateParam() {
    setMoveStateValue("originFoodPheromone", foodPheromone);
    setMoveStateValue("originHomePheromone", homePheromone);
  }

  /**
   * Purpose: Accepts HashMap information with new state information.
   * Assumptions: Grid will not pass call this method when the 'state' field is NO_MOVEMENT (-1).
   * Parameters: HashMap object.
   * Exceptions: TODO
   * Returns: None.
   */
  @Override
  public boolean receiveUpdate(HashMap<String, Integer> newInfo) {
    int incomingState = newInfo.get("state");

    if (incomingState == ANT && getState() == EMPTY) {
      setNextState(ANT);
      setMoveStateValue("hasFood", newInfo.get("hasFood"));
      return true;
    }

    return false;
  }
}