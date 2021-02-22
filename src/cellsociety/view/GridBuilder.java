package cellsociety.view;

import cellsociety.Control;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Purpose: Abstract class for all grids that could appear in the view.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 */

public abstract class GridBuilder {
  private String myTitle;
  protected String myType;
  protected List<Polygon> myCells;
  private Text titleText;
  protected Pane myRoot;

  /**
   * Purpose: Creates the grid for the view.
   * Assumptions: TODO
   * Dependencies: TODO
   * Example of use: TODO
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

  public void createGrid(String title, String type, int rows, int cols, List<Integer> cells) {
    myTitle = title;
    myType = type;
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
   * Parameters: List<Integer> cells
   * Example of use: updateGrid(cells)
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
   * Example of use: clearGrid() in control class
   */
  public void clearGrid() {
    for (Polygon cell : myCells) {
      cell.getStyleClass().clear();
      myRoot.getChildren().remove(cell);
    }
    myCells.removeAll(myCells);
  }
}
