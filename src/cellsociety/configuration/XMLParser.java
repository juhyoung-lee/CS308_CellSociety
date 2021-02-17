package cellsociety.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * This class handles parsing XML files and returning a completed object.
 *
 * @author Rhondu Smithwick
 * @author Robert C. Duvall
 * @author Kenneth Moore III
 */
public class XMLParser {

  // Readable error message that can be displayed by the GUI
  public static final String ERROR_MESSAGE = "XML file does not represent %s";
  // name of root attribute that notes the type of file expecting to parse
  private final String TYPE_ATTRIBUTE;
  // keep only one documentBuilder because it is expensive to make and can reset it before parsing
  private final DocumentBuilder DOCUMENT_BUILDER;


  /**
   * Create parser for XML files of given type.
   */
  public XMLParser(String type) throws XMLException {
    DOCUMENT_BUILDER = getDocumentBuilder();
    TYPE_ATTRIBUTE = type;
  }

  /**
   * Get data contained in this XML file as an object
   */
  public Simulation getSimulation(File dataFile) throws XMLException {
    Element root = getRootElement(dataFile);

    ArrayList<String> infoResults = getInfo(root);
    HashMap<String, Integer> parameterMap = makeParameterMap(root, dataFile);
    ArrayList<String> cellResults = getCells(root);

    return new Simulation(infoResults, parameterMap, cellResults);
  }

  private HashMap<String, Integer> makeParameterMap(Element root, File dataFile) {
    HashMap<String, Integer> parameterMap = getParameters(dataFile);
    HashMap<String, Integer> dimensionMap = getDimensions(root);
    parameterMap.putAll(dimensionMap);
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

  private HashMap<String, Integer> getDimensions(Element root) {
    HashMap<String, Integer> returned = new HashMap<>();
    for (String field : Simulation.GRID_FIELDS) {
      returned.put(field, Integer.parseInt(getTextValue(root, field)));
    }
    return returned;
  }



  private HashMap<String, Integer> getParameters(File dataFile) {
    HashMap<String, Integer> returned = new HashMap<>();
    try {
      NodeList nList = DOCUMENT_BUILDER.parse(dataFile).getElementsByTagName("paramconfig");
      Node node = nList.item(0);
      NodeList list = node.getChildNodes();
      for (int i = 0; i < list.getLength(); i++) {
        Node n = list.item(i);
        if (n.getNodeType() == Node.ELEMENT_NODE) {
          returned.put(n.getNodeName(), Integer.parseInt(n.getTextContent()));
        }
      }
    } catch (Exception e) { }//TODO work on exception
    return returned;
  }

  private ArrayList<String> getInfo(Element root) {
    ArrayList<String> returned = new ArrayList<>();
    for (String field : Simulation.INFORMATION_FIELDS) {
      returned.add(getTextValue(root, field));
    }
    return returned;
  }

  // get root element of an XML file
  private Element getRootElement(File xmlFile) throws XMLException {
    try {
      DOCUMENT_BUILDER.reset();
      Document xmlDocument = DOCUMENT_BUILDER.parse(xmlFile);
      return xmlDocument.getDocumentElement();
    } catch (SAXException | IOException e) {
      throw new XMLException(e);
    }
  }

  //  // returns if this is a valid XML file for the specified object type
//  private boolean isValidFile (Element root, String type) {
//    return getAttribute(root, TYPE_ATTRIBUTE).equals(type);
//  }
//
//  // get value of Element's attribute
//  private String getAttribute (Element e, String attributeName) {
//    return e.getAttribute(attributeName);
//  }
//
  // get value of Element's text
  private String getTextValue(Element e, String tagName) {
    NodeList nodeList = e.getElementsByTagName(tagName);
    if (nodeList != null && nodeList.getLength() > 0) {
      return nodeList.item(0).getTextContent();
    } else {
      // FIXME: empty string or exception? In some cases it may be an error to not find any text
      return "";
    }
  }

  // boilerplate code needed to make a documentBuilder
  private DocumentBuilder getDocumentBuilder() throws XMLException {
    try {
      return DocumentBuilderFactory.newInstance().newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      throw new XMLException(e);
    }
  }
}

