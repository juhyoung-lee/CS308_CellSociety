package cellsociety;

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

  public static void main(String[] args){
    launch(args);
  }

  /**
   * Start the program by initializing the simulation.
   ** @param stage
  */
  @Override
  public void start(Stage stage) {
   Control control = new Control();
  }
}
