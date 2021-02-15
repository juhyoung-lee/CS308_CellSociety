package cellsociety;

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

  public static final int FISH = 0;
  public static final int SHARK = 1;
  public static final int WATER = 2;
  private final int breedTimeFish;
  private final int breedEnergyShark;
  private final int energyGain;
  private final int energyLoss;
  private int breedTime;
  private int breedEnergy;


  /**
   * Purpose: Constructor for WaTorCell class.
   * Assumptions: config will include keys "breedFish", "breedShark", "energyGain", and "energyLoss"
   * Parameters: HashMap config.
   * Exceptions: TODO
   * Returns: WaTorCell object.
   */
  public WaTorCell(HashMap<String, Integer> config) {
    super(config);
    breedTimeFish = config.get(parameterString.getString("breedFish"));
    breedEnergyShark = config.get(parameterString.getString("breedShark"));
    energyGain = config.get(parameterString.getString("energyGain"));
    energyLoss = config.get(parameterString.getString("energyLoss"));
    reset();
  }

  /** Returns settings back to default. */
  private void reset() {
    breedTime = 0;
    breedEnergy = breedEnergyShark;
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: TODO
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: int type. Describes what needs to be moved, if any.
   * Rules taken from https://beltoforion.de/en/wator/
   */
  public HashMap<String, Integer> prepareNewState(int[] neighborStates) {
    if (myState == FISH) {
      fishPrepareState();
    } else if (myState == SHARK) {
      sharkPrepareState();
    } else {
      updateStateField(NO_MOVEMENT);
    }
    return moveState;
  }

  private void fishPrepareState() {
    if (breedTime < breedTimeFish) {
      setToWater();
    } else {
      nextState = FISH;
      breedTime++;
    }
    updateNewStateParam();
    updateStateField(FISH);
  }

  private void sharkPrepareState() {
    if (breedEnergy <= 0) {
      setToWater();
      updateStateField(NO_MOVEMENT);
    } else {
      if (breedEnergy < breedEnergyShark) {
        setToWater();
      } else {
        nextState = SHARK;
        breedEnergy -= energyLoss;
      }
      updateStateField(SHARK);
    }
    updateNewStateParam();
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
    boolean received;

    if (nextState == SHARK) {
      return false;
    } else if (nextState == FISH) {
      received = fishReceiveUpdate(incomingState);
    } else {
      received = true;
    }

    nextState = incomingState;
    breedTime = newInfo.get("breedTime");
    breedEnergy = newInfo.get("breedEnergy");
    return received;
  }

  private boolean fishReceiveUpdate(int newState) {
    if (newState == SHARK) {
      ateFish();
      return true;
    }
    return false;
  }

  /** Sets cell up to be water. */
  private void setToWater() {
    nextState = WATER;
    reset();
  }

  /** Updates moveState HashMap. */
  private void updateNewStateParam() {
    moveState.put("breedTime", breedTime);
    moveState.put("breedEnergy", breedEnergy);
  }

  /** Adjusts breedEnergy upon SHARK cell moving into a FISH cell. */
  private void ateFish() {
    if (myState == SHARK) {
      breedEnergy += energyGain;
    }
  }
}
