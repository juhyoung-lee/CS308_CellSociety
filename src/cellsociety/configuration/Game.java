package cellsociety.configuration;

import java.util.ArrayList;
import java.util.List;

public class Game {

  public static final List<String> INFORMATION_FIELDS = List.of(
      "type", "title", "author", "description");
  public static final List<String> GRID_FIELDS = List.of("width", "height");

  private String myType;
  private String myTitle;
  private String myAuthor;
  private String myDescription;
  private int myWidth;
  private int myHeight;
  private ArrayList<Integer> myParameters;
  private ArrayList<String> myCellRows;

  public Game(String type, String title, String author, String description,
      int width, int height, ArrayList<Integer> parameters, ArrayList<String> cellRows) {
    myType = type;
    myTitle = title;
    myAuthor = author;
    myDescription = description;
    myWidth = width;
    myHeight = height;
    myParameters = parameters;
    myCellRows = cellRows;
  }

  public Game(ArrayList<String> infoValues, ArrayList<Integer> gridValues,
      ArrayList<Integer> paramValues, ArrayList<String> cellValues) {
    this(infoValues.get(0), infoValues.get(1), infoValues.get(2), infoValues.get(3),
        gridValues.get(0), gridValues.get(1), paramValues, cellValues);
  }

  public ArrayList<String> getCellRows(){
    return myCellRows;
  }

  public int getWidth(){return myWidth;}
  public int getHeight(){return myHeight;}
  public String getTitle(){return myTitle;}
  public String getType(){return myType;}

  public String toString() {
    return "Type: " + myType + ", Title: " + myTitle + ", Author: " + myAuthor + ", Descr: "
        + myDescription + "\n" +
        "Width: " + myWidth + " Height: " + myHeight + "\n" +
        "Parameters: " + myParameters + "\n" +
        "Cell Rows: " + myCellRows;
  }
}
