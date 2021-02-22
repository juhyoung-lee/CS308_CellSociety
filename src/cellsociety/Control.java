package cellsociety;

import cellsociety.configuration.Simulation;
import cellsociety.configuration.XMLException;
import cellsociety.model.Grid;
import cellsociety.model.bylsloop.BylsLoopGrid;
import cellsociety.model.fire.FireGrid;
import cellsociety.model.foragingants.ForagingAntsGrid;
import cellsociety.model.gameoflife.GameOfLifeGrid;
import cellsociety.model.percolation.PercolationGrid;
import cellsociety.model.rps.RPSGrid;
import cellsociety.model.segregation.SegregationGrid;
import cellsociety.model.sugarscape.SugarScapeGrid;
import cellsociety.model.wator.WaTorGrid;
import java.io.File;

import cellsociety.view.ScreenControl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
 * @author Kathleen Chen, Jessica Yang
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
  public Control() {
    myStage = new Stage();
    myStage.setResizable(false);

    frameCount = 1;
    delay = 1.0 / frameCount;
    KeyFrame frame = new KeyFrame(Duration.seconds(delay), e -> step());
    animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);

    mySC = new ScreenControl(this);
    Scene scene = mySC.getScene();
    myStage.setScene(scene);
    myStage.setTitle(TITLE);
    myStage.show();
  }

  /**
   * Purpose: Upload a XML file from the computer when the upload button is pressed. Assumptions:
   * TODO Dependencies: TODO Example of use: TODO
   */
  public void uploadFile() {
    FileChooser fileChooser = new FileChooser();
    File selectedFile = null;
    while (selectedFile == null) {
      selectedFile = fileChooser.showOpenDialog(myStage);
    }
    myDataFile = selectedFile.getPath();
    try {
      acceptXMLData(myDataFile);
    } catch (Exception e) {
      mySC.displayErrorMessage(e.getMessage());
      //display error message
      //e.getMessage convert it, display it
    }
    mySC.clearError();
  }

  private void createStageExceptionHandler() {
    try {
      createStage();
    } catch (Exception e) {
      mySC.displayErrorMessage(e.getMessage());

      //display error message
      //e.getMessage convert it, display it
    }
    mySC.clearError();
  }


  /**
   * Purpose: Updates parameters from edit parameters function. Assumptions: TODO Parameters: Map
   * newParams. Exceptions: TODO Returns: None.
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
    String[] gridParam = simulation.getGridParameterArray();

    myGrid = switch (type) {
      case "Game of Life" -> new GameOfLifeGrid(cells, gridParam, params);
      case "Percolation" -> new PercolationGrid(cells, gridParam, params);
      case "Fire" -> new FireGrid(cells, gridParam, params);
      case "Segregation" -> new SegregationGrid(cells, gridParam, params);
      case "WaTor" -> new WaTorGrid(cells, gridParam, params);
      case "Rock Paper Scissors" -> new RPSGrid(cells, gridParam, params);
      case "Foraging Ants" -> new ForagingAntsGrid(cells, gridParam, params);
      case "Byls Loop" -> new BylsLoopGrid(cells, gridParam, params);
      case "SugarScape" -> new SugarScapeGrid(cells, gridParam, params);
      default -> null;
    };
    if (myGrid == null) {
      throw new XMLException("BadType");
    }

    mySC.createGrid(title, type, simulation.getHeight(), simulation.getWidth(), myGrid.viewGrid(),
        gridParam[0]);

//    mySC.createGrid(title, type, simulation.getHeight(), simulation.getWidth(), myGrid.viewGrid(), gridParam[0]);
    resetAnimation();
  }

  private void acceptXMLData(String dataFile) throws Exception {
    mySC.clearGrid();
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
    mySC.updateGrid(myGrid.viewGrid(), myGrid.getDimensions()[0], myGrid.getDimensions()[1]);
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
    animation.play();
  }

  /**
   * Purpose: Step through the animation step by step when the step button is called. Assumptions:
   * TODO Dependencies: TODO Example of use: TODO
   */
  public void next() {
    animation.stop();
    myGrid.updateCells();
    mySC.updateGrid(myGrid.viewGrid(), myGrid.getDimensions()[0], myGrid.getDimensions()[1]);
  }

  /**
   * Purpose: Speed up the animation speed when the speed up button is pressed. ssumptions: TODO
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

  public void configuration() {
    List<Integer> cells = myGrid.viewGrid();
    createFile(simulation.getInfoMap(),params, cells);
  }

  private void createFile(
      Map<String, String> info, Map<String, Integer> params, List<Integer> cells) {

    List<String> cellRows = createCellRows(params, cells);

    try {
      FileWriter myWriter = new FileWriter("data/CreatedFiles/created" +
              (System.currentTimeMillis() / 10000%100000) + ".XML");
      myWriter.write("<root>\n");
      myWriter.write("  <information>\n");
      for (String s : info.keySet()) {
        myWriter.write("    <" + s + ">");
        myWriter.write(info.get(s));
        myWriter.write("</" + s + ">\n");
      }
      myWriter.write("  </information>\n");
      myWriter.write("  <parameters>\n");
      for (String s : params.keySet()) {
        myWriter.write("    <" + s + ">");
        myWriter.write(params.get(s).toString());
        myWriter.write("</" + s + ">\n");
      }
      myWriter.write("  </parameters>\n");
      myWriter.write("  <cells>\n");
      for (String s : cellRows) {
        myWriter.write("    <cellRow>" + s + "</cellRow>\n");
      }
      myWriter.write("  </cells>\n");
      myWriter.write("</root>");
      myWriter.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<String> createCellRows(Map<String, Integer> params, List<Integer> cells) {
    List<String> returned = new ArrayList<>();
    String temp = "";
    int width = params.get("width");
    int height = params.get("height");
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        temp = temp + cells.get(i * height + j);
      }
      returned.add(temp);
      temp = "";
    }
    return returned;
  }
}