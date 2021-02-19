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

  public static final List<String> INFORMATION_FIELDS = List.of(
      "type", "title", "author", "description");
  private final DocumentBuilder DOCUMENT_BUILDER;

  private String myType;
  private String myTitle;
  private String myAuthor;
  private String myDescription;
  private Map<String, Integer> myParameters;
  private List<String> myCellRows;

  public Simulation(String fileString) {
    File dataFile = new File(fileString);

    DOCUMENT_BUILDER = getDocumentBuilder();
    Element root = getRootElement(dataFile);

    ArrayList<String> infoValues = getInfo(root);
    myType = infoValues.get(0);
    myTitle = infoValues.get(1);
    myAuthor = infoValues.get(2);
    myDescription = infoValues.get(3);
    myParameters = makeParameterMap(dataFile);
    myCellRows = getCells(root);
  }

  //TODO: fix pointer stuff
  public List<String> getCellRows() {
    return myCellRows;
  }

  public int getWidth() {
    return myParameters.get("width");
  }

  public int getHeight() {
    return myParameters.get("height");
  }
  //TODO: fix pointer stuff
  public String getTitle() {
    return myTitle;
  }
  //TODO: fix pointer stuff
  public String getType() {
    return myType;
  }
  //TODO: does this pointer stuff work
  public Map<String, Integer> getParameters() {
    Map<String, Integer> returned = new HashMap<>();
    returned.putAll(myParameters);
    return returned;
  }

  private DocumentBuilder getDocumentBuilder() throws XMLException {
    try {
      return DocumentBuilderFactory.newInstance().newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      //TODO: fix excpetions
      throw new XMLException(e);
    }
  }

  private Element getRootElement(File xmlFile) throws XMLException {
    try {
      DOCUMENT_BUILDER.reset();
      Document xmlDocument = DOCUMENT_BUILDER.parse(xmlFile);
      return xmlDocument.getDocumentElement();
    } catch (SAXException | IOException e) {
      //TODO: fix exceptions
      throw new XMLException(e);
    }
  }

  private Map<String, Integer> makeParameters(File dataFile) {
    HashMap<String, Integer> returned = new HashMap<>();
    try {
      NodeList nList = DOCUMENT_BUILDER.parse(dataFile).getElementsByTagName("parameters");
      Node node = nList.item(0);
      NodeList list = node.getChildNodes();
      for (int i = 0; i < list.getLength(); i++) {
        Node n = list.item(i);
        if (n.getNodeType() == Node.ELEMENT_NODE) {
          returned.put(n.getNodeName(), Integer.parseInt(n.getTextContent()));
        }
      }
    } catch (Exception e) {
    }//TODO work on exception
    return returned;
  }

  private Map<String, Integer> makeParameterMap(File dataFile) {
    Map<String, Integer> parameterMap = makeParameters(dataFile);//TODO: RENAME
    return parameterMap;
  }

  private ArrayList<String> getCells(Element root) {
    ArrayList<String> returned = new ArrayList<>();
    NodeList cellRows = root.getElementsByTagName("cellRow");
    for (int i = 0; i < cellRows.getLength(); i++) {
      Node temp = cellRows.item(i);
      returned.add(temp.getTextContent());
    }
    return returned;
  }

  private ArrayList<String> getInfo(Element root) {
    ArrayList<String> returned = new ArrayList<>();
    for (String field : Simulation.INFORMATION_FIELDS) {
      returned.add(getTextValue(root, field));
    }
    return returned;
  }

  private String getTextValue(Element e, String tagName) {
    NodeList nodeList = e.getElementsByTagName(tagName);
    if (nodeList != null && nodeList.getLength() > 0) {
      return nodeList.item(0).getTextContent();
    } else {
      // FIXME: empty string or exception? In some cases it may be an error to not find any text
      return "";
    }
  }

  public String toString() {
    return "Type: " + myType + ", Title: " + myTitle + ", Author: " + myAuthor + ", Descr: "
        + myDescription + "\n" +
        "Parameters: " + myParameters + "\n" +
        "Cell Rows: " + myCellRows;
  }
}
