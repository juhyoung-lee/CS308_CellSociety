package cellsociety.view;

import cellsociety.Control;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;

/**
 * Purpose: Creates the Triangle grid for the view. Extends GridBuilder.
 * Assumptions: the parameters passed in are valid (there are no checks for incorrect parameters)
 * Dependencies: inherits from GridBuilder class
 * Example of use: TriangleGrid tri = new TriangleGrid(styleSheet, scene, root)
 *
 * @author Kathleen Chen
 */

public class TriangleGrid extends GridBuilder {
  /**
   * Purpose: Constructor of TriangleGrid.
   * Assumptions: None
   * Parameters: String styleSheet, Scene scene, Pane root
   * Exceptions: None
   */
  public TriangleGrid(String styleSheet, Scene scene, Pane root) {
    super(styleSheet, scene, root);
    scene.getStylesheets().add(styleSheet);
  }

  /**
   * Purpose: Create a triangle grid with JavaFX's Polygon feature.
   *          Overrides the createPolyGrid() method in the GridBuilder class (superclass).
   * Assumptions: TriangleGrid has been initialized
   * Parameters: int rows, int cols, List cell
   * Exceptions: None
   */
  @Override
  protected void createPolyGrid(int rows, int cols, List<Integer> cells) {
    super.createPolyGrid(rows, cols, cells);
    double xSize = ((double) Control.GRID_SIZE) / (cols + 1);
    double ySize = ((double) Control.GRID_SIZE) / rows;
    for (int i = 0; i < rows; i++) {
      if (i % 2 == 0) {
        for (int j = 0; j < cols; j++) {
          Polygon triangle = evenRows(i, j, xSize, ySize);
          myCells.add(triangle);
          myRoot.getChildren().add(triangle);
          triangle.getStyleClass().add(myType + "-" + cells.get(j + i * cols));
        }
      } else {
        for (int j = 0; j < cols; j++) {
          Polygon triangle = oddRows(i, j, xSize, ySize);
          myCells.add(triangle);
          myRoot.getChildren().add(triangle);
          triangle.getStyleClass().add(myType + "-" + cells.get(j + i * cols));
        }
      }
    }
  }

  private Polygon oddRows(int i, int j, double xSize, double ySize) {
    Polygon triangle = new Polygon();
    double x = Control.GRID_X + j * xSize;
    double y = Control.GRID_Y + i * ySize;
    if (j % 2 != 0) {
      triangle.getPoints().addAll(x, y,
              x + xSize * 2, y,
              x + xSize, y + ySize);
    } else {
      triangle.getPoints().addAll(x, y + ySize,
              x + xSize, y,
              x + xSize * 2, y + ySize);
    }
    return triangle;
  }

  private Polygon evenRows(int i, int j, double xSize, double ySize) {
    Polygon triangle = new Polygon();
    double x = Control.GRID_X + j * xSize;
    double y = Control.GRID_Y + i * ySize;
    if (j % 2 == 0) {
      triangle.getPoints().addAll(x, y,
              x + xSize * 2, y,
              x + xSize, y + ySize);
    } else {
      triangle.getPoints().addAll(x, y + ySize,
              x + xSize, y,
              x + xSize * 2, y + ySize);
    }
    return triangle;
  }
}
