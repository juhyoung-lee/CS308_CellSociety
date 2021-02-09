package cellsociety;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.util.Duration;

/**
 * Purpose: Creates screen display that user interacts with
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Kathleen Chen
 */


public class ScreenControl {
  public static final String TITLE = "Cell Society";
  public static final int X_SIZE = 500;
  public static final int Y_SIZE = 600;
  public static final Paint BACKGROUND = Color.AZURE;

  public static final String PLAY_IMAGE = "PlayButton.gif";
  public static final String PAUSE_IMAGE = "PauseButton.gif";
  public static final int BUTTON_SIZE = 15;

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
    createPauseButton();
  }

  private void myImage(String icon, String text, double x, double y) {
    Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(icon));
    ImageView playImage = new ImageView(image);
    playImage.setFitWidth(BUTTON_SIZE);
    playImage.setFitHeight(BUTTON_SIZE);
    Button button = new Button(text, playImage);
    button.setLayoutX(x);
    button.setLayoutY(y);
    myRoot.getChildren().add(button);
  }

  private void createPauseButton() {
    int x = X_SIZE / 2 - 35;
    int y = Y_SIZE / 12 + 460;
    String pause = "Pause";
    myImage(PAUSE_IMAGE, pause, x, y);
  }

  private void createPlayButton() {
    int x = X_SIZE / 12;
    int y = Y_SIZE / 12 + 460;
    String play = "Play";
    myImage(PLAY_IMAGE, play, x, y);
  }

    private void setGameTitleText() {
    titleText = new Text(0, 30, GAME_TITLE);
    Font font = new Font(30);
    titleText.setFont(font);
    titleText.setX(X_SIZE / 2 - (titleText.getLayoutBounds().getWidth() / 2));
    myRoot.getChildren().add(titleText);
  }
}
