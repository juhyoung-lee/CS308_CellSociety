package cellsociety;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Purpose: Creates screen display that user interacts with.
 * Assumptions: TODO
 * Dependencies: Depends on the grid array passed in to create a grid
 * Example of use: mySC = new ScreenControl() mySC.createGrid(row, col, Grid.viewGrid()) mySC.clearGrid()
 *
 * @author Kathleen Chen
 */


public class ScreenControl {
  private Pane myRoot;
  private Text titleText;
  private ArrayList<Rectangle> myBlocks;
  private Button slowButton;
  private Button fastButton;
  private Button startButton;
  private Button pauseButton;
  private Button stepButton;
  private Button uploadButton;
  private int sX;
  private int sY;
  private Scene myScene;
  private Control sim;
  private String myTitle;
  private String myType;
  private ResourceBundle myResources;
  /**
   * Initialize the scene and add buttons and text.
   */
  public ScreenControl(Control simulationControl, String title, String type) {
    myRoot = new Pane();
    sX = Control.X_SIZE;
    sY = Control.Y_SIZE;
    myTitle = title;
    myType = type;
    myResources = ResourceBundle.getBundle("cellsociety.Visual");
    myScene = new Scene(myRoot, sX, sY);
    myScene.getStylesheets().add(Control.STYLESHEET);
    myBlocks = new ArrayList<>();
    setGameTitleText();
    createButtons();
    sim = simulationControl;
  }

  private void createButtons() {
    startButton = buttonCreation(myResources.getString("PlayButton"), sX / 12, sY / 12 + 460);
    startButton.setOnAction(event -> sim.start());
    pauseButton = buttonCreation(myResources.getString("PauseButton"), sX / 2 - 32, sY / 12 + 460);
    pauseButton.setOnAction(event -> sim.pause());
    stepButton = buttonCreation(myResources.getString("StepButton"), sX * 4 / 5, sY / 12 + 460);
    stepButton.setOnAction(event -> sim.next());
    fastButton = buttonCreation(myResources.getString("SpeedUpButton"), sX *  3 / 5, sY / 12 + 500);
    fastButton.setOnAction(event -> sim.fast());
    slowButton = buttonCreation(myResources.getString("SlowDownButton"), sX * 2 / 9, sY / 12 + 500);
    slowButton.setOnAction(event -> sim.slow());
    uploadButton = buttonCreation(myResources.getString("UploadButton"), sX - 100, 5);
    uploadButton.setOnAction(event -> sim.slow());
  }

  private Button buttonCreation(String text, double x, double y) {
    Button button = new Button(text);
    button.setLayoutX(x);
    button.setLayoutY(y);
    myRoot.getChildren().add(button);
    button.getStyleClass().add("button");
    return button;
  }

  private void setGameTitleText() {
    titleText = new Text(0, 30, myType + ": " + myTitle);
    titleText.setFont(new Font(30));
    titleText.getStyleClass().add("title");
    titleText.setX(sX / 2 - (titleText.getLayoutBounds().getWidth() / 2));
    myRoot.getChildren().add(titleText);
  }

  /**
   * Creates display of Grid for viewer to see.
   ** @param rows
   ** @param cols
   ** @param cells
   */
  public void createGrid(int rows, int cols, ArrayList<Integer> cells) {
    myRoot.getChildren().remove(myBlocks);
    int xsize = Control.GRID_SIZE / cols;
    int ysize = Control.GRID_SIZE / rows;
    myType = myType.replaceAll("\\s", "");
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Rectangle block = new Rectangle(Control.GRID_X + j * xsize, Control.GRID_Y + i * ysize, xsize, ysize);
        myBlocks.add(block);
        block.setStroke(Color.BLACK);
        myRoot.getChildren().add(block);
        block.getStyleClass().add(myType + "-" + cells.get(j + i * cols));
      }
    }
  }

  /**
   * Updates the grid based on new cell information passed in.
   ** @param cells
   */
  public void updateGrid(ArrayList<Integer> cells) {
    for (int i = 0; i < cells.size(); i++) {
      Rectangle block = myBlocks.get(i);
      block.getStyleClass().clear();
      block.getStyleClass().add(myType + "-" + cells.get(i));
    }
  }

  /**
   * Clears the grid.
   */
  public void clearGrid() {
    myRoot.getChildren().clear();
    createButtons();
    setGameTitleText();
  }

  /**
   * Returns the Scene.
   ** @return Scene
   */
  public Scene getScene() {
    return myScene;
  }
}