package cellsociety.model.wator;

import cellsociety.model.Cell;
import java.util.Map;

/**
 * Purpose: Represents a cell for the Wa-Tor simulation. Extends the Cell class.
 * Assumptions: None.
 * Dependencies: Cell class and Map library.
 * Example of use: Cell waTor = new WaTorCell(params).
 *
 * @author Jessica Yang
 */
public class WaTorCell extends Cell {

  public static final int WATER = 0;
  public static final int FISH = 1;
  public static final int SHARK = 2;
  private final String fishBreedThresholdKey = "fishBreedThreshold";
  private final String sharkBreedThresholdKey = "sharkBreedThreshold";
  private final String energyGainKey = "energyGain";
  private final String energyLossKey = "energyLoss";
  private int fishBreedThreshold = 5;
  private int sharkBreedThreshold = 4;
  private int energyGain = 2;
  private int energyLoss = 1;
  private int breedFishTime;
  private int breedSharkEnergy;

  private final String breedTimeKey = "breedTime";
  private final String breedEnergyKey = "breedEnergy";

  /**
   * Purpose: Constructor for WaTorCell class.
   * Assumptions: config will include keys "breedFish", "breedShark", "energyGain", and "energyLoss"
   * Parameters: Map config.
   * Exceptions: Throws exceptions from checkParameters.
   * Returns: WaTorCell object.
   */
  public WaTorCell(Map<String, Integer> config) throws Exception {
    super(config);
    setMaxStateValue(SHARK);
    checkParameters(config);
    resetState();
  }

  private void checkParameters(Map<String, Integer> config) throws Exception {
    try {
      fishBreedThreshold = config.get(fishBreedThresholdKey);
      sharkBreedThreshold = config.get(sharkBreedThresholdKey);
      energyGain = config.get(energyGainKey);
      energyLoss = config.get(energyLossKey);
    } catch (Exception e) {
      throw new Exception(PARAMETER_EXCEPTION_MESSAGE);
    }
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
   * Assumptions: None.
   * Parameters: int[] neighborStates.
   * Exceptions: None.
   * Returns: Map object. Describes what needs to be moved, if any.
   * Rules taken from https://beltoforion.de/en/wator/
   */
  public Map<String, Integer> prepareNextState(int[] neighborStates) {
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
   * Purpose: Accepts Map information with new state information.
   * Assumptions: Grid will not pass call this method when the 'state' field is NO_MOVEMENT (-1).
   * Parameters: Map object.
   * Exceptions: None.
   * Returns: None.
   */
  @Override
  public boolean receiveUpdate(Map<String, Integer> newInfo) {
    int incomingState = newInfo.get("state");

    if (getNextState() == incomingState) {
      return false;
    }

    breedFishTime = newInfo.get(breedTimeKey);
    breedSharkEnergy = newInfo.get(breedEnergyKey);

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
   * Updates moveState Map.
   */
  private void updateMoveStateParam() {
    setMoveStateValue(breedTimeKey, breedFishTime);
    setMoveStateValue(breedEnergyKey, breedSharkEnergy);
  }
}
