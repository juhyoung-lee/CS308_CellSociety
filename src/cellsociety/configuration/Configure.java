package cellsociety.configuration;

import java.io.File;

public class Configure {

  private String configFile;

  public Configure(String file) {
    configFile = file;
  }

  public Simulation getSimulation() {
    File dataFile = new File(configFile);
    while (dataFile != null) {
      try {
        return new XMLParser("media").getSimulation(dataFile);
      } catch (XMLException e) { }
    }
    return null;
  }
}

