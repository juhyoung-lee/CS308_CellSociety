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

/**
 * Purpose: Simulation class stores all of the info for the XML and allows the model to access it.
 * Assumptions: Assumes it is parsing a file for this project.
 * Dependencies: Depends on the imported packages.
 * Example of use: Simulation mySim = new Simulation(data/myXML);
 */
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

  /**
   * Purpose: Constructor for Simulation.
   *
   * @param fileString the path of the file that the XML is going to parse from
   * @throws XMLException when one of the methods called has problem with XML.  Handled in Control
   */
  public Simulation(String fileString) throws XMLException {
    File dataFile = new File(fileString);

    DOCUMENT_BUILDER = getDocumentBuilder();
    Element root = getRootElement(dataFile);
    myParameters = makeParameterMap(dataFile);
    myCellRows = makeCellRowList(root);
    checkCellDimensions();
    myInformation = makeInfoMap(dataFile);
    checkInformation();
  }

  /**
   * Purpose: Checks to make sure the required fields are given in the information section of XML
   * Assumptions: Assumes myInformation is already filled with everything in XML
   *
   * @throws XMLException if missing a field, or if it is not a shape or or grid type given is not a
   *                      possible type
   */
  private void checkInformation() throws XMLException {
    for (String s : REQUIRED_INFORMATION) {
      if (myInformation.get(s) == null) {
        throw new XMLException("Missing Field: " + s);
      }
    }
    checkShape();
    checkGrid();
  }

  private void checkShape() {
    boolean isValidShape = false;
    String tempShape = myInformation.get(SHAPE);
    for (String s : SHAPE_OPTIONS) {
      if (tempShape != null && tempShape.equals(s)) {
        isValidShape = true;
        break;
      }
    }
    if (!isValidShape) {
      myInformation.put(SHAPE, DEFAULT_SHAPE);
    }
  }

  private void checkGrid() {
    boolean isValidGrid = false;
    String tempGrid = myInformation.get(GRID_TYPE);
    for (String s : GRID_OPTIONS) {
      if (tempGrid != null && tempGrid.equals(s)) {
        isValidGrid = true;
        break;
      }
    }
    if (!isValidGrid) {
      myInformation.putIfAbsent(GRID_TYPE, DEFAULT_GRID);
    }
  }


  private void checkCellDimensions() throws XMLException {
    if (myParameters.get(HEIGHT) == null) {
      throw new XMLException("Missing Field: Height");
    }
    if (myCellRows.size() != getHeight()) {
      throw new XMLException("Invalid Number of Cell Rows");
    }
    if (myParameters.get(WIDTH) == null) {
      throw new XMLException("Missing Field: Width");
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


  private DocumentBuilder getDocumentBuilder() throws XMLException {
    try {
      return DocumentBuilderFactory.newInstance().newDocumentBuilder();
    } catch (Exception e) {
      throw new XMLException("DocumentBuilderException");
    }
  }

  private Element getRootElement(File xmlFile) throws XMLException {
    try {
      DOCUMENT_BUILDER.reset();
      Document xmlDocument = DOCUMENT_BUILDER.parse(xmlFile);
      return xmlDocument.getDocumentElement();
    } catch (Exception e) {
      throw new XMLException("Not XML File or Empty XML");
    }
  }

  /**
   * Purpose: to return myCellRows in a safe way
   *
   * @return List<String>: a copy of myCellRows in a safe way
   */
  public List<String> getCellRows() {
    List<String> returned = new ArrayList();
    returned.addAll(myCellRows);
    return returned;
  }

  /**
   * Purpose: To return the parameter map in a safe way
   *
   * @return Map<String, Integer>: a copy of myParameters
   */
  public Map<String, Integer> getParameters() {
    Map<String, Integer> returned = new HashMap<>();
    returned.putAll(myParameters);
    return returned;
  }

  /**
   * Purpose: To return the Info map in a safe way
   *
   * @return Map<String, String>: a copy of myInformation
   */
  public Map<String, String> getInfoMap() {
    Map<String, String> returned = new HashMap<>();
    returned.putAll(myInformation);
    return returned;
  }

  /**
   * Purpose: To return the grid parameters, in the specific form, which is what the grid needs.
   *
   * @return String[] of size 2 with the shape in the first spot and grid type in the second
   */
  public String[] getGridParameterArray() {
    String[] returned = new String[2];
    returned[0] = myInformation.get(SHAPE);
    returned[1] = myInformation.get(GRID_TYPE);
    return returned;
  }

  /**
   * Purpose: get the initial width of grid
   *
   * @return int: the value associated with width key in myParameters
   */
  public int getWidth() {
    return myParameters.get(WIDTH);
  }

  /**
   * Purpose: get the initial height of the grid
   *
   * @return int: the value associated with height key in myParameters
   */
  public int getHeight() {
    return myParameters.get(HEIGHT);
  }

  /**
   * Purpose: get the title of the grid
   *
   * @return String: the value associated with title key in myParameters
   */
  public String getTitle() {
    return myInformation.get("title");
  }

  /**
   * Purpose: get the type of  grid
   *
   * @return String: the value associated with type key in myParameters
   */
  public String getType() {
    return myInformation.get("type");
  }
}
