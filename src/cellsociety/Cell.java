package cellsociety;

/**
 * Purpose: Represents a cell within the cell automata simulation.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Juhyoung Lee, Jessica Yang
 */
public abstract class Cell {

  protected int myState;
  protected int nextState;

  /**
   * Purpose: Constructor for Cell class.
   * Assumptions: TODO
   * Parameters: int state.
   * Exceptions: TODO
   * Returns: Cell object.
   */
  public Cell(int state) {
    myState = state;
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: TODO
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: TODO
   */
  public abstract void prepareNewState(int[] neighborStates);

  /**
   * Purpose: Update current cell state.
   * Assumptions: TODO
   * Parameters: None.
   * Exceptions: None.
   * Returns: String object.
   */
  public abstract int updateState();

  /**
   * Purpose: Returns state of the cell.
   * Assumptions: TODO
   * Parameters: None.
   * Exceptions: None.
   * Returns: int state.
   */
  public int getState() {
    return myState;
  }
}
