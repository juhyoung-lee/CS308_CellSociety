package cellsociety;

import cellsociety.configuration.Configure;
import cellsociety.configuration.Simulation;
import cellsociety.model.Grid;
import cellsociety.model.fire.FireGrid;
import cellsociety.model.foragingants.ForagingAntsGrid;
import cellsociety.model.gameoflife.GameOfLifeGrid;
import cellsociety.model.percolation.PercolationGrid;
import cellsociety.model.rps.RPSGrid;
import cellsociety.model.segregation.SegregationGrid;
import cellsociety.model.wator.WaTorGrid;

import java.io.File;

import cellsociety.view.ScreenControl;
import java.util.List;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Purpose: Creates simulation and runs step function.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Kathleen Chen
 */

public class Control {
  public static final String TITLE = "Cell Society";
  public static final int X_SIZE = 500;
  public static final int Y_SIZE = 600;
  public static final int GRID_SIZE = 450;
  public static final int GRID_X = (X_SIZE / 2) - (GRID_SIZE / 2);
  public static final int GRID_Y = Y_SIZE / 12;

  private ScreenControl mySC;
  private Grid myGrid;
  private int frameCount;
  private double delay;
  private Timeline animation;
  private Stage myStage;

  /**
   * Purpose: Initialize the scene and animation timeline.
   * Assumptions: TODO
   * Dependencies: TODO
   * Example of use: TODO
   */
  public void initialize(Stage stage) {
    myStage = stage;

    frameCount = 1;
    delay = 1.0 / frameCount;
    KeyFrame frame = new KeyFrame(Duration.seconds(delay), e -> step());
    animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);

    mySC = new ScreenControl(this);
    Scene scene = mySC.getScene();
    stage.setScene(scene);
    stage.setTitle(TITLE);
    stage.show();
  }

  /**
   * Purpose: Upload a XML file from the computer when the upload button is pressed.
   * Assumptions: TODO
   * Dependencies: TODO
   * Example of use: TODO
   */
  public void uploadFile() {
    FileChooser fileChooser = new FileChooser();
    File selectedFile = fileChooser.showOpenDialog(myStage);
    String dataFile = selectedFile.getPath();
    createStageFromData(dataFile);
  }

  private void createStageFromData(String dataFile) {
    mySC.resetGameTitleText();
    mySC.clearGrid();

    Configure config = new Configure(dataFile);
    Simulation simulation = config.getSimulation();

    String title = simulation.getTitle();
    String type = simulation.getType();
    List<String> cells = simulation.getCellRows();
    Map<String, Integer> params = simulation.getParameters();

    myGrid = switch (type) {
      case "Game of Life" -> new GameOfLifeGrid(cells, params);
      case "Percolation" -> new PercolationGrid(cells, params);
      case "Fire" -> new FireGrid(cells, params);
      case "Segregation" -> new SegregationGrid(cells, params);
      case "WaTor" -> new WaTorGrid(cells, params);
      case "Rock Paper Scissors" -> new RPSGrid(cells, params);
      case "Foraging Ants" -> new ForagingAntsGrid(cells, params);
      default -> null;
    };

    mySC.createGrid(title, type, simulation.getHeight(), simulation.getWidth(), myGrid.viewGrid());

    resetAnimation();
  }

  private void resetAnimation() {
    animation.stop();
    animation.getKeyFrames().clear();
    frameCount = 1;
    delay = 1.0 / frameCount;
    KeyFrame frame = new KeyFrame(Duration.seconds(delay), e -> step());
    animation.getKeyFrames().add(frame);
  }

  private void step() {
    myGrid.updateCells();
    mySC.updateGrid(myGrid.viewGrid());
  }

  /**
   * Purpose: Pause the animation when the pause button is pressed.
   * Assumptions: TODO
   * Dependencies: TODO
   * Example of use: TODO
   */
  public void pause() {
    animation.stop();
  }

  /**
   * Purpose: Start the animation when the play button is pressed.
   * Assumptions: TODO
   * Dependencies: TODO
   * Example of use: TODO
   */
  public void start() {
    animation.play();
  }

  /**
   * Purpose: Step through the animation step by step when the step button is called.
   * Assumptions: TODO
   * Dependencies: TODO
   * Example of use: TODO
   */
  public void next() {
    animation.stop();
    myGrid.updateCells();
    mySC.updateGrid(myGrid.viewGrid());
  }

  /**
   * Purpose: Speed up the animation speed when the speed up button is pressed.
   * Assumptions: TODO
   * Dependencies: TODO
   * Example of use: TODO
   */
  public void fast() {
    animation.stop();
    frameCount += 1;
    if (frameCount < 0) {
      frameCount = 1;
    }
    animation.setRate(frameCount);
    animation.play();
  }

  /**
   * Purpose: Slow the speed of the animation when the slow button is called.
   * Assumptions: TODO
   * Dependencies: TODO
   * Example of use: TODO
   */
  public void slow() {
    animation.stop();
    frameCount -= 1;
    if (frameCount > 0) {
      animation.setRate(frameCount);
    } else if (frameCount == 0) {
      animation.setRate(0.8);
    } else if (frameCount == -1) {
      animation.setRate(0.65);
    } else {
      animation.setRate(1.0 / (frameCount * -1));
    }
    animation.play();
  }
}