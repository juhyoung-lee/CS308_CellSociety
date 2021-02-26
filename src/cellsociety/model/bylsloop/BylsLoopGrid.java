package cellsociety.model.bylsloop;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.List;
import java.util.Map;

/**
 * Allows for the simulation of BylsLoop.
 * Assumptions: game will follow the rules laid out in BylsLoopCell.
 * Dependencies: java.util.*, GridHelper, Grid, Cell, BylsLoopCell
 * Examples:
 * '''
 * Grid grid = new BylsLoopGrid(cellA, gridP, cellP);
 * grid.updateCells();
 * '''
 */
public class BylsLoopGrid extends Grid {

  /**
   * Fills grid and instantiates Cells.
   *
   * @param cellArrangement cell grid from XML
   * @param gridParameters grid settings from XML
   * @param cellParameters game settings from XML
   * @throws Exception invalid cell state
   */
  public BylsLoopGrid(List<String> cellArrangement, String[] gridParameters,
      Map<String, Integer> cellParameters) throws Exception {
    super(cellArrangement, gridParameters, cellParameters);
  }

  /**
   * Creates BylsLoopCell.
   *
   * @param parameters cell state and game parameters from XML
   * @return BylsLoopCell object
   */
  @Override
  protected Cell chooseCell(Map<String, Integer> parameters) {
    return new BylsLoopCell(parameters);
  }

  /**
   * Limits neighborhood size to square as the rules are very strict.
   *
   * @param index center index
   * @return values for computing neighboring indexes
   * @throws Exception when parameters are invalid
   */
  @Override
  protected int[] decideNeighborhood(int index) throws Exception {
    return switch (getShape()) {
      case SQUARE -> square();
      default -> throw new Exception(INVALID_SHAPE);
    };
  }

  private int[] square() {
    int width = getDimensions()[0];
    if (getNeighborhoodSize() == SQUARE_SIDES_MIN) {
      return new int[]{-1, width, 1, -1 * width};
    }
    return new int[]{};
  }
}
