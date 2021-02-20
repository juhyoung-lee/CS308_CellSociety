package cellsociety.view;

import cellsociety.Control;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Purpose: Creates the Rectangle grid for the view. Extends GridBuilder.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Kathleen Chen
 */

public class RectangleGrid extends GridBuilder {
  /**
   * Purpose: Constructor of RectangleGrid
   * Assumptions: TODO
   * Parameters: TODO
   * Dependencies: TODO
   * Example of use: TODO
   */
  public RectangleGrid(String styleSheet, Scene scene, Pane root) {
    super(styleSheet, scene, root);
    scene.getStylesheets().add(styleSheet);
  }

  @Override
  protected void createPolyGrid(int rows, int cols, List<Integer> cells) {
    super.createPolyGrid(rows, cols, cells);
    double xSize = ((double) Control.GRID_SIZE) / cols;
    double ySize = ((double) Control.GRID_SIZE) / rows;
    myType = myType.replaceAll("\\s", "");
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
