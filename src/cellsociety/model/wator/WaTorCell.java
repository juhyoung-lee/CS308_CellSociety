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
public class WaTorCell extends Cell {

  public static final int FISH = 1;
  public static final int SHARK = 2;
  public static final int WATER = 0;
  private final int fishBreedThreshold;
  private final int sharkBreedThreshold;
  private final int energyGain;
  private final int energyLoss;
  private int breedFishTime;
  private int breedSharkEnergy;


  /**
   * Purpose: Constructor for WaTorCell class. Assumptions: config will include keys "breedFish",
   * "breedShark", "energyGain", and "energyLoss" Parameters: HashMap config. Exceptions: TODO
   * Returns: WaTorCell object.
   */
  public WaTorCell(HashMap<String, Integer> config) {
    super(config);
    fishBreedThreshold = config.get("fishBreedThreshold");
    sharkBreedThreshold = config.get("sharkBreedThreshold");
    energyGain = config.get("energyGain");
    energyLoss = config.get("energyLoss");
    resetMyState();
  }

  /**
   * Returns settings back to default.
   */
  private void resetMyState() {
    breedFishTime = 0;
    breedSharkEnergy = sharkBreedThreshold / 2;
  }

  /**
   * Purpose: Determine new state to update to. Assumptions: TODO Parameters: int[] neighborStates.
   * Exceptions: TODO Returns: int type. Describes what needs to be moved, if any. Rules taken from
   * https://beltoforion.de/en/wator/
   */
  public HashMap<String, Integer> prepareNewState(int[] neighborStates) {
    if (myState == FISH) {
      fishPrepareState();
    } else if (myState == SHARK) {
      sharkPrepareState();
    } else {
      nextState = WATER;
      updateStateField(NO_MOVEMENT);
    }
    return moveState;
  }

  private void fishPrepareState() {
    breedFishTime++;
    updateMoveStateParam();
    if (breedFishTime < fishBreedThreshold) {
      setToWater();
    } else {
      nextState = FISH;
      resetMyState();
    }
    updateStateField(FISH);
  }

  private void sharkPrepareState() {
    if (breedSharkEnergy <= 0) {
      setToWater();
      updateStateField(NO_MOVEMENT);
    } else {
      breedSharkEnergy -= energyLoss;
      updateMoveStateParam();
      if (breedSharkEnergy < sharkBreedThreshold) {
        setToWater();
      } else {
        nextState = SHARK;
        resetMyState();
      }
      updateStateField(SHARK);
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
    resetMyState();
  }

  /**
   * Updates moveState HashMap.
   */
  private void updateMoveStateParam() {
    moveState.put("breedTime", breedFishTime);
    moveState.put("breedEnergy", breedSharkEnergy);
  }
}
