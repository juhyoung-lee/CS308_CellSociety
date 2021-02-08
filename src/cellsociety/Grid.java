package cellsociety;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.util.HashMap;

/**
 * @author juhyoung
 *
 * Stores and updates Cells
 * Instantiates Cell objects from XML input
 * Updates cell statuses using neighboring cell information
 *
 * Assumes proper XML input
 * configuration.XML
 * <information>
 *     <type>Game of Life</type>
 *     <title>Glider</title>
 *     <author>Richard Guy</author>
 *     <description>Example of gliders</description>
 * </information>
 * <parameters></parameters>
 * <grid>
 *     <width>400</width>
 *     <height>400</height>
 * </grid>
 * <cellconfig>
 *   <cell name="0">
 *     <neighbors>1,2</neighbors>
 *     <state>0</state>
 *   </cell>
 *   <cell name="1">
 *     <neighbors>0,3</neighbors>
 *     <state>1</state>
 *   </cell>
 *   <cell name="2">
 *     <neighbors>0,3</neighbors>
 *     <state>1</state>
 *   </cell>
 *   <cell name="3">
 *     <neighbors>1,2</neighbors>
 *     <state>0</state>
 *   </cell>
 * </cellconfig>
 *
 * Depends on java.util, java.io, javax.xml, org.w3c.dom, and cellsociety.Cell
 *
 * Example Usage
 * Grid myGrid = new Grid()
 * myGrid.updateCells()
 * visualize(myGrid.getGrid())
 */
public class Grid {

    private final String CONFIGURATION_FILE = "data/configuration.XML";
    private final String XML_CELL_NAME = "name";
    private final String XML_CELL_STATE = "state";
    private final String XML_CELL_NEIGHBORS = "neighbors";
    private final String XML_GRID_PARAMETERS = "grid";
    private final String XML_GRID_WIDTH = "width";
    private final String XML_GRID_HEIGHT = "height";

    private HashMap<Cell,String> grid;
    private int width;
    private int height;

    public Grid() {
        grid = new HashMap();
        readXML();
        printGrid();
    }

    /**
     * code adapted from https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
     *
     * TODO: consider putting checks for faulty XML file
     */
    private void readXML() {
        try {
            File inputFile = new File(CONFIGURATION_FILE);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            fillGridConfigFromDoc(doc);
            fillGridFromDoc(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillGridConfigFromDoc(Document doc) {
        Node nNode = doc.getElementsByTagName(XML_GRID_PARAMETERS).item(0);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            Element gridParameters = (Element) nNode;
            String xmlWidth = gridParameters.getElementsByTagName(XML_GRID_WIDTH).item(0).getTextContent();
            String xmlHeight = gridParameters.getElementsByTagName(XML_GRID_HEIGHT).item(0).getTextContent();
            this.width = Integer.parseInt(xmlWidth);
            this.height = Integer.parseInt(xmlHeight);
        }
    }

    private void fillGridFromDoc(Document doc) {
        NodeList cells = doc.getElementsByTagName("cell");
        for (int i = 0; i < cells.getLength(); i++){
            Node nNode = cells.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element cell = (Element) nNode;
                int cellIndex = Integer.parseInt(cell.getAttribute(XML_CELL_NAME));
                String cellNeighbors  = cell.getElementsByTagName(XML_CELL_NEIGHBORS).item(0).getTextContent();
                int cellState = Integer.parseInt((cell.getElementsByTagName(XML_CELL_STATE)).item(0).getTextContent());
                this.grid.put(new Cell(cellIndex,cellState), cellNeighbors);
            }
        }
    }

    /**
     * for testing
     * TODO: implement
     */
    private void readTextInput(){
        // unsure if to request in console or read text file
        // unsure where console request should be done
    }

    /**
     * for testing
     * TODO: implement Cell class
     * TODO: ADD ROW/COLUMN COUNT CONSIDERATION TO PRINTING
     * TODO: test find XML file on other computers/OS
     */
    private void printGrid(){
        System.out.println("Grid Information\nWidth: " + this.width + "\nHeight: " + this.height);
        for (Cell cell : this.grid.keySet()) {
//            System.out.println(cell.getState());
            System.out.println("Cell: "+cell.getIndex() + " and " + this.grid.get(cell));
        }
    }

    public static void main(String[] args){
        Grid myGrid = new Grid();
        myGrid.printGrid();
    }
}
