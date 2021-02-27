package cellsociety.view;

import cellsociety.Control;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Purpose: Creates buttons for the view.
 * Assumptions: None
 * Dependencies: cellsociety.Control, javafx.geometry.Pos, javafx.scene.control.Button,
 *               javafx.scene.layout.VBox, java.utils.ResourceBundle
 * Example of use: ButtonBuilder button = new ButtonBuilder(control, rBundle, sc)
 *
 * @author Kathleen Chen
 */

public class ButtonBuilder {
  private Control myControl;
  private ResourceBundle myResources;
  private ScreenControl mySC;
  private VBox myBox;


  /**
   * Purpose: Constructor of ButtonBuilder.
   * Assumptions: rBundle is not empty
   * Parameters: Control control, ResourceBundle rBundle, ScreenControl sc
   * Exception: None
   */
  public ButtonBuilder(Control control, ResourceBundle resourceBundle, ScreenControl sc) {
    myControl = control;
    mySC = sc;
    myResources = resourceBundle;
    myBox = new VBox();
    myBox.setAlignment(Pos.CENTER_LEFT);
    myBox.setSpacing(5.0);
    createButtons();
  }

  /**
   * Purpose: Creates each button and sets its actions when it is pressed.
   * Assumptions: rBundle has all of the keys for each button
   * Parameters: None
   * Exception: None
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
   * Purpose: Returns the VBox that holds all the buttons
   *          so that buttons can be displayed on a main Pane.
   * Assumptions: None
   * Parameters: None
   * Exception: None
   * Returns: VBox
   */
  public VBox getBox() {
    return myBox;
  }

  /**
   * Purpose: Removes all the current buttons from the VBox
   *          and recreates buttons to match the correct .properties file.
   * Assumptions: resourceBundle is not empty
   * Parameters: ResourceBundle resourceBundle
   * Exception: None
   */
  public void resetButtons(ResourceBundle resourceBundle) {
    myResources = resourceBundle;
    myBox.getChildren().removeAll(myBox.getChildren());
    createButtons();
  }
}
