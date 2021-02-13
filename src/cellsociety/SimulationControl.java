package cellsociety;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Purpose: Creates simulation and runs step function.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Kathleen Chen
 */

public class SimulationControl {
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

  private ScreenControl mySC;
  private Grid myGrid;
  private int framecount;
  private double delay;
  private Timeline animation;
  private boolean paused = false;

  public void initialize(Stage stage) {
    mySC = new ScreenControl();
    Scene scene = mySC.getScene();
    stage.setScene(scene);
    stage.setTitle(TITLE);
    stage.show();
    framecount = 60;
    delay = 1.0 / framecount;

    ArrayList<String> cArrange = new ArrayList<>();
    cArrange.add("01001");
    cArrange.add("01101");
    cArrange.add("01101");
    cArrange.add("01001");
    cArrange.add("01011");
    myGrid = new Grid(GAME_TITLE, cArrange);
    mySC.createGrid(myGrid.getDimensions()[0], myGrid.getDimensions()[1], myGrid.viewGrid());

    KeyFrame frame = new KeyFrame(Duration.seconds(delay), e -> step());
    animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    //pause();
  }

  private void step() {
    myGrid.updateCells();
    mySC.updateGrid(myGrid.viewGrid());
  }

/*  public void pause() {
    if (paused) {
      animation.play();
      paused = false;
    } else {
      animation.pause();
      paused = true;
    }
  }*/

  public void stop() {
    animation.stop();
  }

  public void start() {
    animation.play();
  }
}
