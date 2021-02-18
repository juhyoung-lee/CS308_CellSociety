package cellsociety.model.gameoflife;

import cellsociety.model.Cell;
import java.util.HashMap;

/**
 * Purpose: Represents a cell for the Game of Life simulation. Extends the Cell class.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Jessica Yang
 */
public class GameOfLifeCell extends Cell {

  public static final int ALIVE = 1;
  public static final int DEAD = 0;

  /**
   * Purpose: Constructor for GameOfLifeCell class.
   * Assumptions: TODO
   * Parameters: HashMap config.
   * Exceptions: TODO
   * Returns: GameOfLifeCell object.
   */
  public GameOfLifeCell(HashMap<String, Integer> config) {
    super(config);
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: TODO
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: HashMap object. Describes what needs to be moved, if any.
   * Rules taken from https://en.wikipedia.org/wiki/Conway's_Game_of_Life
   */
  public HashMap<String, Integer> prepareNextState(int[] neighborStates) {
    int live = calculateLive(neighborStates);

    if (getState() == ALIVE && (live == 2 || live == 3)) {
      setNextState(ALIVE);
    } else if (getState() == DEAD && live == 3) {
      setNextState(ALIVE);
    } else {
      setNextState(DEAD);
    }

    setMoveStateValue("state", NO_MOVEMENT);
    return getMoveStateCopy();
  }

  /** Calculates number live cells in neighbors. */
  private int calculateLive(int[] neighborStates) {
    int live = 0;

    for (int state : neighborStates) {
      if (state == ALIVE) {
        live++;
      }
    }

    return live;
  }
}
