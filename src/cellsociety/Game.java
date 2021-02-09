package cellsociety;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.util.Duration;


public class Game {
  public static final String TITLE = "Cell Society";
  public static final int X_SIZE = 500;
  public static final int Y_SIZE = 600;
  public static final Paint BACKGROUND = Color.AZURE;

  public static final String GAME_TITLE = "Conway's Game of Life";

  private Group myRoot;
  private Scene myScene;
  private Text titleText;

  public void initialize(Stage stage) {
    myRoot = new Group();
    myScene = new Scene(myRoot, X_SIZE, Y_SIZE, BACKGROUND);
    stage.setScene(myScene);
    stage.setTitle(TITLE);
    stage.show();
    /**
     * temporary place holder for grid cells
    */
    Rectangle framePlaceholder = new Rectangle();
    framePlaceholder.setWidth(450);
    framePlaceholder.setHeight(450);
    framePlaceholder.setX(X_SIZE / 2 - (framePlaceholder.getWidth() / 2));
    framePlaceholder.setY(Y_SIZE / 12);
    myRoot.getChildren().add(framePlaceholder);
    setGameTitleText();
    createPlayButton();
  }

  private void createPlayButton() {
    Button play = new Button("Play");
    play.setLayoutX(X_SIZE / 12);
    play.setLayoutY(Y_SIZE / 12 + 460);
    myRoot.getChildren().add(play);
  }

    private void setGameTitleText() {
    titleText = new Text(0, 30, GAME_TITLE);
    Font font = new Font(30);
    titleText.setFont(font);
    titleText.setX(X_SIZE / 2 - (titleText.getLayoutBounds().getWidth() / 2));
    myRoot.getChildren().add(titleText);
  }
}
