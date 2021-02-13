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
   * Assumptions: TODO
   * Parameters: int state, int breedFish, int breedShark, int energyGain, int energyLoss.
   * Exceptions: TODO
   * Returns: WaTorCell object.
   */
  public WaTorCell(int state, int breedFish, int breedShark, int energyGain, int energyLoss) {
    super(state);
    breedTimeFish = breedFish;
    breedEnergyShark = breedShark;
    this.energyGain = energyGain;
    this.energyLoss = energyLoss;
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

  /**
   * Purpose: Adjusts breedEnergy upon SHARK cell moving into a FISH cell.
   * Assumptions: Called by Grid class upon 'movement checking'.
   * Parameters: TODO
   * Exceptions: TODO
   * Returns: TODO
   */
  public void ateFish() {
    if (myState == SHARK) {
      breedEnergy += energyGain;
    }
  }


}
