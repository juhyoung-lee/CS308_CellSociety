package cellsociety.configuration;

import java.util.List;
import java.util.Map;

public class Simulation {

  public static final List<String> INFORMATION_FIELDS = List.of(
      "type", "title", "author", "description");
  public static final List<String> GRID_FIELDS = List.of("width", "height");

  private String configFile;

  private String myType;
  private String myTitle;
  private String myAuthor;
  private String myDescription;
  private Map<String, Integer> myParameters;

  private List<String> myCellRows;

  public Simulation(List<String> infoValues, Map<String, Integer> paramValues,
      List<String> cellValues) {
    myType = infoValues.get(0);
    myTitle = infoValues.get(1);
    myAuthor = infoValues.get(2);
    myDescription = infoValues.get(3);
    myParameters = paramValues;
    myCellRows = cellValues;
  }

  public List<String> getCellRows(){
    return myCellRows;
  }

  public int getWidth(){return myParameters.get("width");}
  public int getHeight(){return myParameters.get("height");}
  public String getTitle(){return myTitle;}
  public String getType(){return myType;}
  public Map<String, Integer> getParameters() {
    return myParameters;
  }

  public String toString() {
    return "Type: " + myType + ", Title: " + myTitle + ", Author: " + myAuthor + ", Descr: "
        + myDescription + "\n" +
        "Parameters: " + myParameters + "\n" +
        "Cell Rows: " + myCellRows;
  }
}
