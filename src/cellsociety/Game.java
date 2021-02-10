package cellsociety;

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

  public Game(String type, String title, String author, String description,
      int width, int height, ArrayList<Integer> parameters){
    myType = type;
    myTitle = title;
    myAuthor = author;
    myDescription = description;
    myWidth = width;
    myHeight = height;
    myParameters = parameters;
  }

  public Game(ArrayList<String> infoValues, ArrayList<Integer> gridValues,
      ArrayList<Integer> parameterValues){
    this(infoValues.get(0), infoValues.get(1), infoValues.get(2), infoValues.get(3),
        gridValues.get(0), gridValues.get(1), parameterValues);
  }

  public String toString(){
    return "Type: "+myType+" Title: "+myTitle+" Author: "+myAuthor+" Descr: "+myDescription +"\n"+
        "Width: "+myWidth+" Height: "+myHeight;
  }
}
