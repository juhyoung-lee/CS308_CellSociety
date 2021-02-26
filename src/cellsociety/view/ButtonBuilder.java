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


  /**
   * Purpose: TODO
   * Assumptions: TODO
   * Parameters: TODO
   * Dependencies: TODO
   * Example of use: TODO
   */
  public ButtonBuilder(Control control, ResourceBundle rBundle, ScreenControl sc) {
    myControl = control;
    mySC = sc;
    myResources = rBundle;
    myBox = new VBox();
    myBox.setAlignment(Pos.CENTER_LEFT);
    myBox.setSpacing(5.0);
    createButtons();
  }

  /**
   * Purpose: TODO
   * Assumptions: TODO
   * Parameters: TODO
   * Dependencies: TODO
   * Example of use: TODO
   */
  public void createButtons() {
    Button startButton = buttonCreation(myResources.getString("PlayButton"));
    startButton.setOnAction(event -> myControl.start());
    Button pauseButton = buttonCreation(myResources.getString("PauseButton"));
    pauseButton.setOnAction(event -> myControl.pause());
    Button stepButton = buttonCreation(myResources.getString("StepButton"));
    stepButton.setOnAction(event -> myControl.next());
    Button fastButton = buttonCreation(myResources.getString("SpeedUpButton"));
    fastButton.setOnAction(event -> myControl.fast());
    Button slowButton = buttonCreation(myResources.getString("SlowDownButton"));
    slowButton.setOnAction(event -> myControl.slow());
    Button uploadButton = buttonCreation(myResources.getString("UploadButton"));
    uploadButton.setOnAction(event -> myControl.uploadFile());
    Button graphButton = buttonCreation(myResources.getString("GraphButton"));
    graphButton.setOnAction(event -> mySC.makeNewGraphWindow());
    Button editButton = buttonCreation(myResources.getString("EditButton"));
    editButton.setOnAction(event -> mySC.makeNewEditWindow());
    Button addButton = buttonCreation(myResources.getString("AddButton"));
    addButton.setOnAction(event -> new Control());
    Button configurationButton = buttonCreation(myResources.getString("ConfigurationButton"));
    configurationButton.setOnAction(event -> myControl.configuration());
  }

  private Button buttonCreation(String text) {
    Button button = new Button(text);
    button.getStyleClass().add("button");
    myBox.getChildren().add(button);
    return button;
  }

  /**
   * Purpose: TODO
   * Assumptions: TODO
   * Parameters: TODO
   * Dependencies: TODO
   * Example of use: TODO
   */
  public VBox getBox() {
    return myBox;
  }

  /**
   * Purpose: TODO
   * Assumptions: TODO
   * Parameters: TODO
   * Dependencies: TODO
   * Example of use: TODO
   */
  public void resetButtons(ResourceBundle rBundle) {
    myResources = rBundle;
    myBox.getChildren().removeAll(myBox.getChildren());
    createButtons();
  }
}
