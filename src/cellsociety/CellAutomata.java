package cellsociety;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 *
 */
public class CellAutomata {

    private final String CONFIGURATION_FILE = "data/configuration.XML";
    private final String XML_CELL_NAME = "name";

    /**
     *
     */
    public CellAutomata() {
    
    }

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


    /**
     *
     */
    public void step() {

    }


    private void takeXML(String file) {

    }
}