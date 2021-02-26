package cellsociety.view;

import cellsociety.Control;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Purpose: Abstract class for all grids that could appear in the view.
 * Assumptions: Size of screen is set when deciding where to place the title
 * Dependencies: cellsociety.Control, java.util.ArrayList, java.util.List, javafx.scene.Scene,
 *               javafx.scene.layout.Pane, javafx.scene.shape.Polygon, javafx.scene.text.Font,
 *               javafx.scene.text.Text
 * Example of use: Extended by HexagonGrid, RectangleGrid, and TriangleGrid
 *
 * @author Kathleen Chen
 */

public abstract class GridBuilder {
  private String myTitle;
  protected String myType;
  protected List<Polygon> myCells;
  private Text titleText;
  protected Pane myRoot;

  /**
   * Purpose: Constructor of GridBuilder class.
   * Assumptions: styleSheet exists
   * Parameters: String styleSheet, Scene scene, Pane root
   * Exception: None
   */
  public GridBuilder(String styleSheet, Scene scene, Pane root) {
    scene.getStylesheets().add(styleSheet);
    myRoot = root;
    myCells = new ArrayList<>();
  }

  private void setGameTitleText() {
    titleText = new Text(0, 30, myType + ": " + myTitle);
    titleText.setFont(new Font(20));
    titleText.getStyleClass().add("title");
    titleText.setX(Control.X_SIZE / 2 - (titleText.getLayoutBounds().getWidth() / 2));
    titleText.setY(45);
    myRoot.getChildren().add(titleText);
  }

  public Text getTitleText() {
    return titleText;
  }

  /**
   * Purpose: Instructions for creating a grid on screen.
   * Assumptions: Inputs are valid
   * Parameters: String title, String type, int rows, int cols, List cells
   * Exceptions: None
   */
  public void createGrid(String title, String type, int rows, int cols, List<Integer> cells) {
    clearGrid();
    myTitle = title;
    myType = type;
    myType = myType.replaceAll("\\s", "");
    setGameTitleText();
    myRoot.getChildren().remove(myCells);
    createPolyGrid(rows, cols, cells);
  }

  protected void createPolyGrid(int rows, int cols, List<Integer> cells) {
    Polygon cell = new Polygon();
  }

  /**
   * Purpose: Updates the grid based on new cell information passed in.
   * Assumptions: Integer list will be passed in and will have valid values.
   * Parameters: List cells
   * Exceptions: None
   */
  public void updateGrid(List<Integer> cells, int row, int col) {
    if (cells.size() != myCells.size()) {
      createGrid(myTitle, myType, row, col, cells);
    }
    for (int i = 0; i < cells.size(); i++) {
      Polygon cell = myCells.get(i);
      cell.getStyleClass().clear();
      cell.getStyleClass().add(myType + "-" + cells.get(i));
    }
  }

  /**
   * Purpose: Clear grid from view.
   * Assumptions: None
   * Parameters: None
   * Exceptions: None
   */
  public void clearGrid() {
    for (Polygon cell : myCells) {
      cell.getStyleClass().clear();
      myRoot.getChildren().remove(cell);
    }
    myCells.removeAll(myCells);
  }
}
