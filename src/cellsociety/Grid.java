package cellsociety;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//@author juhyoung

/**
 * Keeps track of Cells in a Grid.
 *
 * <p>Stores and updates Cells Instantiates Cell objects from XML input Updates cell statuses using
 * neighboring cell information
 *
 * <p>Assumes proper XML input configuration.XML TODO: example here!!
 *
 * <p>Depends on java.util, java.io, javax.xml, org.w3c.dom, and cell society.Cell
 *
 * <p>Example Usage Grid myGrid = new Grid() myGrid.updateCells() visualize(myGrid.getGrid())
 */
public class Grid {

  private static final String CONFIGURATION_FILE = "data/configuration.XML";
  private static final String XML_CELL_NAME = "name";
  private static final String XML_CELL_STATE = "state";
  private static final String XML_CELL_NEIGHBORS = "neighbors";
  private static final String XML_GRID_PARAMETERS = "grid";
  private static final String XML_GRID_WIDTH = "width";
  private static final String XML_GRID_HEIGHT = "height";

  private HashMap<Cell, String> grid;
  private int width;
  private int height;

  /**
   * Constructor.
   */
  public Grid() {
    grid = new HashMap<>();
    readTextInput();
    // readXml();
    printGrid();
  }

  /**
   * Loads updates for cells and then updates.
   */
  public void updateCells() {
  }

  /**
   * Reads in XML.
   *
   * <p>code adapted from https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
   *
   * <p>TODO:consider putting checks for faulty XML file
   */
  private void readXml() {
    try {
      File inputFile = new File(CONFIGURATION_FILE);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(inputFile);
      doc.getDocumentElement().normalize();

      getGridConfigFromDoc(doc);
      fillGridFromDoc(doc);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void getGridConfigFromDoc(Document doc) {
    Node node = doc.getElementsByTagName(XML_GRID_PARAMETERS).item(0);
    if (node.getNodeType() == Node.ELEMENT_NODE) {
      Element gridParameters = (Element) node;
      String xmlWidth = gridParameters.getElementsByTagName(XML_GRID_WIDTH).item(0)
          .getTextContent();
      String xmlHeight = gridParameters.getElementsByTagName(XML_GRID_HEIGHT).item(0)
          .getTextContent();
      this.width = Integer.parseInt(xmlWidth);
      this.height = Integer.parseInt(xmlHeight);
    }
  }

  private void fillGridFromDoc(Document doc) {
    NodeList cells = doc.getElementsByTagName("cell");
    for (int i = 0; i < cells.getLength(); i++) {
      Node node = cells.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element cell = (Element) node;
        int cellIndex = Integer.parseInt(cell.getAttribute(XML_CELL_NAME));
        String cellNeighbors = cell.getElementsByTagName(XML_CELL_NEIGHBORS).item(0)
            .getTextContent();
        int cellState = Integer
            .parseInt((cell.getElementsByTagName(XML_CELL_STATE)).item(0).getTextContent());
        this.grid.put(new Cell(cellIndex, cellState), cellNeighbors);
      }
    }
  }

  /**
   * For testing.
   *
   * <p>TODO: implement
   */
  private void readTextInput() {
    Scanner in = new Scanner(System.in);
    System.out.println("Are you playing Conway's Game of Life? (Answer y)\n");
    if (!in.nextLine().equals("y")) {
      System.out.println("Goodbye\n");
      return;
    }
    System.out.println("Enter grid width as an integer: ");
    this.width = in.nextInt();
    System.out.println("Enter grid height as an integer: ");
    this.height = in.nextInt();


  }

  /**
   * For testing.
   *
   * <p>TODO: implement Cell class TODO: ADD ROW/COLUMN COUNT CONSIDERATION TO PRINTING
   * TODO: test find XML file on other computers/OS
   */
  private void printGrid() {
    System.out.println("Grid Information\nWidth: " + this.width + "\nHeight: " + this.height);
    for (Cell cell : this.grid.keySet()) {
      // System.out.println(cell.getState());
      System.out.println("Cell: " + cell.getIndex() + " and " + this.grid.get(cell));
    }
  }

  public static void main(String[] args) {
    Grid myGrid = new Grid();
    myGrid.printGrid();
  }
}
