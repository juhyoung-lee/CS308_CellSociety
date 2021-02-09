package cellsociety;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main extends Application {
    private Game game = new Game();

    public static void main(String[] args){
        launch(args);
    }

    /**
     * Start the program by initializing the game
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        game.initialize(stage);
    }
}
