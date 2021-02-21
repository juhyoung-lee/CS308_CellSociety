package cellsociety.view;

import cellsociety.Control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
 * @author Kathleen Chen
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
    startButton = buttonCreation(myResources.getString("PlayButton"), sX / 12, sY / 12 + 460);
    startButton.setOnAction(event -> sim.start());
    pauseButton = buttonCreation(myResources.getString("PauseButton"), sX / 2 - 32, sY / 12 + 460);
    pauseButton.setOnAction(event -> sim.pause());
    stepButton = buttonCreation(myResources.getString("StepButton"), sX * 4 / 5, sY / 12 + 460);
    stepButton.setOnAction(event -> sim.next());
    fastButton = buttonCreation(myResources.getString("SpeedUpButton"), sX * 3 / 5, sY / 12 + 500);
    fastButton.setOnAction(event -> sim.fast());
    slowButton = buttonCreation(myResources.getString("SlowDownButton"), sX * 2 / 9, sY / 12 + 500);
    slowButton.setOnAction(event -> sim.slow());
    uploadButton = buttonCreation(myResources.getString("UploadButton"), sX - 120, 0);
    uploadButton.setOnAction(event -> sim.uploadFile());
    graphButton = buttonCreation(myResources.getString("GraphButton"), sX - 220, 0);
    graphButton.setOnAction(event -> makeNewWindow());
  }

  private void makeNewWindow() {
    int size = Control.X_SIZE;
    Pane secondaryLayout = new Pane();
    Scene secondScene = new Scene(secondaryLayout, size, size);
    String graphStyle = "cellsociety/view/resources/graph.css";
    secondScene.getStylesheets().add(graphStyle);

    myWindowTitle(size, secondaryLayout);

    myGraph = new PieChart();
    secondaryLayout.getChildren().add(myGraph);

    Stage newWindow = new Stage();
    newWindow.setTitle("Graph");
    newWindow.setScene(secondScene);
    newWindow.show();
  }

  private void myWindowTitle(int size, Pane secondaryLayout) {
    Text title = new Text(0, 30, "Graph of states");
    title.setFont(new Font(25));
    title.setX(size / 2 - (title.getLayoutBounds().getWidth() / 2));
    title.setY(20);
    title.getStyleClass().add("title");
    secondaryLayout.getChildren().add(title);
  }

  private Button buttonCreation(String text, double x, double y) {
    Button button = new Button(text);
    button.setLayoutX(x);
    button.setLayoutY(y);
    myRoot.getChildren().add(button);
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
  public void createRectGrid(String title, String type, int rows, int cols, List<Integer> cells) {
    myRectGrid = new RectangleGrid(myStyleSheet, myScene, myRoot);
    myRectGrid.createGrid(title, type, rows, cols, cells);
    titleText = myRectGrid.getTitleText();
    if (myGraph != null) {
      updateGraph(cells);
    }
  }



  public void createTriGrid(String title, String type, int rows, int cols, List<Integer> cells) {
    myTriGrid = new TriangleGrid(myStyleSheet, myScene, myRoot);
    myTriGrid.createGrid(title, type, rows, cols, cells);
    titleText = myTriGrid.getTitleText();
  }

  /**
   * Purpose: Creates grid of hexagons.
   * Assumptions: TODO
   * Parameters: String title, String type, int rows, int cols, List cells.
   * Exceptions: TODO
   * Returns: None.
   */
  public void createHexGrid(String title, String type, int rows, int cols, List<Integer> cells) {
    myHexGrid = new HexagonGrid(myStyleSheet, myScene, myRoot);
    myHexGrid.createGrid(title, type, rows, cols, cells);
    titleText = myHexGrid.getTitleText();
  }

  private void updateGraph(List<Integer> cells) {
    List<Integer> sortedlist = new ArrayList<>(cells);
    Collections.sort(sortedlist);
    int max = sortedlist.get(cells.size() - 1);
    System.out.println(max);
    ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
    for (int i = 0; i <= max; i++) {
      int num = Collections.frequency(cells, i);
      data.add(new PieChart.Data(String.valueOf(i), num));
    }
    myGraph.setData(data);
  }
  /**
   * Updates the grid based on new cell information passed in.
   * * @param cells
   */
  public void updateGrid(List<Integer> cells) {
    if (myRectGrid != null) {
      myRectGrid.updateGrid(cells);
    }
    if (myTriGrid != null) {
      myTriGrid.updateGrid(cells);
    }
    if (myGraph != null) {
      updateGraph(cells);
    }
  }

  public void clearGrid() {
    if (myRectGrid != null) {
      myRectGrid.clearGrid();
    }
    if (myTriGrid != null) {
      myTriGrid.clearGrid();
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
}
