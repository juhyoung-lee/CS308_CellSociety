package cellsociety.view;

import cellsociety.Control;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Purpose: Creates screen display that user interacts with.
 * Assumptions: Values passed in are valid;
 *              All information passed to ScreenControl from the grid and xml parsing are valid
 *              (does not check for exceptions in front end stuff)
 * Dependencies: cellsociety.Control, java.util.ArrayList, java.util.Collections, java.util.HashMap,
 *               java.util.List, java.util.Map, java.util.ResourceBundle,
 *               javafx.collections.FXCollections, javafx.collections.ObservableList,
 *               javafx.event.ActionEvent, javafx.event.EventHandler, javafx.scene.Scene,
 *               javafx.scene.chart.PieChart, javafx.scene.control.Button,
 *               javafx.scene.control.ComboBox, javafx.scene.control.TextField,
 *               javafx.scene.layout.*, javafx.scene.text.Font,
 *               javafx.scene.text.Text, javafx.stage.Stage
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
  private PieChart myGraph;
  private String myType;
  private ObservableList<PieChart.Data> myData;
  private Map<String, Integer> paramsMap;
  private ButtonBuilder myButtons;
  private HBox myResourceStyle;
  private AnchorPane myGridBox;
  private ErrorMessage myError;

  /**
   * Purpose: Constructor of ScreenControl.
   * Assumptions: None
   * Parameters: Control simulationControl
   * ExcptionsL None
   */
  public ScreenControl(Control simulationControl) {
    sim = simulationControl;
    myRoot = new BorderPane();
    myResourceStyle = new HBox();
    myResourceStyle.setSpacing(5.0);
    myRoot.setTop(myResourceStyle);
    createResourceButton();
    createStyleButton();
    myResources = ResourceBundle.getBundle("cellsociety.view.resources.English");
    myStyleSheet = "cellsociety/view/resources/default.css";
    myScene = new Scene(myRoot, SCREEN_X, SCREEN_Y);
    myScene.getStylesheets().add(myStyleSheet);
    myButtons = new ButtonBuilder(simulationControl, myResources, this);
    VBox box = myButtons.getBox();
    myRoot.setLeft(box);
    myGridBox = new AnchorPane();
    myRoot.setCenter(myGridBox);
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
    myResourceStyle.getChildren().add(styleButton);
    styleButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        sim.pause();
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
    myResourceStyle.getChildren().add(resourceButton);
    resourceButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        sim.pause();
        myResources = ResourceBundle.getBundle("cellsociety.view.resources." + resourceButton.getValue());
        myButtons.resetButtons(myResources);
      }
    });
  }


  private Button buttonCreation(String text, double x, double y, Pane pane) {
    Button button = new Button(text);
    button.setLayoutX(x);
    button.setLayoutY(y);
    pane.getChildren().add(button);
    button.getStyleClass().add("button");
    return button;
  }

  /**
   * Purpose: Construct Graph Window.
   * Assumptions: None
   * Parameters: None
   * Exceptions: None
   */
  public void makeNewGraphWindow() {
    int size = Control.X_SIZE;

    Pane secondaryLayout = new Pane();
    Scene secondScene = new Scene(secondaryLayout, size, size);
    String graphStyle = "cellsociety/view/resources/graph.css";
    secondScene.getStylesheets().add(graphStyle);

    myWindowTitle(Control.X_SIZE, secondaryLayout, "Graph of states");

    myGraph = new PieChart();
    myGraph.setAnimated(false);
    myData = FXCollections.observableArrayList();
    secondaryLayout.getChildren().add(myGraph);

    Stage newWindow = new Stage();
    newWindow.setTitle("Graph");
    newWindow.setScene(secondScene);
    newWindow.show();
  }

  /**
   * Purpose: Create window for editing parameters.
   * Assumptions: Called after setParams().
   * Parameters: None.
   * Exceptions: None.
   * Returns: None.
   */
  public void makeNewEditWindow() {
    Pane tertiaryLayout = new Pane();
    Scene thirdScene = new Scene(tertiaryLayout, Control.X_SIZE, Control.X_SIZE);
    thirdScene.getStylesheets().add(myStyleSheet);

    myWindowTitle(Control.X_SIZE, tertiaryLayout, "Edit Parameters");

    List<TextField> allTextFields = setAllTextFields(tertiaryLayout);

    Button changeButton = buttonCreation("Make Changes", Control.X_SIZE / 2, 200, tertiaryLayout); //TODO: use resource bundle?
    changeButton.setOnAction(event -> sim.updateParams(getData(allTextFields)));

    Stage newEditWindow = new Stage();
    newEditWindow.setTitle("Edit Parameters");
    newEditWindow.setScene(thirdScene);
    newEditWindow.show();
  }

  private List<TextField> setAllTextFields(Pane pane) {
    List<TextField> textFields = new ArrayList<>();

    int i = 0;
    for (String key : paramsMap.keySet()) {
      if (!((key.equals("width") || key.equals("neighborhoodSize") || key.equals("state")
          || key.equals("height")))) {
        textFields.add(textFieldCreation(key, 0,
            25 + i * (Control.X_SIZE / paramsMap.size()), pane));
        i++;
      }
    }

    return textFields;
  }

  private Map<String, Integer> getData(List<TextField> allTextFields) {
    Map<String, Integer> newParams = new HashMap<>();

    for (TextField textField : allTextFields) {
      if (!textField.getText().equals("")) {
        newParams.put(textField.getPromptText(), Integer.parseInt(textField.getText()));
      }
    }

    return newParams;
  }

  private void myWindowTitle(int size, Pane secondaryLayout, String text) {
    Text title = new Text(0, 30, text);
    title.setFont(new Font(25));
    title.setX(size / 2 - (title.getLayoutBounds().getWidth() / 2));
    title.setY(20);
    title.getStyleClass().add("title");
    secondaryLayout.getChildren().add(title);
  }

  private TextField textFieldCreation(String prompt, double x, double y, Pane pane) {
    TextField textField = new TextField();
    textField.setPromptText(prompt);
    textField.setLayoutX(x);
    textField.setLayoutY(y);
    pane.getChildren().add(textField);
    return textField;
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
      updateGraph(cells);
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

  private void updateGraph(List<Integer> cells) {
    myData.clear();
    List<Integer> sortedlist = new ArrayList<>(cells);
    Collections.sort(sortedlist);
    int max = sortedlist.get(cells.size() - 1);
    for (int i = 0; i <= max; i++) {
      int num = Collections.frequency(cells, i);
      myData.add(new PieChart.Data(myResources.getString(myType + i), num));
    }
    myGraph.setData(myData);
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
      updateGraph(cells);
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