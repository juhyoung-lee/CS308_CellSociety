package cellsociety;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

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

  public void initialize(Stage stage) {

  }
}
