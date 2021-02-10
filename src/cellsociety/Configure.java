package cellsociety;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Configure {
  private static final String CONFIGURATION_FILE = "data/configuration.XML";
  private static final String XML_INFORMATION = "information";
  private static final String XML_CELL_NAME = "name";
  private static final String XML_CELL_STATE = "state";
  private static final String XML_CELL_NEIGHBORS = "neighbors";
  private static final String XML_GRID_PARAMETERS = "grid";
  private static final String XML_GRID_WIDTH = "width";
  private static final String XML_GRID_HEIGHT = "height";

  private String type;
  private String title;
  private String author;
  private String description;
  private int width;
  private int height;
  private ArrayList<String> cellRows;

  public Configure(){
    readXML();
  }

  private void readXML() {
    File dataFile = new File(CONFIGURATION_FILE);
    while (dataFile != null) {
      try {
        Game g = new XMLParser("media").getGame(dataFile);
        System.out.println("Success");
        System.out.println(g);
      } catch (XMLException e) {
        // handle error of unexpected file format
        System.out.println("Error");
      }
    }
  }


}


//try {
//    File inputFile = new File(CONFIGURATION_FILE);
//    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//    DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
//    Document doc = docBuilder.parse(inputFile);
//    doc.getDocumentElement().normalize();
//
//    Node information = doc.getElementsByTagName(XML_INFORMATION).item(0);
//
//
//
//    //for getting sizes
//    Node node = doc.getElementsByTagName(XML_GRID_PARAMETERS).item(0);
//    if (node.getNodeType() == Node.ELEMENT_NODE) {
//      Element gridParameters = (Element) node;
//      String xmlWidth = gridParameters.getElementsByTagName(XML_GRID_WIDTH).item(0)
//          .getTextContent();
//      String xmlHeight = gridParameters.getElementsByTagName(XML_GRID_HEIGHT).item(0)
//          .getTextContent();
//      this.width = Integer.parseInt(xmlWidth);
//      this.height = Integer.parseInt(xmlHeight);
//    }
//  } catch (Exception e) {
//    e.printStackTrace();
//  }
