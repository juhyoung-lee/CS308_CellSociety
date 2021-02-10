package cellsociety;

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
   * Purpose: Construct for GameOfLifeCell class.
   * Assumptions: TODO
   * Parameters: int index, int state.
   * Exceptions: TODO
   * Returns: GameOfLifeCell object.
   */
  public GameOfLifeCell(int state) {
    super(state);
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: TODO
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: TODO
   * Rules taken from https://en.wikipedia.org/wiki/Conway's_Game_of_Life
   */
  public void prepareNewState(int[] neighborStates) {
    int live = 0;

    for (int state : neighborStates) {
      if (state == ALIVE) {
        live++;
      }
    }

    if (myState == ALIVE && (live == 2 || live == 3)) {
      nextState = ALIVE;
    } else if (myState == DEAD && live == 3) {
      nextState = ALIVE;
    } else {
      nextState = DEAD;
    }
  }

  /**
   * Purpose: Update current cell state, and return value for other methods to use.
   * Assumptions: TODO
   * Parameters: None.
   * Exceptions: None.
   * Returns: int object.
   */
  public int updateState() {
    myState = nextState;
    return myState;
  }
}
