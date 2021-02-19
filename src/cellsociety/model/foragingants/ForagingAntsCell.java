package cellsociety.model.foragingants;

import cellsociety.model.Cell;
import java.util.Map;

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
  public static final int HOME = 1;
  public static final int FOOD = 2;
  public static final int ANT = 3;
  private int foodPheromone;
  private int homePheromone;
  private int hasFood;

  /**
   * Purpose: Constructor for ForagingAntsCell class.
   * Assumptions: TODO
   * Parameters: Map config.
   * Exceptions: TODO
   * Returns: ForagingAntsCell object.
   */
  public ForagingAntsCell(Map<String, Integer> config) {
    super(config);
    setMaxStateValue(ANT);
    foodPheromone = 0;
    homePheromone = 0;
    hasFood = 0;
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: Grid will use values in moveState to determine where to move an ant.
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: Map object.
   * Rules taken from https://greenteapress.com/complexity/html/thinkcomplexity013.html
   */
  public Map<String, Integer> prepareNextState(int[] neighborStates) {
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
    boolean containsHome = checkNeighborState(HOME, neighborStates);
    boolean containsFood = checkNeighborState(FOOD, neighborStates);

    if (hasFood == 0) { // basically reached food
      if (containsFood) {
        hasFood = 1;
      }
      homePheromone++;
      setMoveStateValue("state", ANT);
    } else {
      if (containsHome) { // basically reached home, and can disappear
        hasFood = 0;
        setMoveStateValue("state", NO_MOVEMENT);
      } else {
        setMoveStateValue("state", ANT);
      }
      foodPheromone += 2;
    }

    setNextState(EMPTY);
  }

  private boolean checkNeighborState(int checkState, int[] neighborStates) {
    for (int state : neighborStates) {
      if (state == checkState) {
        return true;
      }
    }

    return false;
  }

  private void updateMoveStateParam() {
    setMoveStateValue("originFoodPheromone", foodPheromone);
    setMoveStateValue("originHomePheromone", homePheromone);
  }

  /**
   * Purpose: Accepts Map information with new state information.
   * Assumptions: Grid will not pass call this method when the 'state' field is NO_MOVEMENT (-1).
   * Parameters: Map object.
   * Exceptions: TODO
   * Returns: None.
   */
  @Override
  public boolean receiveUpdate(Map<String, Integer> newInfo) {
    int incomingState = newInfo.get("state");

    if (incomingState == ANT && getState() == EMPTY) {
      setNextState(ANT);
      setMoveStateValue("hasFood", newInfo.get("hasFood"));
      return true;
    }

    return false;
  }

  /**
   * Getter used by ForagingAntsGrid.
   *
   * @return [food pheromone count, home pheromone count]
   */
  public int[] getPheromone() {
    return new int[]{foodPheromone, homePheromone};
  }

  /* sample grid code for movement

  if movestate.hasFood == 0 -> go towards food
   */
}