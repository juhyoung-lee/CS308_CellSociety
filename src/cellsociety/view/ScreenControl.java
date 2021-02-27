package cellsociety.view;

import cellsociety.Control;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * Purpose: Creates screen display that user interacts with.
 * Assumptions: Values passed in are valid;
 *              All information passed to ScreenControl from the grid and xml parsing are valid
 *              (does not check for exceptions in front end stuff)
 * Dependencies: cellsociety.Control, java.util.ArrayList, java.util.Collections, java.util.HashMap,
 *               java.util.List, java.util.Map, java.util.ResourceBundle, javafx.collections.FXCollections,
 *               javafx.collections.ObservableList, javafx.event.ActionEvent, javafx.event.EventHandler;
 *               javafx.scene.Scene, javafx.scene.chart.PieChart, javafx.scene.control.Button,
 *               javafx.scene.control.ComboBox, javafx.scene.control.TextField, javafx.scene.layout.*,
 *               javafx.scene.text.Font, javafx.scene.text.Text, javafx.stage.Stage
 * Example of use: mySC = new ScreenControl(Control control)
 *
 * @author Kathleen Chen, Jessica Yang
 */


public class ScreenControl {
  public static final int SCREEN_X = 700;
  public static final int SCREEN_Y = 600;

  private BorderPane myRoot;
  private Text titleText;
  private Scene myScene;
  private Control sim;
  private ResourceBundle myResources;
  private String myStyleSheet;
  private RectangleGrid myRectGrid;
  private HexagonGrid myHexGrid;
  private TriangleGrid myTriGrid;
  private String myType;
  private Map<String, Integer> paramsMap;
  private ButtonBuilder myButtons;
  private AnchorPane myGridBox;
  private ErrorMessage myError;
  private ResourceStyleBuilder myResourceStyle;
  private GraphWindow myGraph;
  private EditWindow myEditor;

  /**
   * Purpose: Constructor of ScreenControl.
   * Assumptions: None
   * Parameters: Control simulationControl
   * ExcptionsL None
   */
  public ScreenControl(Control simulationControl) {
    sim = simulationControl;
    myRoot = new BorderPane();
    myResources = ResourceBundle.getBundle("cellsociety.view.resources.English");
    myStyleSheet = "cellsociety/view/resources/default.css";
    myScene = new Scene(myRoot, SCREEN_X, SCREEN_Y);
    myScene.getStylesheets().add(myStyleSheet);
    myButtons = new ButtonBuilder(simulationControl, myResources, this);
    myRoot.setLeft(myButtons.getBox());
    myResourceStyle = new ResourceStyleBuilder(sim, myResources, myScene, myStyleSheet, myButtons);
    myRoot.setTop(myResourceStyle.getHBox());
    myGridBox = new AnchorPane();
    myRoot.setCenter(myGridBox);
  }

  /**
   * Purpose: Construct Graph Window.
   * Assumptions: None
   * Parameters: None
   * Exceptions: None
   */
  public void makeNewGraphWindow() {
    myGraph = new GraphWindow("cellsociety/view/resources/graph.css", myType + ": Display States", "Graph", Control.X_SIZE, this);
  }

  /**
   * Purpose: Create window for editing parameters.
   * Assumptions: Called after setParams().
   * Parameters: None.
   * Exceptions: None.
   * Returns: None.
   */
  public void makeNewEditWindow() {
    myEditor = new EditWindow(myStyleSheet, "Edit Parameters", "Parameter Editor", Control.X_SIZE, this);

    Button changeButton = myEditor.buttonCreation("Make Changes", Control.X_SIZE / 2, 200);
    changeButton.setOnAction(event -> sim.updateParams(myEditor.getData(myEditor.getAllTextFields())));
  }


  private void resetGameTitleText() {
    myGridBox.getChildren().remove(titleText);
  }

  /**
   * Purpose: Create grid on display screen based on the shape
   * Assumptions: None
   * Parameters: String title, String type, int rows, int cols, List<Integer> cells, String shape
   * Exceptions: None
   */
  public void createGrid(String title, String type, int rows, int cols, List<Integer> cells, String shape) {
    myType = type;
    myType = myType.replaceAll("\\s", "");
    if (shape.equals("square")) {
      createRecGrid(title, type, rows, cols, cells);
    } else if (shape.equals("triangle")) {
      createTriGrid(title, type, rows, cols, cells);
    } else {
      createHexGrid(title, type, rows, cols, cells);
    }
    if (myGraph != null) {
      myGraph.updateGraph(cells, myResources, myType);
    }
  }

  private void createRecGrid(String title, String type, int rows, int cols, List<Integer> cells) {
    myRectGrid = new RectangleGrid(myStyleSheet, myScene, myGridBox);
    myRectGrid.createGrid(title, type, rows, cols, cells);
    titleText = myRectGrid.getTitleText();
  }

  private void createTriGrid(String title, String type, int rows, int cols, List<Integer> cells) {
    myTriGrid = new TriangleGrid(myStyleSheet, myScene, myGridBox);
    myTriGrid.createGrid(title, type, rows, cols, cells);
    titleText = myTriGrid.getTitleText();
  }

  private void createHexGrid(String title, String type, int rows, int cols, List<Integer> cells) {
    myHexGrid = new HexagonGrid(myStyleSheet, myScene, myGridBox);
    myHexGrid.createGrid(title, type, rows, cols, cells);
    titleText = myHexGrid.getTitleText();
  }

  /**
   * Purpose: Update the display of Grid based on shape.
   * Assumptions: None
   * Parameters: List cells, int row, int col
   * Exceptions: None
   */
  public void updateGrid(List<Integer> cells, int row, int col) {
    if (myRectGrid != null) {
      myRectGrid.updateGrid(cells, row, col);
    } else if (myTriGrid != null) {
      myTriGrid.updateGrid(cells, row, col);
    } else if (myHexGrid != null) {
      myHexGrid.updateGrid(cells, row, col);
    }
    if (myGraph != null) {
      myGraph.updateGraph(cells, myResources, myType);
    }
  }

  /**
   * Purpose: Clears the display of grid.
   * Assumptions: None
   * Parameters: None
   * Exceptions: None
   */
  public void clearGrid() {
    if (myRectGrid != null) {
      myRectGrid.clearGrid();
    }
    if (myTriGrid != null) {
      myTriGrid.clearGrid();
    }
    if (myHexGrid != null) {
      myHexGrid.clearGrid();
    }
    resetGameTitleText();
  }

  /**
   * Purpose: Return the scene.
   * Assumptions: None
   * Parameters: None
   * Exceptions: None
   * Return: Scene
   */
  public Scene getScene() {
    return myScene;
  }

  /**
   * Purpose: Takes in parameters being used by Control.
   * Assumptions: None.
   * Parameters: Map params.
   * Exceptions: None.
   * Returns: None.
   */
  public void setParams(Map<String, Integer> params) {
    paramsMap = params;
  }

  public Map<String, Integer> getParamsMap() {
    return paramsMap;
  }

  /**
   * Purpose: Display error message on scene.
   * Assumptions: None
   * Parameters: String message
   * Exceptions: None
   */
  public void displayErrorMessage(String message) {
    myError = new ErrorMessage(message);
    myRoot.setBottom(myError.getBox());
    sim.uploadFile();
  }

  /**
   * Purpose: Clear error message when resolved.
   * Assumptions: None
   * Parameters: None
   * Exceptions: None
   */
  public void clearError() {
    if (myError != null) {
      myRoot.getChildren().remove(myError.getBox());
    }
  }
}