package cellsociety;

import java.util.ArrayList;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Purpose: Creates screen display that user interacts with.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Kathleen Chen
 */


public class ScreenControl {
  public static final String TITLE = "Cell Society";
  public static final int X_SIZE = 500;
  public static final int Y_SIZE = 600;
  public static final Paint BACKGROUND = Color.AZURE;
  public static final int GRID_SIZE = 450;
  public static final int GRID_X = (X_SIZE / 2) - (GRID_SIZE / 2);
  public static final int GRID_Y = Y_SIZE / 12;

  public static final String PLAY_IMAGE = "PlayButton.gif";
  public static final String PAUSE_IMAGE = "PauseButton.gif";
  public static final String STEP_IMAGE = "StepButton.gif";
  public static final String SPEED_UP_IMAGE = "SpeedUpButton.gif";
  public static final String SLOW_DOWN_IMAGE = "SlowDownButton.gif";
  public static final int BUTTON_SIZE = 15;

  public static final String GAME_TITLE = "Conway's Game of Life";

  private Group myRoot;
  private Text titleText;
  private ArrayList<Rectangle> myBlocks;
  private Button slowButton;
  private Button fastButton;
  private Button startButton;
  private Button pauseButton;
  private Button stepButton;

  /**
   * Initialize the scene and add buttons and text.
   ** @param stage
   */
  public void initialize(Stage stage) {
    myRoot = new Group();
    Scene scene = new Scene(myRoot, X_SIZE, Y_SIZE, BACKGROUND);
    myBlocks = new ArrayList<>();
    stage.setScene(scene);
    stage.setTitle(TITLE);
    stage.show();
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
    playImage.setFitWidth(BUTTON_SIZE);
    playImage.setFitHeight(BUTTON_SIZE);
    Button button = new Button(text, playImage);
    button.setLayoutX(x);
    button.setLayoutY(y);
    myRoot.getChildren().add(button);
    return button;
  }

  private void createSlowDownButton() {
    int x = X_SIZE * 2 / 9;
    int y = Y_SIZE / 12 + 500;
    String pause = "Slow Down";
    slowButton = myImage(SLOW_DOWN_IMAGE, pause, x, y);
    slowButton.setOnAction(event -> slowDown());
  }

  private void slowDown() {
  }

  private void createSpeedUpButton() {
    int x = X_SIZE *  3 / 5;
    int y = Y_SIZE / 12 + 500;
    String pause = "Speed up";
    fastButton = myImage(SPEED_UP_IMAGE, pause, x, y);
    fastButton.setOnAction(event -> speedUp());
  }

  private void speedUp() {
  }

  private void createStepButton() {
    int x = X_SIZE * 4 / 5;
    int y = Y_SIZE / 12 + 460;
    String pause = "Step";
    stepButton = myImage(STEP_IMAGE, pause, x, y);
    stepButton.setOnAction(event -> step());
  }

  private void step() {
    int rows = 15;
    int cols = 15;
    ArrayList<Integer> cell = new ArrayList<Integer>(rows * cols);
    Random rand = new Random();
    for (int i = 1; i <= rows * cols; i++) {
      cell.add(rand.nextInt(3));
    }
    updateCell(cell);
  }

  private void createPauseButton() {
    int x = X_SIZE / 2 - 32;
    int y = Y_SIZE / 12 + 460;
    String pause = "Pause";
    pauseButton = myImage(PAUSE_IMAGE, pause, x, y);
    pauseButton.setOnAction(event -> stop());
  }

  private void stop() {
  }

  private void createPlayButton() {
    int x = X_SIZE / 12;
    int y = Y_SIZE / 12 + 460;
    String play = "Play";
    startButton = myImage(PLAY_IMAGE, play, x, y);
    startButton.setOnAction(event -> start());
  }

  private void start() {
  }

  private void setGameTitleText() {
    titleText = new Text(0, 30, GAME_TITLE);
    Font font = new Font(30);
    titleText.setFont(font);
    titleText.setX(X_SIZE / 2 - (titleText.getLayoutBounds().getWidth() / 2));
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
    int xsize = GRID_SIZE / cols;
    int ysize = GRID_SIZE / rows;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Rectangle block = new Rectangle(GRID_X + j * xsize, GRID_Y + i * ysize, xsize, ysize);
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

  public void updateCell(ArrayList<Integer> cells) {
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

  public void clearGrid(){
    myRoot.getChildren().clear();
    createButtons();
    setGameTitleText();
  }
}