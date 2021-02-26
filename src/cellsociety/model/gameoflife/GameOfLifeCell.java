package cellsociety.model.gameoflife;

import cellsociety.model.Cell;
import java.util.Map;

/**
 * Purpose: Represents a cell for the Game of Life simulation. Extends the Cell class.
 * Assumptions: None.
 * Dependencies: Cell class and Map library.
 * Example of use: Cell gameOfLife = new GameOfLifeCell(params).
 *
 * @author Jessica Yang
 */
public class GameOfLifeCell extends Cell {

  public static final int DEAD = 0;
  public static final int ALIVE = 1;

  /**
   * Purpose: Constructor for GameOfLifeCell class.
   * Assumptions: None.
   * Parameters: Map config.
   * Exceptions: None.
   * Returns: GameOfLifeCell object.
   */
  public GameOfLifeCell(Map<String, Integer> config) {
    super(config);
    setMaxStateValue(ALIVE);
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: None.
   * Parameters: int[] neighborStates.
   * Exceptions: None.
   * Returns: Map object. Describes what needs to be moved, if any.
   * Rules taken from https://en.wikipedia.org/wiki/Conway's_Game_of_Life
   */
  public Map<String, Integer> prepareNextState(int[] neighborStates) {
    int live = calculateLive(neighborStates);

    if (getState() == ALIVE && (live == 2 || live == 3)) {
      setNextState(ALIVE);
    } else if (getState() == DEAD && live == 3) {
      setNextState(ALIVE);
    } else {
      setNextState(DEAD);
    }

    setMoveStateValue(STATE_KEY, NO_MOVEMENT);
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
