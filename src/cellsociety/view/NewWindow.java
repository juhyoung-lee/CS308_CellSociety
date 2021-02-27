package cellsociety.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public abstract class NewWindow {
  protected Pane myWindow;
  protected Scene myScene;
  protected ScreenControl mySC;
  public NewWindow(String styleSheet, String displayTitle, String windowTitle, int size, ScreenControl screenControl) {
    myWindow = new Pane();
    myScene = new Scene(myWindow, size, size);
    myScene.getStylesheets().add(styleSheet);
    mySC = screenControl;
    
    myWindowTitle(size, myWindow, displayTitle);

    Stage newWindow = new Stage();
    newWindow.setTitle(windowTitle);
    newWindow.setScene(myScene);
    newWindow.show();
  }

  private void myWindowTitle(int size, Pane window, String displayTitle) {
    Text title = new Text(0, 30, displayTitle);
    title.setFont(new Font(25));
    title.setX((double) (size / 2) - (title.getLayoutBounds().getWidth() / 2));
    title.setY(20);
    title.getStyleClass().add("title");
    window.getChildren().add(title);
  }
}
