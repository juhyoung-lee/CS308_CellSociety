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
  private final int loseThreshold;
  private final int bufferBound;

  /**
   * Purpose: Constructor for RPSCell class.
   * Assumptions: TODO
   * Parameters: Map config.
   * Exceptions: TODO
   * Returns: RPSCell object.
   */
  public RPSCell(Map<String, Integer> config) {
    super(config);
    setMaxStateValue(SCISSORS);
    loseThreshold = config.get(loseThresholdKey);
    bufferBound = config.get(bufferBoundKey);
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

    setMoveStateValue("state", NO_MOVEMENT);
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

    return opponentCount + buffer > loseThreshold;
  }
}
