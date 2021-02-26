package cellsociety.model.percolation;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import cellsociety.model.GridHelper;
import java.util.List;
import java.util.Map;

/**
 * Allows for the simulation of Percolation.
 * Assumptions: game will follow the rules laid out in PercolationCell.
 * Dependencies: java.util.*, GridHelper, Grid, Cell, PercolationCell
 * Examples:
 * '''
 * Grid grid = new PercolationGrid(cellA, gridP, cellP);
 * grid.updateCells();
 * '''
 */
public class PercolationGrid extends Grid {

  /**
   * Fills grid and instantiates Cell objects.
   *
   * @param cellArrangement cell grid from XML
   * @param cellParameters game settings from XML
   * @param gridParameters grid settings from XML
   * @throws Exception when parameters are invalid
   */
  public PercolationGrid(List<String> cellArrangement, String[] gridParameters,
      Map<String, Integer> cellParameters) throws Exception {
    super(cellArrangement, gridParameters, cellParameters);
  }

  /**
   * Returns Percolation cell.
   *
   * @param parameters cell state and game parameters from XML
   * @return Percolation cell object
   */
  @Override
  protected Cell chooseCell(Map<String, Integer> parameters) {
    return new PercolationCell(parameters);
  }

  /**
   * Neighborhood ignores cells on bottom as water cannot flow upwards.
   *
   * @param index center index
   * @return top, left, right cells
   */
  @Override
  protected int[] decideNeighborhood(int index) throws Exception {
    // percolation only looks at cells above and next
    return switch (getShape()) {
      case GridHelper.SQUARE -> square();
      case GridHelper.TRIANGLE -> triangle(index);
      case GridHelper.HEXAGON -> hexagon(index);
      default -> throw new Exception(INVALID_SHAPE);
    };
  }

  private int[] square() {
    if (getNeighborhoodSize() != SQUARE_SIDES_MIN) {
      return new int[]{};
    }
    int width = getDimensions()[0];
    return new int[]{-1 * width, -1, 1};
  }

  private int[] triangle(int index) {
    int w = getDimensions()[0];
    boolean trianglePointy = isTriangleTopPointy(index);

    if (trianglePointy && getNeighborhoodSize() == TRIANGLE_SIDES_MIN) {
      return new int[]{-1, 1};
    } else if (getNeighborhoodSize() == TRIANGLE_SIDES_MIN) {
      return new int[]{-1 * w, -1, 1};
    } else {
      return new int[]{};
    }
  }

  private int[] hexagon(int index) {
    int w = getDimensions()[0];
    if (getNeighborhoodSize() != HEXAGON_SIDES) {
      return new int[]{};
    }
    boolean evenRow = (index / w) % 2 == 0;
    if (evenRow) {
      return new int[]{-1 - w, -1 * w, -1, 1};
    } else {
      return new int[]{-1 * w, 1 - w, -1, 1};
    }
  }
}
