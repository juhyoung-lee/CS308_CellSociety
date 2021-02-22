package cellsociety.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Simulation {

  public static final List<String> REQUIRED_INFORMATION = List.of(
      "type", "title", "author", "description");
  public static final String SHAPE = "shape";
  public static final String GRID_TYPE = "gridType";
  public static final String HEIGHT = "height";
  public static final String WIDTH = "width";
  public static final List<String> SHAPE_OPTIONS = List.of("square", "hexagon", "triangle");
  public static final List<String> GRID_OPTIONS = List.of("bounded", "infinite", "wrapping");
  public static final String DEFAULT_SHAPE = SHAPE_OPTIONS.get(0);
  public static final String DEFAULT_GRID = GRID_OPTIONS.get(0);
  private final DocumentBuilder DOCUMENT_BUILDER;

  private Map<String, String> myInformation;
  private Map<String, Integer> myParameters;
  private List<String> myCellRows;

  public Simulation(String fileString) throws XMLException {
    File dataFile = new File(fileString);

    DOCUMENT_BUILDER = getDocumentBuilder();
    Element root = getRootElement(dataFile);
    myParameters = makeParameterMap(dataFile);
    //makeCellsFromParameterMap();
    myCellRows = makeCellRowList(root);
    checkCellDimensions();
    myInformation = makeInfoMap(dataFile);
    checkInformation();
  }

  //TODO so that multiple ways to get cells from XML
//  private void makeCellsFromParameterMap() {
//    for(String s : myParameters.keySet()){
//
//    }
//  }

  private void checkInformation() throws XMLException {
    for (String s : REQUIRED_INFORMATION) {
      if (myInformation.get(s) == null) {
        throw new XMLException("Missing Field: " + s);
      }
    }
    boolean isValidShape = false;
    String tempShape = myInformation.get(SHAPE);
    for(String s: SHAPE_OPTIONS) {
      if (tempShape != null && tempShape.equals(s)) {
        isValidShape = true;
        break;
      }
    }
    if(!isValidShape) {
      myInformation.put(SHAPE, DEFAULT_SHAPE);
    }
    boolean isValidGrid = false;
    String tempGrid = myInformation.get(GRID_TYPE);
    for(String s: GRID_OPTIONS) {
      if (tempGrid != null && tempGrid.equals(s)) {
        isValidGrid=true;
        break;
      }
    }
    if(!isValidGrid) {
      myInformation.putIfAbsent(GRID_TYPE, DEFAULT_GRID);
    }
  }


  private void checkCellDimensions() throws XMLException {
    if (myParameters.get(HEIGHT) == null) {
      throw new XMLException("Missing Field: Height");//turn into resource field
    }
    if (myCellRows.size() != getHeight()) {
      throw new XMLException("Invalid Number of Cell Rows");//turn into resource file
    }
    if (myParameters.get(WIDTH) == null) {
      throw new XMLException("Missing Field: Width");//turn into resource file
    }
    for (String cellRow : myCellRows) {
      if (cellRow.length() != getWidth()) {
        throw new XMLException("Invalid Number of Cells in Row");
      }
    }
  }


  private Map<String, Integer> makeParameterMap(File dataFile) throws XMLException {
    try {
      Map<String, Integer> returned = new HashMap<>();
      NodeList nList = DOCUMENT_BUILDER.parse(dataFile).getElementsByTagName("parameters");
      Node node = nList.item(0);
      NodeList list = node.getChildNodes();
      for (int i = 0; i < list.getLength(); i++) {
        Node n = list.item(i);
        if (n.getNodeType() == Node.ELEMENT_NODE) {
          returned.put(n.getNodeName(), Integer.parseInt(n.getTextContent()));
        }
      }
      returned.putIfAbsent("neighborhoodSize", 4);
      return returned;
    } catch (Exception e) {
      throw new XMLException("Missing Parameters Section or Non-int Values");
    }
  }


  private List<String> makeCellRowList(Element root) throws XMLException {
    List<String> returned = new ArrayList<>();
    NodeList cellRows = root.getElementsByTagName("cellRow");
    int length = cellRows.getLength();
    if (length == 0) {
      throw new XMLException("No Cell Rows");
    } else {
      for (int i = 0; i < length; i++) {
        Node temp = cellRows.item(i);
        returned.add(temp.getTextContent());
      }
    }
    return returned;
  }

  private Map<String, String> makeInfoMap(File dataFile) throws XMLException {
    try {
      Map<String, String> returned = new HashMap<>();
      NodeList nList = DOCUMENT_BUILDER.parse(dataFile).getElementsByTagName("information");
      Node node = nList.item(0);
      NodeList list = node.getChildNodes();
      for (int i = 0; i < list.getLength(); i++) {
        Node n = list.item(i);
        if (n.getNodeType() == Node.ELEMENT_NODE) {
          returned.put(n.getNodeName(), n.getTextContent());
        }
      }
      return returned;
    } catch (Exception e) {
      throw new XMLException("Missing Information Section or Non-String Values");
    }
  }

  private String getTextValue(Element e, String tagName) {
    NodeList nodeList = e.getElementsByTagName(tagName);
    if (nodeList != null && nodeList.getLength() > 0) {
      return nodeList.item(0).getTextContent();
    } else {
      return null;
    }
  }

  private DocumentBuilder getDocumentBuilder() throws XMLException {
    try {
      return DocumentBuilderFactory.newInstance().newDocumentBuilder();
    } catch (Exception e) {
      //TODO: fix exceptions
      //not sure what this one does
      throw new XMLException("DocumentBuilderException");
    }
  }

  private Element getRootElement(File xmlFile) throws XMLException {
    try {
      DOCUMENT_BUILDER.reset();
      Document xmlDocument = DOCUMENT_BUILDER.parse(xmlFile);
      return xmlDocument.getDocumentElement();
    } catch (Exception e) {
      //TODO: fix exceptions
      throw new XMLException("Not XML File or Empty XML");
    }
  }

  public List<String> getCellRows() {
    List<String> returned = new ArrayList();
    returned.addAll(myCellRows);
    return returned;
  }

  public Map<String, Integer> getParameters() {
    Map<String, Integer> returned = new HashMap<>();
    returned.putAll(myParameters);
    return returned;
  }

  public Map<String, String> getInfoMap() {
    Map<String, String> returned = new HashMap<>();
    returned.putAll(myInformation);
    return returned;
  }

  public String[] getGridParameterArray() {
    String[] returned = new String[2];
    returned[0] = myInformation.get(SHAPE);
    returned[1] = myInformation.get(GRID_TYPE);
    return returned;
  }

  public int getWidth() {
    return myParameters.get(WIDTH);
  }

  public int getHeight() {
    return myParameters.get(HEIGHT);
  }

  public String getTitle() {
    return myInformation.get("title");
  }

  public String getType() {
    return myInformation.get("type");
  }



//  public String toString() {
//    return "Type: " + myType + ", Title: " + myTitle + ", Author: " + myAuthor + ", Descr: "
//        + myDescription + "\n" +
//        "Parameters: " + myParameters + "\n" +
//        "Cell Rows: " + myCellRows;
//  }
}
