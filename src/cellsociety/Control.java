package cellsociety;

import cellsociety.configuration.Simulation;
import cellsociety.model.Grid;
import cellsociety.model.bylsloop.BylsLoopGrid;
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
 * Purpose: Creates simulation and runs step function. Assumptions: TODO Dependencies: TODO Example
 * of use: TODO
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
  private String myDataFile;

  Simulation simulation;
  private String title;
  private String type;
  private List<String> cells;
  Map<String, Integer> params;

  /**
   * Purpose: Initialize the scene and animation timeline. Assumptions: TODO Dependencies: TODO
   * Example of use: TODO
   */
  public void initialize(Stage stage) {
    myStage = stage;
    myStage.setResizable(false);

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
   * Purpose: Upload a XML file from the computer when the upload button is pressed. Assumptions:
   * TODO Dependencies: TODO Example of use: TODO
   */
  public void uploadFile() {
    FileChooser fileChooser = new FileChooser();
    File selectedFile = fileChooser.showOpenDialog(myStage);
    myDataFile = selectedFile.getPath();
    try {
      acceptXMLData(myDataFile);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      //display error message
      //e.getMessage convert it, display it
    }
  }

  private void createStageExceptionHandler() {
    try {
      createStage();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      //display error message
      //e.getMessage convert it, display it
    }
  }

  /**
   * updates parameteres from edit params
   * TODO
   */
  public void updateParams(Map<String, Integer> newParams) {
    for (String key : newParams.keySet()) {
      params.put(key, newParams.get(key));
    }
    mySC.setParams(params);
    mySC.clearGrid();
    createStageExceptionHandler();
  }

  private void createStage() throws Exception {
    // TODO: refactor into XML reader
    String shape = "square";
    int nSize = switch (type) {
      case "Game of Life" -> 8;
      case "Percolation" -> 4;
      case "Fire" -> 4;
      case "Segregation" -> 8;
      case "WaTor" -> 8;
      case "Rock Paper Scissors" -> 8;
      case "Foraging Ants" -> 4;
      case "Byls Loop" -> 8;
      default -> 8;
    };
    params.put("neighborhoodSize", nSize);

    myGrid = switch (type) {
      case "Game of Life" -> new GameOfLifeGrid(cells, shape, params);
      case "Percolation" -> new PercolationGrid(cells, shape, params);
      case "Fire" -> new FireGrid(cells, shape, params);
      case "Segregation" -> new SegregationGrid(cells, shape, params);
      case "WaTor" -> new WaTorGrid(cells, shape, params);
      case "Rock Paper Scissors" -> new RPSGrid(cells, shape, params);
      case "Foraging Ants" -> new ForagingAntsGrid(cells, shape, params);
      case "Byls Loop" -> new BylsLoopGrid(cells, shape, params);
      default -> null;
    };

    mySC.createRectGrid(title, type, simulation.getHeight(), simulation.getWidth(), myGrid.viewGrid());
    //mySC.createHexGrid(title, type, simulation.getHeight(), simulation.getWidth(), myGrid.viewGrid());

    resetAnimation();
  }

  private void acceptXMLData(String dataFile) throws Exception {
    mySC.resetGameTitleText();
    //mySC.clearGrid(); TODO: breaks on first upload because grid objects haven't been declared yet
    simulation = new Simulation(dataFile);

    title = simulation.getTitle();
    type = simulation.getType();
    cells = simulation.getCellRows();
    params = simulation.getParameters();

    mySC.setParams(params);
    createStageExceptionHandler();
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
   * Purpose: Pause the animation when the pause button is pressed. Assumptions: TODO Dependencies:
   * TODO Example of use: TODO
   */
  public void pause() {
    animation.stop();
  }

  /**
   * Purpose: Start the animation when the play button is pressed. Assumptions: TODO Dependencies:
   * TODO Example of use: TODO
   */
  public void start() {
    try {
      checkFilled(myDataFile);
      animation.play();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean checkFilled(String myDataFile) {
    return !myDataFile.equals(null);
  }

  /**
   * Purpose: Step through the animation step by step when the step button is called. Assumptions:
   * TODO Dependencies: TODO Example of use: TODO
   */
  public void next() {
    animation.stop();
    myGrid.updateCells();
    mySC.updateGrid(myGrid.viewGrid());
  }

  /**
   * Purpose: Speed up the animation speed when the speed up button is pressed. Assumptions: TODO
   * Dependencies: TODO Example of use: TODO
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
   * Purpose: Slow the speed of the animation when the slow button is called. Assumptions: TODO
   * Dependencies: TODO Example of use: TODO
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