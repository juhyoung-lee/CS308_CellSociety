package cellsociety;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
  private Group myRoot;
  private Text titleText;
  private ArrayList<Rectangle> myBlocks;
  private Button slowButton;
  private Button fastButton;
  private Button startButton;
  private Button pauseButton;
  private Button stepButton;
  private int sX;
  private int sY;
  private Scene myScene;

  /**
   * Initialize the scene and add buttons and text.
   */
  public ScreenControl() {
    myRoot = new Group();
    sX = SimulationControl.X_SIZE;
    sY = SimulationControl.Y_SIZE;
    myScene = new Scene(myRoot, sX, sY, SimulationControl.BACKGROUND);
    myBlocks = new ArrayList<>();
    setGameTitleText();
    createButtons();
  }

  private void createButtons() {
    createPlayButton();
    createPauseButton();
    createStepButton();
    createSpeedUpButton();
    createSlowDownButton();
  }

  private Button myImage(String icon, String text, double x, double y) {
    Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(icon));
    ImageView playImage = new ImageView(image);
    playImage.setFitWidth(SimulationControl.BUTTON_SIZE);
    playImage.setFitHeight(SimulationControl.BUTTON_SIZE);
    Button button = new Button(text, playImage);
    button.setLayoutX(x);
    button.setLayoutY(y);
    myRoot.getChildren().add(button);
    return button;
  }

  private void createSlowDownButton() {
    int x = sX * 2 / 9;
    int y = sY / 12 + 500;
    String pause = "Slow Down";
    slowButton = myImage(SimulationControl.SLOW_DOWN_IMAGE, pause, x, y);
    slowButton.setOnAction(event -> slowDown());
  }

  private void slowDown() {
  }

  private void createSpeedUpButton() {
    int x = sX *  3 / 5;
    int y = sY / 12 + 500;
    String pause = "Speed up";
    fastButton = myImage(SimulationControl.SPEED_UP_IMAGE, pause, x, y);
    fastButton.setOnAction(event -> speedUp());
  }

  private void speedUp() {
  }

  private void createStepButton() {
    int x = sX * 4 / 5;
    int y = sY / 12 + 460;
    String pause = "Step";
    stepButton = myImage(SimulationControl.STEP_IMAGE, pause, x, y);
    stepButton.setOnAction(event -> step());
  }

  private void step() {
  }

  private void createPauseButton() {
    int x = sX / 2 - 32;
    int y = sY / 12 + 460;
    String pause = "Pause";
    pauseButton = myImage(SimulationControl.PAUSE_IMAGE, pause, x, y);
    pauseButton.setOnAction(event -> stop());
  }

  private void stop() {
  }

  private void createPlayButton() {
    int x = sX / 12;
    int y = sY / 12 + 460;
    String play = "Play";
    startButton = myImage(SimulationControl.PLAY_IMAGE, play, x, y);
    startButton.setOnAction(event -> start());
  }

  private void start() {
  }

  private void setGameTitleText() {
    titleText = new Text(0, 30, SimulationControl.GAME_TITLE);
    Font font = new Font(30);
    titleText.setFont(font);
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
    int xsize = SimulationControl.GRID_SIZE / cols;
    int ysize = SimulationControl.GRID_SIZE / rows;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Rectangle block = new Rectangle(SimulationControl.GRID_X + j * xsize, SimulationControl.GRID_Y + i * ysize, xsize, ysize);
        myBlocks.add(block);
        block.setStroke(Color.BLACK);
        myRoot.getChildren().add(block);
        if (cells.get(j + i * cols) == 0) {
          block.setFill(Color.WHITE);
        } else if (cells.get(j + i * cols) == 1) {
          block.setFill(Color.MEDIUMBLUE);
        } else {
          block.setFill(Color.DEEPPINK);
        }
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
      block.setStroke(Color.BLACK);
      if (cells.get(i) == 0) {
        block.setFill(Color.WHITE);
      } else if (cells.get(i) == 1) {
        block.setFill(Color.MEDIUMBLUE);
      } else {
        block.setFill(Color.DEEPPINK);
      }
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

  public Scene getScene() {
    return myScene;
  }
}