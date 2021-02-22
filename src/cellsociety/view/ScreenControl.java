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
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Purpose: Creates screen display that user interacts with.
 * Assumptions: TODO
 * Dependencies: Depends on the grid array passed in to create a grid
 * Example of use: mySC = new ScreenControl() mySC.createGrid(row, col, Grid.viewGrid()) mySC.clearGrid()
 *
 * @author Kathleen Chen, Jessica Yang
 */


public class ScreenControl {
  private Pane myRoot;
  private Text titleText;
  private Button slowButton;
  private Button fastButton;
  private Button startButton;
  private Button pauseButton;
  private Button stepButton;
  private Button uploadButton;
  private Button graphButton;
  private Button editButton;
  private int sX;
  private int sY;
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

  /**
   * Initialize the scene and add buttons and text.
   */
  public ScreenControl(Control simulationControl) {
    myRoot = new Pane();
    sX = Control.X_SIZE;
    sY = Control.Y_SIZE;
    createResourceButton();
    createStyleButton();
    myResources = ResourceBundle.getBundle("cellsociety.view.resources.English");
    myStyleSheet = "cellsociety/view/resources/default.css";
    myScene = new Scene(myRoot, sX, sY);
    myScene.getStylesheets().add(myStyleSheet);
    createButtons();
    sim = simulationControl;
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
    myRoot.getChildren().add(styleButton);
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
    myRoot.getChildren().add(resourceButton);
    resourceButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        sim.pause();
        myResources = ResourceBundle.getBundle("cellsociety.view.resources." + resourceButton.getValue());
        resetButtons();
      }
    });
  }

  private void createButtons() {
    startButton = buttonCreation(myResources.getString("PlayButton"), sX / 12, sY / 12 + 460, myRoot);
    startButton.setOnAction(event -> sim.start());
    pauseButton = buttonCreation(myResources.getString("PauseButton"), sX / 2 - 32, sY / 12 + 460, myRoot);
    pauseButton.setOnAction(event -> sim.pause());
    stepButton = buttonCreation(myResources.getString("StepButton"), sX * 4 / 5, sY / 12 + 460, myRoot);
    stepButton.setOnAction(event -> sim.next());
    fastButton = buttonCreation(myResources.getString("SpeedUpButton"), sX * 3 / 5, sY / 12 + 500, myRoot);
    fastButton.setOnAction(event -> sim.fast());
    slowButton = buttonCreation(myResources.getString("SlowDownButton"), sX * 2 / 9, sY / 12 + 500, myRoot);
    slowButton.setOnAction(event -> sim.slow());
    uploadButton = buttonCreation(myResources.getString("UploadButton"), sX - 120, 0, myRoot);
    uploadButton.setOnAction(event -> sim.uploadFile());
    graphButton = buttonCreation(myResources.getString("GraphButton"), sX - 220, 0, myRoot);
    graphButton.setOnAction(event -> makeNewGraphWindow());
    editButton = buttonCreation("Edit", sX - 300, 0, myRoot); //TODO: use resource bundle
    editButton.setOnAction(event -> makeNewEditWindow());
  }

  private void makeNewGraphWindow() {
    int size = Control.X_SIZE;
    Pane secondaryLayout = new Pane();
    Scene secondScene = new Scene(secondaryLayout, size, size);
    String graphStyle = "cellsociety/view/resources/graph.css";
    secondScene.getStylesheets().add(graphStyle);

    myWindowTitle(size, secondaryLayout, "Graph of states");

    myGraph = new PieChart();
    myGraph.setAnimated(false);
    myData = FXCollections.observableArrayList();
    secondaryLayout.getChildren().add(myGraph);

    Stage newWindow = new Stage();
    newWindow.setTitle("Graph");
    newWindow.setScene(secondScene);
    newWindow.show();
  }

  /** called after setParams() */
  private void makeNewEditWindow() {
    Pane tertiaryLayout = new Pane();
    Scene thirdScene = new Scene(tertiaryLayout, Control.X_SIZE, Control.X_SIZE);
    String editStyle = "cellsociety/view/resources/default.css";  //TODO: use correct css?
    thirdScene.getStylesheets().add(editStyle);

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
      if (key.equals("width") || key.equals("neighborhoodSize") || key.equals("state")
          || key.equals("height")) {
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
    //TODO: styleclass?
    return textField;

  }

  private Button buttonCreation(String text, double x, double y, Pane pane) {
    Button button = new Button(text);
    button.setLayoutX(x);
    button.setLayoutY(y);
    pane.getChildren().add(button);
    button.getStyleClass().add("button");
    return button;
  }

  public void resetGameTitleText() {
    myRoot.getChildren().remove(titleText);
  }

  /**
   * Creates display of Rectangle Grid for viewer to see.
   * * @param rows
   * * @param cols
   * * @param cells
   */
  public void createGrid(String title, String type, int rows, int cols, List<Integer> cells, String shape) {
    myType = type;
    myType = myType.replaceAll("\\s", "");
    if(shape.equals("square")) {
      createRecGrid(title, type, rows, cols, cells);
    } else if (shape.equals("triangle")) {
      createTriGrid(title, type, rows, cols, cells);
    } else {
      createHexGrid(title, type, rows, cols, cells);
    }
    if (myGraph != null) {
      updateGraph(cells, type);
    }
  }


  private void createRecGrid(String title, String type, int rows, int cols, List<Integer> cells) {
    myRectGrid = new RectangleGrid(myStyleSheet, myScene, myRoot);
    myRectGrid.createGrid(title, type, rows, cols, cells);
    titleText = myRectGrid.getTitleText();
  }

  private void createTriGrid(String title, String type, int rows, int cols, List<Integer> cells) {
    myTriGrid = new TriangleGrid(myStyleSheet, myScene, myRoot);
    myTriGrid.createGrid(title, type, rows, cols, cells);
    titleText = myTriGrid.getTitleText();
  }

  private void createHexGrid(String title, String type, int rows, int cols, List<Integer> cells) {
    myHexGrid = new HexagonGrid(myStyleSheet, myScene, myRoot);
    myHexGrid.createGrid(title, type, rows, cols, cells);
    titleText = myHexGrid.getTitleText();
  }

  private void updateGraph(List<Integer> cells, String type) {
    clearGraph();
    List<Integer> sortedlist = new ArrayList<>(cells);
    Collections.sort(sortedlist);
    int max = sortedlist.get(cells.size() - 1);
    for (int i = 0; i <= max; i++) {
      int num = Collections.frequency(cells, i);
      myData.add(new PieChart.Data(myResources.getString(myType + i), num));
    }
    myGraph.setData(myData);
  }

  private void clearGraph() {
    myData.clear();
  }
  /**
   * Updates the grid based on new cell information passed in.
   * * @param cells
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
      updateGraph(cells, myType);
    }
  }

  public void clearGrid() {
    if (myRectGrid != null) {
      myRectGrid.clearGrid();
    } else if (myTriGrid != null) {
      myTriGrid.clearGrid();
    } else if (myHexGrid != null) {
      myHexGrid.clearGrid();
    }
  }

  private void resetButtons() {
    myRoot.getChildren().remove(startButton);
    myRoot.getChildren().remove(pauseButton);
    myRoot.getChildren().remove(fastButton);
    myRoot.getChildren().remove(slowButton);
    myRoot.getChildren().remove(stepButton);
    myRoot.getChildren().remove(uploadButton);
    myRoot.getChildren().remove(graphButton);
    createButtons();
  }

  /**
   * Returns the Scene.
   * * @return Scene
   */
  public Scene getScene() {
    return myScene;
  }

  /**
   * Purpose: Takes in parameters being used by Control.
   * Assumptions: TODO
   * Parameters: Map params.
   * Exceptions: TODO
   * Returns: None.
   */
  public void setParams(Map<String, Integer> params) {
    paramsMap = params;
  }
}

