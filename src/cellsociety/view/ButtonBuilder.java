package cellsociety.view;

import cellsociety.Control;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ResourceBundle;

/**
 * Purpose: Creates buttons for the view.
 * Assumptions: TODO
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Kathleen Chen
 */

public class ButtonBuilder {
  private Control myControl;
  private ResourceBundle myResources;
  private ScreenControl mySC;
  private VBox myBox;
  private Button slowButton;
  private Button fastButton;
  private Button startButton;
  private Button pauseButton;
  private Button stepButton;
  private Button uploadButton;
  private Button addButton;
  private Button graphButton;
  private Button editButton;
  private Button configurationButton;


  public ButtonBuilder(Control control, ResourceBundle rBundle, ScreenControl sc) {
    myControl = control;
    mySC = sc;
    myResources = rBundle;
    myBox = new VBox();
    myBox.setAlignment(Pos.CENTER_LEFT);
    myBox.setSpacing(5.0);
    createButtons();
  }

  public void createButtons() {
    startButton = buttonCreation(myResources.getString("PlayButton"));
    startButton.setOnAction(event -> myControl.start());
    pauseButton = buttonCreation(myResources.getString("PauseButton"));
    pauseButton.setOnAction(event -> myControl.pause());
    stepButton = buttonCreation(myResources.getString("StepButton"));
    stepButton.setOnAction(event -> myControl.next());
    fastButton = buttonCreation(myResources.getString("SpeedUpButton"));
    fastButton.setOnAction(event -> myControl.fast());
    slowButton = buttonCreation(myResources.getString("SlowDownButton"));
    slowButton.setOnAction(event -> myControl.slow());
    uploadButton = buttonCreation(myResources.getString("UploadButton"));
    uploadButton.setOnAction(event -> myControl.uploadFile());
    graphButton = buttonCreation(myResources.getString("GraphButton"));
    graphButton.setOnAction(event -> mySC.makeNewGraphWindow());
    editButton = buttonCreation(myResources.getString("EditButton"));
    editButton.setOnAction(event -> mySC.makeNewEditWindow());
    addButton = buttonCreation(myResources.getString("AddButton"));
    addButton.setOnAction(event -> new Control());
    configurationButton = buttonCreation(myResources.getString("ConfigurationButton"));
    configurationButton.setOnAction(event -> myControl.configuration());
  }

  private Button buttonCreation(String text) {
    Button button = new Button(text);
    button.getStyleClass().add("button");
    myBox.getChildren().add(button);
    return button;
  }

  public VBox getBox() {
    return myBox;
  }

  public void resetButtons(ResourceBundle rBundle) {
    myResources = rBundle;
    myBox.getChildren().removeAll(myBox.getChildren());
    createButtons();
  }
}
