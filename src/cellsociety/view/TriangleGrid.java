package cellsociety.view;

import cellsociety.Control;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;

import java.util.List;

/**
 * Purpose: Creates the Rectangle grid for the view. Extends GridBuilder.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Kathleen Chen
 */

public class TriangleGrid extends GridBuilder {
  /**
   * Purpose: Constructor of RectangleGrid
   * Assumptions: TODO
   * Parameters: TODO
   * Dependencies: TODO
   * Example of use: TODO
   */
  public TriangleGrid(String styleSheet, Scene scene, Pane root) {
    super(styleSheet, scene, root);
    scene.getStylesheets().add(styleSheet);
  }

  @Override
  protected void createPolyGrid(int rows, int cols, List<Integer> cells) {
    super.createPolyGrid(rows, cols, cells);
    double xSize = ((double) Control.GRID_SIZE) / (cols + 1);
    double ySize = ((double) Control.GRID_SIZE) / rows;
    myType = myType.replaceAll("\\s", "");
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
