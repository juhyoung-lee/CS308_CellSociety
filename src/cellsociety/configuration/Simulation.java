package cellsociety.configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Simulation {

  public static final List<String> REQUIRED_INFORMATION = List.of(
      "type", "title", "author", "description");
  public static final List<String> PARAMETER_INFORMATION = List.of("shape", "gridType");
  private final DocumentBuilder DOCUMENT_BUILDER;

  private Map<String, String> myInformation;
  private Map<String, Integer> myParameters;
  private List<String> myCellRows;

  public Simulation(String fileString) throws XMLException {
    File dataFile = new File(fileString);

    DOCUMENT_BUILDER = getDocumentBuilder();
    Element root = getRootElement(dataFile);
    myParameters = makeParameterMap(dataFile);
    myCellRows = makeCellRowList(root);
    myInformation = makeInfoMap(dataFile);

    checkInformation();
    checkCellDimensions();
  }

  private void checkInformation() throws XMLException {
    for (String s : REQUIRED_INFORMATION) {
      if (myInformation.get(s) == null) {
        throw new XMLException("Missing" + s);
      }
    }
    myInformation.putIfAbsent("shape", "square");
    myInformation.putIfAbsent("gridType", "bounded");
  }

  //TODO: constructor to make XML file?
//  public Simulation() throws XMLException{
//
//  }

  private void checkCellDimensions() throws XMLException {
    if (myParameters.get("height") == null) {
      throw new XMLException("MissingHeight");//turn into resource field
    }
    if (myCellRows.size() != getHeight()) {
      throw new XMLException("InvalidHeight");//turn into resource file
    }
    if (myParameters.get("width") == null) {
      throw new XMLException("MissingWidth");//turn into resource file
    }
    for (String cellRow : myCellRows) {
      if (cellRow.length() != getWidth()) {
        throw new XMLException("InvalidWidth");
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
      return returned;
    } catch (Exception e) {
      throw new XMLException("BadParameters");
    }
  }


  private List<String> makeCellRowList(Element root) throws XMLException {
    List<String> returned = new ArrayList<>();
    NodeList cellRows = root.getElementsByTagName("cellRow");
    int length = cellRows.getLength();
    if (length == 0) {
      throw new XMLException("MissingCellRows");
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
      throw new XMLException("BadInformation");
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
      throw new XMLException("BadXML");
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

  public String getShape() {
    String shape = myInformation.get("shape");
    if (shape == null) {
      shape = "square";
    }
    return shape;
  }

  public int getWidth() {
    return myParameters.get("width");
  }

  public int getHeight() {
    return myParameters.get("height");
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
