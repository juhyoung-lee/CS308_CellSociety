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
  private SimulationControl sim = new SimulationControl();

  public static void main(String[] args){
    launch(args);
  }

  /**
   * Start the program by initializing the simulation.
   ** @param stage
  */
  @Override
  public void start(Stage stage) {
    sim.initialize(stage);
  }
}
