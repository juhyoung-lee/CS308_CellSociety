package cellsociety.model.foragingants;

import cellsociety.model.Cell;
import java.util.Map;

/**
 * Purpose: Represents a cell for the Foraging Ants simulation. Extends the Cell class.
 * Assumptions: The nest is set at the top left corner of the grid, while food is at the bottom
 *    right corner.
 * Dependencies: Cell class and Map library.
 * Example of use: Cell foragingAnts = new ForagingAntsCell(params).
 *
 * @author Jessica Yang
 */
public class ForagingAntsCell extends Cell {

  public static final int EMPTY = 0;
  public static final int HOME = 1;
  public static final int FOOD = 2;
  public static final int ANT = 3;
  public static final String HAS_FOOD = "hasFood";
  private int foodPheromone;
  private int homePheromone;
  private int hasFood;

  /**
   * Purpose: Constructor for ForagingAntsCell class.
   * Assumptions: None.
   * Parameters: Map config.
   * Exceptions: None.
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
   * Exceptions: None.
   * Returns: Map object.
   * Rules taken from https://greenteapress.com/complexity/html/thinkcomplexity013.html
   */
  public Map<String, Integer> prepareNextState(int[] neighborStates) {
    if (getState() == HOME) {
      setNextState(HOME);
      setMoveStateValue(STATE_KEY, ANT);
      setMoveStateValue(HAS_FOOD, hasFood);
    } else if (getState() == ANT) {
      antPrepareNextState(neighborStates);
    } else {
      setNextState(getState());
      setMoveStateValue(STATE_KEY, NO_MOVEMENT);
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
      setMoveStateValue(STATE_KEY, ANT);
    } else {
      if (containsHome) { // basically reached home, and can disappear
        hasFood = 0;
        setMoveStateValue(STATE_KEY, NO_MOVEMENT);
      } else {
        setMoveStateValue(STATE_KEY, ANT);
      }
      foodPheromone += 2;
    }

    setMoveStateValue(HAS_FOOD,hasFood);
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
   * Exceptions: None.
   * Returns: None.
   */
  @Override
  public boolean receiveUpdate(Map<String, Integer> newInfo) {
    int incomingState = newInfo.get(STATE_KEY);

    if (incomingState == ANT && getState() == EMPTY) {
      setNextState(ANT);
      setMoveStateValue(HAS_FOOD, newInfo.get(HAS_FOOD));
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
}