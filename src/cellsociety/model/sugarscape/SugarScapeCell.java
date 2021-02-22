package cellsociety.model.sugarscape;

import cellsociety.model.Cell;
import java.util.Map;
import java.util.Random;

/**
 * Purpose: Represents a cell for the SugarScape simulation. Extends the Cell class.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Jessica Yang
 */
public class SugarScapeCell extends Cell {

  public static final int EMPTY = 0;
  public static final int AGENT = 1;
  private final String neighborNumMaxKey = "neighborNumMax";
  private final String agentVisionMaxKey = "agentVisionMax";
  private final String agentSugarMetabolismMaxKey = "agentSugarMetabolismMax";
  private final String agentInitialSugarKey = "agentInitialSugar";
  private final Random randGen = new Random();
  private int neighborNumMax = 8;
  private int agentVisionMax = 8;
  private int agentSugarMetabolismMax = 3;
  private int agentSugarMetabolism;
  private int agentVision;
  private int agentSugar;

  private final String patchMaxSugarKey = "patchMaxSugar";
  private final String patchSugarGrowBackRateKey = "patchSugarGrowBackRate";
  private final String patchSugarGrowBackIntervalKey = "getPatchSugarGrowBackInterval";
  private int patchMaxSugar = 5;
  private int patchSugarGrowBackRate = 1;
  private int patchSugarGrowBackInterval = 1;
  private int patchSugar;
  private int patchIntervalCount;

  private final String agentSugarMetabolismKey = "agentSugarMetabolism";
  private final String agentVisionKey = "agentVision";
  private final String agentSugarKey = "agentSugar";

  /**
   * Purpose: Constructor for SugarScapeCell class.
   * Assumptions: TODO
   * Parameters: Map config.
   * Exceptions: TODO
   * Returns: SugarScapeCell object.
   */
  public SugarScapeCell(Map<String, Integer> config) throws Exception {
    super(config);
    setMaxStateValue(AGENT);
    checkParameters(config);
    agentVision = randGen.nextInt(agentVisionMax) + 1;
    agentSugarMetabolism = randGen.nextInt(agentSugarMetabolismMax) + 1;
    patchSugar = patchMaxSugar;
    patchIntervalCount = 0;
  }

  private void checkParameters(Map<String, Integer> config) throws Exception {
    try {
      neighborNumMax = config.get(neighborNumMaxKey);
      agentVisionMax = config.get(agentVisionMaxKey);
      agentSugar = config.get(agentInitialSugarKey) + 5;
      agentSugarMetabolismMax = config.get(agentSugarMetabolismMaxKey);
      patchMaxSugar = config.get(patchMaxSugarKey);
      patchSugarGrowBackRate = config.get(patchSugarGrowBackRateKey);
      patchSugarGrowBackInterval = config.get(patchSugarGrowBackIntervalKey);
    } catch (Exception e) {
      throw new Exception(PARAMETER_EXCEPTION_MESSAGE);
    }
  }

  /**
   * Purpose: Checks if assigned state is valid for the Cell subclass. Overridden for specific
   *    SugarScapeCell check.
   * Assumptions: setMaxStateValue has already been called in the constructor of the subclass.
   * Parameters: None.
   * Exceptions: TODO
   * Returns: boolean type.
   */
  @Override
  public boolean isValidState() {
    if (agentVisionMax > neighborNumMax) {
      return false;
    } else {
      return super.isValidState();
    }
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: Passed max number of neighbors from Grid.
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: Map object. There should never be any movement.
   * Rules taken from https://www2.cs.duke.edu/courses/compsci308/current/assign/02_simulation/Sugarscape_Leicester.pdf
   */
  public Map<String, Integer> prepareNextState(int[] neighborStates) {
    updatePatchSugar();

    if (getState() == AGENT) {
      setMoveStateValue("state", AGENT);
      updateMoveStateParam();
    } else {
      setMoveStateValue("state", NO_MOVEMENT);
    }
    setNextState(EMPTY);

    return getMoveStateCopy();
  }

  private void updatePatchSugar() {
    patchIntervalCount++;
    if (patchIntervalCount == patchSugarGrowBackInterval) {
      patchSugar = Math.min(patchMaxSugar, patchSugar + patchSugarGrowBackRate);
      patchIntervalCount = 0;
    }
  }

  private void updateMoveStateParam() {
    setMoveStateValue(agentSugarKey, agentSugar);
    setMoveStateValue(agentSugarMetabolismKey, agentSugarMetabolism);
    setMoveStateValue(agentVisionKey, agentVision);
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
    int newAgentSugar = calcNewAgentSugar(newInfo);

    if (getNextState() == incomingState || newAgentSugar <= 0) {
      return false;
    } else {
      agentSugar = newAgentSugar;
      patchSugar = 0;
      agentVision = newInfo.get(agentVisionKey);
      setNextState(incomingState);
      return true;
    }
  }

  private int calcNewAgentSugar(Map<String, Integer> newInfo) {
    int newAgentSugar = newInfo.get(agentSugarKey);
    int newAgentSugarMetabolism = newInfo.get(agentSugarMetabolismKey);

    newAgentSugar += (patchSugar - newAgentSugarMetabolism);

    return newAgentSugar;
  }

  /**
   * Purpose: Gets patchSugar value for Grid to select the Cell to move to.
   * Assumptions: TODO
   * Parameters: None.
   * Exceptions: None.
   * Returns: int patchSugar.
   */
  public int getPatchSugar() {
    return patchSugar;
  }
}
