package cellsociety.model.rps;

import cellsociety.model.Cell;
import java.util.Map;
import java.util.Random;

/**
 * Purpose: Represents a cell for the Rock Paper Scissors simulation. Extends the Cell class.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Jessica Yang
 */
public class RPSCell extends Cell {

  public static final int ROCK = 0;
  public static final int PAPER = 1;
  public static final int SCISSORS = 2;
  private final String loseThresholdKey = "loseThreshold";
  private final String bufferBoundKey = "bufferBound";
  private final Random randBuff = new Random();
  private int loseThreshold = 3;
  private int bufferBound = 1;

  /**
   * Purpose: Constructor for RPSCell class.
   * Assumptions: TODO
   * Parameters: Map config.
   * Exceptions: TODO
   * Returns: RPSCell object.
   */
  public RPSCell(Map<String, Integer> config) throws Exception {
    super(config);
    setMaxStateValue(SCISSORS);
    checkParameters(config);
  }

  private void checkParameters(Map<String, Integer> config) throws Exception {
    try {
      loseThreshold = config.get(loseThresholdKey);
      bufferBound = config.get(bufferBoundKey);
    } catch (Exception e) {
      throw new Exception(PARAMETER_EXCEPTION_MESSAGE);
    }
  }

  /**
   * Purpose: Returns default state for Cell upon grid size expansion.
   * Assumptions: None.
   * Parameters: None.
   * HashMap object.
   * Exceptions: TODO
   * Returns: int type.
   */
  @Override
  public int getBaseState() {
    Random randState = new Random();
    return randState.nextInt(SCISSORS + 1);
  }

  /**
   * Purpose: Determine new state to update to. nextState should be set to the winner's state.
   * Assumptions: TODO
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: Map object. There should never be any movement.
   * Rules taken from https://softologyblog.wordpress.com/2018/03/23/rock-paper-scissors-cellular-automata/
   */
  public Map<String, Integer> prepareNextState(int[] neighborStates) {
    if (getState() == ROCK && checkLose(PAPER, neighborStates)) {
      setNextState(PAPER);
    } else if (getState() == PAPER && checkLose(SCISSORS, neighborStates)) {
      setNextState(SCISSORS);
    } else if (getState() == SCISSORS && checkLose(ROCK, neighborStates)) {
      setNextState(ROCK);
    } else {
      setNextState(getState());
    }

    setMoveStateValue(STATE_KEY, NO_MOVEMENT);
    return getMoveStateCopy();
  }

  private boolean checkLose(int opponentState, int[] neighborStates) {
    int opponentCount = 0;
    int buffer = randBuff.nextInt(bufferBound);

    for (int state : neighborStates) {
      if (state == opponentState) {
        opponentCount++;
      }
    }

    return opponentCount + buffer >= loseThreshold;
  }
}
