package cellsociety;

import java.io.File;

public class Configure {

  private static final String CONFIGURATION_FILE = "data/configuration.XML";


  public Configure() {
    readXML();
  }

  private void readXML() {
    File dataFile = new File(CONFIGURATION_FILE);
    while (dataFile != null) {
      try {
        Game g = new XMLParser("media").getGame(dataFile);
      } catch (XMLException e) {
        // handle error of unexpected file format
        System.out.println("Error");
      }
    }
  }
}

