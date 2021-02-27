package cellsociety.view;

import cellsociety.Control;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

import java.util.ResourceBundle;

public class ResourceStyleBuilder {
  private Control myControl;
  private ResourceBundle myResources;
  private Scene myScene;
  private HBox myBox;
  private String myStyleSheet;
  private ButtonBuilder myButtons;

  public ResourceStyleBuilder(Control control, ResourceBundle resourceBundle, Scene scene, String styleSheet, ButtonBuilder buttons) {
    myControl = control;
    myResources = resourceBundle;
    myScene = scene;
    myStyleSheet = styleSheet;
    myButtons = buttons;
    myBox = new HBox();
    myBox.setSpacing(5.0);
    createResourceButton();
    createStyleButton();
  }

  private void createStyleButton() {
    ComboBox<String> styleButton = new ComboBox();
    styleButton.getItems().addAll(
            "dark",
            "light",
            "default"
    );
    styleButton.setValue("default");
    styleButton.setLayoutX(100);
    myBox.getChildren().add(styleButton);
    styleButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            myControl.pause();
            myScene.getStylesheets().remove(myStyleSheet);
            myStyleSheet = "cellsociety/view/resources/" + styleButton.getValue() + ".css";
            myScene.getStylesheets().add(myStyleSheet);
        }
    });
  }

  private void createResourceButton() {
    ComboBox<String> resourceButton = new ComboBox();
    resourceButton.getItems().addAll(
            "English",
            "Chinese",
            "Spanish"
    );
    resourceButton.setValue("English");
    myBox.getChildren().add(resourceButton);
    resourceButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            myControl.pause();
            myResources = ResourceBundle.getBundle("cellsociety.view.resources." + resourceButton.getValue());
            myButtons.resetButtons(myResources);
        }
    });
  }

  public HBox getHBox() {
    return myBox;
  }
}
