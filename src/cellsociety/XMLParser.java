package cellsociety;

import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * This class handles parsing XML files and returning a completed object.
 *
 * @author Rhondu Smithwick
 * @author Robert C. Duvall
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
  public XMLParser (String type) throws XMLException {
    DOCUMENT_BUILDER = getDocumentBuilder();
    TYPE_ATTRIBUTE = type;
  }

  /**
   * Get data contained in this XML file as an object
   */
  public Game getGame (File dataFile) throws XMLException {
    Element root = getRootElement(dataFile);
//    if (! isValidFile(root, Game.DATA_TYPE)) {
//      throw new XMLException(ERROR_MESSAGE, Game.DATA_TYPE);
//    }
    // read data associated with the fields given by the object
    ArrayList<String> infoResults = new ArrayList<>();
    for (String field : Game.INFORMATION_FIELDS) {
      infoResults.add(getTextValue(root, field));
    }

    ArrayList<Integer> gridResults = new ArrayList<>();
    for (String field : Game.GRID_FIELDS) {
      gridResults.add(Integer.parseInt(getTextValue(root, field)));
    }

    NodeList cellRows = root.getElementsByTagName("cellRow");
    ArrayList<Integer> parameterResults = new ArrayList<>();
    for (String field : Game.GRID_FIELDS) {
      gridResults.add(Integer.parseInt(getTextValue(root, field)));
    }

    return new Game(infoResults, gridResults);
  }

  // get root element of an XML file
  private Element getRootElement (File xmlFile) throws XMLException {
    try {
      DOCUMENT_BUILDER.reset();
      Document xmlDocument = DOCUMENT_BUILDER.parse(xmlFile);
      return xmlDocument.getDocumentElement();
    }
    catch (SAXException | IOException e) {
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
  private String getTextValue (Element e, String tagName) {
    NodeList nodeList = e.getElementsByTagName(tagName);
    if (nodeList != null && nodeList.getLength() > 0) {
      return nodeList.item(0).getTextContent();
    }
    else {
      // FIXME: empty string or exception? In some cases it may be an error to not find any text
      return "";
    }
  }

  // boilerplate code needed to make a documentBuilder
  private DocumentBuilder getDocumentBuilder () throws XMLException {
    try {
      return DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }
    catch (ParserConfigurationException e) {
      throw new XMLException(e);
    }
  }
}

