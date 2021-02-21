package cellsociety.view;

import cellsociety.Control;
import cellsociety.view.GridBuilder;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;

/**
 * Purpose: Creates the Rectangle grid for the view. Extends GridBuilder.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Jessica Yang
 */
public class HexagonGrid extends GridBuilder {

  /**
   * Purpose: Constructor for HexagonGrid class.
   * Assumptions: TODO
   * Parameters: String styleSheet, Scene scene, Pane root.
   * Exceptions: TODO
   * Returns: HexagonGrid object.
   */
  public HexagonGrid(String styleSheet, Scene scene, Pane root) {
    super(styleSheet, scene, root);
    scene.getStylesheets().add(styleSheet);
  }

  /**
   * Purpose: Populates view with hexagon Polygons.
   * Assumptions: TODO
   * Parameters: int rows, int cols, List cells.
   * Exceptions: TODO
   * Returns: None.
   */
  @Override
  protected void createPolyGrid(int rows, int cols, List<Integer> cells) {
    super.createPolyGrid(rows, cols, cells);
    double width = ((double) Control.GRID_SIZE) / (cols + 0.5);
    double width_offset = width / 2.0;

    double effective_height = ((double) Control.GRID_SIZE) / (rows + 0.25);
    double slant_height = effective_height / 3.0;
    double vertical_height = slant_height * 2.0;

    myType = myType.replaceAll("\\s", "");

    for (int i = 0; i < rows; i++) {
      double row_offset = (i % 2) * width_offset;
      for (int j = 0; j < cols; j++) {
        double x = Control.GRID_X + row_offset + j * width;
        double y = Control.GRID_Y + slant_height + i * effective_height;
        Polygon block = new Polygon();
        block.getPoints().addAll(x, y,
            x + width_offset, y - slant_height,
            x + width, y,
            x + width, y + vertical_height,
            x + width_offset, y + effective_height,
            x, y + vertical_height);
        myCells.add(block);
        myRoot.getChildren().add(block);
        block.getStyleClass().add(myType + "-" + cells.get(j + i * cols));
      }
    }
  }
}
