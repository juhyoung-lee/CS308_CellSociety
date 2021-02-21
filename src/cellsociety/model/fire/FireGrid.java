package cellsociety.model.fire;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import cellsociety.model.IndexVariance;
import java.util.List;
import java.util.Map;

public class FireGrid extends Grid {

  /**
   * Constructor.
   *
   * @param cellArrangement cell grid from XML
   * @param parameters      game settings from XML
   */
  public FireGrid(List<String> cellArrangement, String shape,
      Map<String, Integer> parameters) throws Exception {
    super(cellArrangement, shape, parameters);
  }

  /**
   * Used by setupGrid(). Create cell object matching Grid type. Assumptions: Parameters contains
   * XML information and cell state
   *
   * @param parameters cell state and game parameters from XML
   * @return appropriate cell object
   */
  @Override
  protected Cell chooseCell(Map<String, Integer> parameters) {
    return new FireCell(parameters);
  }

  /**
   * Used by pullNeighborIndexes(). Returns array of values to be added to center index to get
   * neighboring indexes. Assumptions: Grid is a square tesselation.
   *
   * @param index center index
   * @return values for computing neighboring indexes
   */
  @Override
  protected int[] decideNeighborhood(int index) throws Exception {
    int width = getDimensions()[0];
    int neighborhoodSize = getNeighborhoodSize();
    IndexVariance varianceCalculator = new IndexVariance(index, width, neighborhoodSize);

    return switch (getShape()) {
      case "square" -> varianceCalculator.squareCardinal();
      case "triangle" -> varianceCalculator.triangleImmediate();
      case "hexagon" -> varianceCalculator.hexagon();
      default -> throw new Exception("Invalid shape: " + getShape());
    };
  }
}
