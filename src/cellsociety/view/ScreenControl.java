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
  private List<Rectangle> myBlocks;
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
  private String myStyleSheet;

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
    uploadButton = buttonCreation(myResources.getString("UploadButton"), sX - 120, 5);
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

  private void setGameTitleText() {
    titleText = new Text(0, 30, myType + ": " + myTitle);
    titleText.setFont(new Font(20));
    titleText.getStyleClass().add("title");
    titleText.setX(sX / 2 - (titleText.getLayoutBounds().getWidth() / 2));
    myRoot.getChildren().add(titleText);
  }

  public void resetGameTitleText() {
    myRoot.getChildren().remove(titleText);
  }

  /**
   * Creates display of Grid for viewer to see.
   ** @param rows
   ** @param cols
   ** @param cells
   */
  public void createGrid(String title, String type, int rows, int cols, List<Integer> cells) {
    myTitle = title;
    myType = type;
    setGameTitleText();
    myRoot.getChildren().remove(myBlocks);
    int xsize = Control.GRID_SIZE / cols;
    int ysize = Control.GRID_SIZE / rows;
    myType = myType.replaceAll("\\s", "");
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Rectangle block = new Rectangle(Control.GRID_X + j * xsize, Control.GRID_Y + i * ysize, xsize, ysize);
        myBlocks.add(block);
        block.getStyleClass().add("rect");
        myRoot.getChildren().add(block);
        block.getStyleClass().add(myType + "-" + cells.get(j + i * cols));
      }
    }
  }

  /**
   * Updates the grid based on new cell information passed in.
   ** @param cells
   */
  public void updateGrid(List<Integer> cells) {
    for (int i = 0; i < cells.size(); i++) {
      Rectangle block = myBlocks.get(i);
      block.getStyleClass().clear();
      block.getStyleClass().add("rect");
      block.getStyleClass().add(myType + "-" + cells.get(i));
    }
  }

  public void clearGrid() {
    for (Rectangle block : myBlocks) {
      block.getStyleClass().clear();
      myRoot.getChildren().remove(block);
    }
    myRoot.getChildren().remove(myBlocks);
    myBlocks.removeAll(myBlocks);
    resetButtons();
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
}