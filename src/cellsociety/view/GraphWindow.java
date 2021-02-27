package cellsociety.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class GraphWindow extends NewWindow {
  private PieChart myGraph;
  private ObservableList<PieChart.Data> myData;
  private String myStyleSheet;

  public GraphWindow(String styleSheet, String displayTitle, String windowTitle, int size, ScreenControl screenControl) {
    super(styleSheet, displayTitle, windowTitle, size, screenControl);
    myStyleSheet = styleSheet;
    myGraph = new PieChart();
    myGraph.setAnimated(false);
    myData = FXCollections.observableArrayList();
    myWindow.getChildren().add(myGraph);
  }

  public void updateGraph(List<Integer> cells, ResourceBundle resourceBundle, String type) {
    myData.clear();
    List<Integer> sortedList = new ArrayList<>(cells);
    Collections.sort(sortedList);
    int max = sortedList.get(cells.size() - 1);
    for (int i = 0; i <= max; i++) {
        int num = Collections.frequency(cells, i);
        myData.add(new PieChart.Data(resourceBundle.getString(type + i), num));
    }
    myGraph.setData(myData);
  }
}
