package cellsociety;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

/**
 * Purpose: Initialize the screen
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Kathleen Chen
 */

public class Main extends Application {
  private ScreenControl sc = new ScreenControl();

  public static void main(String[] args){
    launch(args);
  }

  /**
   * Start the program by initializing the game
   * @param stage
  */
  @Override
  public void start(Stage stage) {
    int rows = 15;
    int cols = 15;
    ArrayList<Integer> cell = new ArrayList<Integer>(rows * cols);
    Random rand = new Random();
    for (int i = 1; i <= rows * cols; i++) {
      cell.add(rand.nextInt(3));
    }
    sc.initialize(stage);
    sc.createGrid(rows, cols, cell);
  }
}
