package cellsociety.model.wator;

import cellsociety.model.Cell;
import java.util.HashMap;

/**
 * Purpose: Represents a cell for the Wa-Tor simulation. Extends the Cell class.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Jessica Yang
 */
public class WaTorCell implements Cell {

  public static final int FISH = 1;
  public static final int SHARK = 2;
  public static final int WATER = 0;
  private final String fishBreedThresholdKey = "fishBreedThreshold";
  private final String sharkBreedThresholdKey = "sharkBreedThreshold";
  private final String energyGainKey = "energyGain";
  private final String energyLossKey = "energyLoss";
  private final int fishBreedThreshold;
  private final int sharkBreedThreshold;
  private final int energyGain;
  private final int energyLoss;
  private int breedFishTime;
  private int breedSharkEnergy;
  private final HashMap<String, Integer> moveState;
  private int myState;
  private int nextState;

  /**
   * Purpose: Constructor for WaTorCell class. Assumptions: config will include keys "breedFish",
   * "breedShark", "energyGain", and "energyLoss" Parameters: HashMap config. Exceptions: TODO
   * Returns: WaTorCell object.
   */
  public WaTorCell(HashMap<String, Integer> config) {
    moveState = new HashMap<>();
    myState = config.get("state");
    fishBreedThreshold = config.get(fishBreedThresholdKey);
    sharkBreedThreshold = config.get(sharkBreedThresholdKey);
    energyGain = config.get(energyGainKey);
    energyLoss = config.get(energyLossKey);
    resetState();
  }

  /**
   * Returns settings back to default.
   */
  private void resetState() {
    breedFishTime = 0;
    breedSharkEnergy = sharkBreedThreshold / 2;
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: TODO Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: int type. Describes what needs to be moved, if any. Rules taken from
   * https://beltoforion.de/en/wator/
   */
  public HashMap<String, Integer> prepareNextState(int[] neighborStates) {
    if (myState == FISH) {
      fishPrepareNextState();
    } else if (myState == SHARK) {
      sharkPrepareNextState();
    } else {
      nextState = WATER;
      moveState.put("state", NO_MOVEMENT);
    }
    return moveState;
  }

  private void fishPrepareNextState() {
    breedFishTime++;
    updateMoveStateParam();
    if (breedFishTime < fishBreedThreshold) {
      setToWater();
    } else {
      nextState = FISH;
      resetState();
    }
    moveState.put("state", FISH);
  }

  private void sharkPrepareNextState() {
    if (breedSharkEnergy <= 0) {
      setToWater();
      moveState.put("state", NO_MOVEMENT);
    } else {
      breedSharkEnergy -= energyLoss;
      updateMoveStateParam();
      if (breedSharkEnergy < sharkBreedThreshold) {
        setToWater();
      } else {
        nextState = SHARK;
        resetState();
      }
      moveState.put("state", SHARK);
    }
  }

  /**
   * Purpose: Accepts HashMap information with new state information. Assumptions: Grid will not
   * pass call this method when the 'state' field is NO_MOVEMENT (-1). Parameters: HashMap object.
   * Exceptions: TODO Returns: None.
   */
  @Override
  public boolean receiveUpdate(HashMap<String, Integer> newInfo) {
    int incomingState = newInfo.get("state");

    if (nextState == SHARK || nextState == incomingState) { //TODO: allow fish to move into sharks?
      return false;
    }

    breedFishTime = newInfo.get("breedTime");
    breedSharkEnergy = newInfo.get("breedEnergy");

    if (nextState == FISH && incomingState == SHARK) {
      breedSharkEnergy += energyGain;
    }

    nextState = incomingState;
    return true;
  }

  /**
   * Sets cell up to be water.
   */
  private void setToWater() {
    nextState = WATER;
    resetState();
  }

  /**
   * Updates moveState HashMap.
   */
  private void updateMoveStateParam() {
    moveState.put("breedTime", breedFishTime);
    moveState.put("breedEnergy", breedSharkEnergy);
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
