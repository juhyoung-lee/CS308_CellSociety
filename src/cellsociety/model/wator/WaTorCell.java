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

  /**
   * Purpose: Constructor for WaTorCell class. Assumptions: config will include keys "breedFish",
   * "breedShark", "energyGain", and "energyLoss" Parameters: HashMap config. Exceptions: TODO
   * Returns: WaTorCell object.
   */
  public WaTorCell(HashMap<String, Integer> config) {
    super(config);
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
   * Assumptions: TODO
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: HashMap object. Describes what needs to be moved, if any.
   * https://beltoforion.de/en/wator/
   */
  public HashMap<String, Integer> prepareNextState(int[] neighborStates) {
    if (getState() == FISH) {
      fishPrepareNextState();
    } else if (getState() == SHARK) {
      sharkPrepareNextState();
    } else {
      setNextState(WATER);
      setMoveStateValue("state", NO_MOVEMENT);
    }
    return getMoveStateCopy();
  }

  private void fishPrepareNextState() {
    breedFishTime++;
    updateMoveStateParam();
    if (breedFishTime < fishBreedThreshold) {
      setToWater();
    } else {
      setNextState(FISH);
      resetState();
    }
    setMoveStateValue("state", FISH);
  }

  private void sharkPrepareNextState() {
    if (breedSharkEnergy <= 0) {
      setToWater();
      setMoveStateValue("state", NO_MOVEMENT);
    } else {
      breedSharkEnergy -= energyLoss;
      updateMoveStateParam();
      if (breedSharkEnergy < sharkBreedThreshold) {
        setToWater();
      } else {
        setNextState(SHARK);
        resetState();
      }
      setMoveStateValue("state", SHARK);
    }
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

    if (getNextState() == incomingState) {
      return false;
    }

    breedFishTime = newInfo.get("breedTime");
    breedSharkEnergy = newInfo.get("breedEnergy");

    if (getNextState() == FISH && incomingState == SHARK
        || getNextState() == SHARK && incomingState == FISH) {
      breedSharkEnergy += energyGain;
    }

    setNextState(incomingState);
    return true;
  }

  /**
   * Sets cell up to be water.
   */
  private void setToWater() {
    setNextState(WATER);
    resetState();
  }

  /**
   * Updates moveState HashMap.
   */
  private void updateMoveStateParam() {
    setMoveStateValue("breedTime", breedFishTime);
    setMoveStateValue("breedEnergy", breedSharkEnergy);
  }
}
