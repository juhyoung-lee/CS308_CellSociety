package cellsociety;

/**
 * Purpose: Represents a cell within the cell automota simulation.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Juhyoung Lee, Jessica Yang
 */
public class Cell {

  private final int index;
  private int state;

  /**
   * Purpose: Constructor for Cell class. Assumptions: TODO
   * Parameters: int index, int state.
   * Exceptions: TODO
   * Returns: Cell object.
   */
  public Cell(int index, int state) {
    this.index = index;
    this.state = state;
  }

  /**
   * Purpose: Returns state of the cell.
   * Assumptions: TODO
   * Parameters: None.
   * Exceptions: None.
   * Returns: int state.
   */
  public int getState() {
    return state;
  }

  /**
   * Purpose: Returns index of the cell.
   * Assumptions: TODO
   * Parameters: None.
   * Exceptions: None.
   * Returns: int index.
   */
  public int getIndex() {
    return index;
  }
}
