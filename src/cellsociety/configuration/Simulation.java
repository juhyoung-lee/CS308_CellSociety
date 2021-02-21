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

  public Simulation(String fileString) throws XMLException {
    File dataFile = new File(fileString);

    DOCUMENT_BUILDER = getDocumentBuilder();
    Element root = getRootElement(dataFile);

    myParameters = makeParameterMap(dataFile);
    myCellRows = makeCellRowList(root);
    List<String> infoValues = makeInfoList(root);
    myType = infoValues.get(0);
    myTitle = infoValues.get(1);
    myAuthor = infoValues.get(2);
    myDescription = infoValues.get(3);

    checkCellWidth();
    checkCellHeight();
  }

  private void checkCellHeight() throws XMLException {
    if (myParameters.get("height") == null) {
      throw new XMLException("MissingHeight");//turn into resource field
    }
    if (myCellRows.size() != getHeight()) {
      throw new XMLException("InvalidHeight");//turn into resource file
    }
  }

  private void checkCellWidth() throws XMLException {
    if (myParameters.get("width") == null) {
      throw new XMLException("MissingWidth");//turn into resource file
    }
    int count = 0;
    for (String cellRow : myCellRows) {
      count++;
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
      throw new XMLException(null, null);
      //TODO work on exception
      //parse datafile exception
    }
  }


  private List<String> makeCellRowList(Element root) throws XMLException{
    List<String> returned = new ArrayList<>();
    NodeList cellRows = root.getElementsByTagName("cellRow");
    int length = cellRows.getLength();
    if (length==0) {
      throw new XMLException("MissingCellRows");
    } else {
      for (int i = 0; i < length; i++) {
        Node temp = cellRows.item(i);
        returned.add(temp.getTextContent());
      }
    }
    return returned;
  }

  private List<String> makeInfoList(Element root) throws XMLException{
    List<String> returned = new ArrayList<>();
    String temp;
    for (String field : Simulation.INFORMATION_FIELDS) {
      temp = getTextValue(root, field);
      if(temp == null) {
        throw new XMLException("Missing"+field);
      } else {
        returned.add(temp);
      }
    }
    return returned;
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
    } catch (ParserConfigurationException e) {
      //TODO: fix exceptions
      //Document builder exception 1
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
      //Document Exception 2
      throw new XMLException(e);
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

  public int getWidth() {
    return myParameters.get("width");
  }

  public int getHeight() {
    return myParameters.get("height");
  }

  public String getTitle() {
    return myTitle;
  }

  public String getType() {
    return myType;
  }

  public String toString() {
    return "Type: " + myType + ", Title: " + myTitle + ", Author: " + myAuthor + ", Descr: "
        + myDescription + "\n" +
        "Parameters: " + myParameters + "\n" +
        "Cell Rows: " + myCellRows;
  }
}
