package cellsociety.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.List;

/**
 * Purpose: Creates the Rectangle grid for the view. Extends GridBuilder.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Kathleen Chen
 */

public class TriangleGrid extends GridBuilder{
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
    myType = myType.replaceAll("\\s", "");


  }
}
