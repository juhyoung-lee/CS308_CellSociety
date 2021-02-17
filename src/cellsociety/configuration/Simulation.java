package cellsociety.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Simulation {

  public static final List<String> INFORMATION_FIELDS = List.of(
      "type", "title", "author", "description");
  public static final List<String> GRID_FIELDS = List.of("width", "height");

  private String myType;
  private String myTitle;
  private String myAuthor;
  private String myDescription;
  private HashMap<String, Integer> myParameters;
  private ArrayList<String> myCellRows;

  public Simulation(String type, String title, String author, String description,
  HashMap<String, Integer> parameters, ArrayList<String> cellRows) {
    myType = type;
    myTitle = title;
    myAuthor = author;
    myDescription = description;
    myParameters = parameters;
    myCellRows = cellRows;
  }

  public Simulation(ArrayList<String> infoValues, HashMap<String, Integer> paramValues,
      ArrayList<String> cellValues) {
    this(infoValues.get(0), infoValues.get(1), infoValues.get(2), infoValues.get(3),
        paramValues, cellValues);
  }

  public ArrayList<String> getCellRows(){
    return myCellRows;
  }

  public int getWidth(){return myParameters.get("width");}
  public int getHeight(){return myParameters.get("height");}
  public String getTitle(){return myTitle;}
  public String getType(){return myType;}
  public HashMap<String, Integer> getParameters() {
    return myParameters;
  }

  public String toString() {
    return "Type: " + myType + ", Title: " + myTitle + ", Author: " + myAuthor + ", Descr: "
        + myDescription + "\n" +
        "Parameters: " + myParameters + "\n" +
        "Cell Rows: " + myCellRows;
  }
}
