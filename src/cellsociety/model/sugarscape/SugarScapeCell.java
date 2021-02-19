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

  private int patchMaximumSugar;
  private int patchSugar;
  private String patchSugarStateKey = "patchSugarState";

  public static final int EMPTY = 0;
  public static final int AGENT = 1;
  private String agentSugarMetabolismMaxKey = "agentSugarMetabolismMax";
  private String agentVisionMaxKey = "agentVisionMax";
  private String agentInitialSugarKey = "agentInitialSugar";
  private Random randGen = new Random();
  private final int agentSugarMetabolism;
  private final int agentVision;
  private int agentSugar;

  /**
   * Purpose: Constructor for SugarScapeCell class.
   * Assumptions: TODO
   * Parameters: Map config.
   * Exceptions: TODO
   * Returns: SugarScapeCell object.
   */
  public SugarScapeCell(Map<String, Integer> config) {
    super(config);
    patchMaximumSugar = config.get(patchSugarStateKey);
    patchSugar = patchMaximumSugar;
    agentSugarMetabolism = randGen.nextInt(config.get(agentSugarMetabolismMaxKey));
    agentVision = randGen.nextInt(config.get(agentVisionMaxKey));
    agentSugar = config.get(agentInitialSugarKey);
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: TODO
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: Map object. There should never be any movement.
   * Rules taken from https://fab.cba.mit.edu/classes/865.18/replication/Byl.pdf
   */
  public Map<String, Integer> prepareNextState(int[] neighborStates) {
  }
}
