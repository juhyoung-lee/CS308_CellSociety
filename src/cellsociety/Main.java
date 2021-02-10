package cellsociety;

import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;

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
      Configure c = new Configure();
      //sc.initialize(stage);
  }
}
