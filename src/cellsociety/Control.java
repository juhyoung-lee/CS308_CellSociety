package cellsociety;

import cellsociety.configuration.Configure;
import cellsociety.configuration.Game;
import cellsociety.model.Grid;
import cellsociety.model.fire.FireGrid;
import cellsociety.model.gameoflife.GameOfLifeGrid;
import cellsociety.model.percolation.PercolationGrid;
import cellsociety.model.segregation.SegregationGrid;
import cellsociety.model.wator.WaTorGrid;
import java.util.HashMap;

import cellsociety.view.ScreenControl;
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

public class Control {
  public static final String DATA_FILE="data/XMLs/WaTor/first.XML";

  public static final String TITLE = "Cell Society";
  public static final int X_SIZE = 500;
  public static final int Y_SIZE = 600;
  public static final Paint BACKGROUND = Color.AZURE;
  public static final int GRID_SIZE = 450;
  public static final int GRID_X = (X_SIZE / 2) - (GRID_SIZE / 2);
  public static final int GRID_Y = Y_SIZE / 12;

  public static final String STYLESHEET = "cellsociety/view/resources/default.css";

  private ScreenControl mySC;
  private Grid myGrid;
  private int framecount = 1;
  private double delay;
  private Timeline animation;

  public void initialize(Stage stage) {
    delay = 1.0 / framecount;
    KeyFrame frame = new KeyFrame(Duration.seconds(delay), e -> step());
    animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);

    Configure config = new Configure(DATA_FILE);
    Game game = config.getGame();

    String title = game.getTitle();
    String type = game.getType();
    ArrayList<String> cells = game.getCellRows();
    HashMap<String, Integer> params = game.getParameters();
    if (type.equals("Game of Life")) {
      type = "Game of Life";
    }
    myGrid = switch (type) {
      case "Game of Life" -> new GameOfLifeGrid(cells, params);
      case "Percolation" -> new PercolationGrid(cells, params);
      case "Fire" -> new FireGrid(cells, params);
      case "Segregation" -> new SegregationGrid(cells, params);
      case "WaTor" -> new WaTorGrid(cells, params);
      default -> null;
    };

    mySC = new ScreenControl(this, title, type);
    Scene scene = mySC.getScene();
    stage.setScene(scene);
    stage.setTitle(TITLE);
    stage.show();

    mySC.createGrid(game.getHeight(), game.getWidth(), myGrid.viewGrid());
  }

  private void step() {
    myGrid.updateCells();
    mySC.updateGrid(myGrid.viewGrid());
  }

  public void pause() {
    animation.stop();
  }

  public void start() {
    animation.play();
  }

  public void next() {
    animation.stop();
    myGrid.updateCells();
    mySC.updateGrid(myGrid.viewGrid());
  }

  public void fast() {
    animation.stop();
    framecount += 1;
    if (framecount < 0) {
      framecount = 1;
    }
    animation.setRate(framecount);
    animation.play();
  }

  public void slow() {
    animation.stop();
    framecount -= 1;
    if (framecount > 0) {
      animation.setRate(framecount);
    } else if (framecount == 0 || framecount == -1) {
      animation.setRate(0.8);
    } else {
      animation.setRate(1.0 / (framecount * -1));
    }
    animation.play();
  }
}
