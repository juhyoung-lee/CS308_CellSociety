package cellsociety.view;

import cellsociety.Control;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;

/**
 * Purpose: Creates the Rectangle grid for the view. Extends GridBuilder.
 * Assumptions: The parameters passed in are valid (there are no checks for incorrect parameters)
 * Dependencies: Inherits from GridBuilder class.
 * Example of use: RectangleGrid rect = new RectangleGrid(styleSheet, scene, root)
 *
 * @author Kathleen Chen
 */

public class RectangleGrid extends GridBuilder {
  /**
   * Purpose: Constructor of RectangleGrid.
   * Assumptions: None
   * Parameters: String styleSheet, Scene scene, Pane root
   * Exceptions: None
   */
  public RectangleGrid(String styleSheet, Scene scene, Pane root) {
    super(styleSheet, scene, root);
    scene.getStylesheets().add(styleSheet);
  }

  /**
   * Purpose: Create a rectangle grid with JavaFX's Polygon feature.
   *          Overrides the createPolyGrid() method in the GridBuilder class (superclass).
   * Assumptions: RectangleGrid has been initialized
   * Parameters: int rows, int cols, List cell
   * Exceptions: None
   */
  @Override
  protected void createPolyGrid(int rows, int cols, List<Integer> cells) {
    super.createPolyGrid(rows, cols, cells);
    double xSize = ((double) Control.GRID_SIZE) / cols;
    double ySize = ((double) Control.GRID_SIZE) / rows;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        double x = Control.GRID_X + j * xSize;
        double y = Control.GRID_Y + i * ySize;
        Polygon block = new Polygon();
        block.getPoints().addAll(x, y,
                x + xSize, y,
                x + xSize, y + ySize,
                x, y + ySize);
        myCells.add(block);
        myRoot.getChildren().add(block);
        block.getStyleClass().add(myType + "-" + cells.get(j + i * cols));
      }
    }
  }
}
