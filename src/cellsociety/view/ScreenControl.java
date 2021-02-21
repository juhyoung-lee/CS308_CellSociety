package cellsociety.view;

import cellsociety.Control;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
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
  private List<Rectangle> myBlocks;
  private List<Polygon> myTriangle;
  private List<Polygon> myHexagon;
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
  private String myType;
  private ResourceBundle myResources;
  private String myStyleSheet;
  private RectangleGrid myRectGrid;
  private HexagonGrid myHexGrid;

  /**
   * Initialize the scene and add buttons and text.
   */
  public ScreenControl(Control simulationControl) {
    myRoot = new Pane();
    sX = Control.X_SIZE;
    sY = Control.Y_SIZE;
    createResourceButton();
    createStyleButton();
    myResources = ResourceBundle.getBundle("cellsociety.view.resources.English");
    myStyleSheet = "cellsociety/view/resources/default.css";
    myScene = new Scene(myRoot, sX, sY);
    myScene.getStylesheets().add(myStyleSheet);
    myBlocks = new ArrayList<>();
    createButtons();
    sim = simulationControl;
  }

  private void createStyleButton() {
    ComboBox<String> styleButton = new ComboBox();
    styleButton.getItems().addAll(
            "dark",
            "light",
            "default"
    );
    styleButton.setValue("default");
    styleButton.setLayoutX(100);
    myRoot.getChildren().add(styleButton);
    styleButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        myScene.getStylesheets().remove(myStyleSheet);
        myStyleSheet = "cellsociety/view/resources/" + styleButton.getValue() + ".css";
        myScene.getStylesheets().add(myStyleSheet);
        resetButtons();
      }
    });
  }

  private void createResourceButton() {
    ComboBox<String> resourceButton = new ComboBox();
    resourceButton.getItems().addAll(
            "English",
            "Chinese",
            "Spanish"
    );
    resourceButton.setValue("English");
    myRoot.getChildren().add(resourceButton);
    resourceButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        myResources = ResourceBundle.getBundle("cellsociety.view.resources." + resourceButton.getValue());
        resetButtons();
      }
    });
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
    uploadButton = buttonCreation(myResources.getString("UploadButton"), sX - 120, 0);
    uploadButton.setOnAction(event -> sim.uploadFile());
  }

  private Button buttonCreation(String text, double x, double y) {
    Button button = new Button(text);
    button.setLayoutX(x);
    button.setLayoutY(y);
    myRoot.getChildren().add(button);
    button.getStyleClass().add("button");
    return button;
  }

  public void resetGameTitleText() {
    myRoot.getChildren().remove(titleText);
  }

  /**
   * Creates display of Rectangle Grid for viewer to see.
   ** @param rows
   ** @param cols
   ** @param cells
   */
  public void createRectGrid(String title, String type, int rows, int cols, List<Integer> cells) {
    myRectGrid = new RectangleGrid(myStyleSheet, myScene, myRoot);
    myRectGrid.createGrid(title, type, rows, cols, cells);
    titleText = myRectGrid.getTitleText();
  }

  public void createTriGrid(int rows, int cols, List<Integer> cells) {
    myRoot.getChildren().remove(myTriangle);
    double xSize = ((double) Control.GRID_SIZE) / cols;
    double ySize = ((double) Control.GRID_SIZE) / rows;
    myType = myType.replaceAll("\\s", "");
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Rectangle block = new Rectangle(Control.GRID_X + j * xSize, Control.GRID_Y + i * ySize, xSize, ySize);
        myBlocks.add(block);
        myRoot.getChildren().add(block);
        block.getStyleClass().add(myType + "-" + cells.get(j + i * cols));
      }
    }
  }

  public void createHexGrid(String title, String type, int rows, int cols, List<Integer> cells) {
    myHexGrid = new HexagonGrid(myStyleSheet, myScene, myRoot);
    myHexGrid.createGrid(title, type, rows, cols, cells);
    titleText = myHexGrid.getTitleText();
  }

  /**
   * Updates the grid based on new cell information passed in.
   ** @param cells
   */
  public void updateGrid(List<Integer> cells) {
    myRectGrid.updateGrid(cells);
  }

  public void clearGrid() {
    myRectGrid.clearGrid();
  }

  private void resetButtons() {
    myRoot.getChildren().remove(startButton);
    myRoot.getChildren().remove(pauseButton);
    myRoot.getChildren().remove(fastButton);
    myRoot.getChildren().remove(slowButton);
    myRoot.getChildren().remove(stepButton);
    myRoot.getChildren().remove(uploadButton);
    createButtons();
  }

  /**
   * Returns the Scene.
   ** @return Scene
   */
  public Scene getScene() {
    return myScene;
  }

  public Pane getRoot() {
    return myRoot;
  }
}
