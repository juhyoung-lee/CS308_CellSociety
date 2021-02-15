package cellsociety;

import java.io.File;

public class Configure {

  private String configFile;

  public Configure(String file) {
    configFile = file;
  }

  public Game getGame() {
    File dataFile = new File(configFile);
    while (dataFile != null) {
      try {
        return new XMLParser("media").getGame(dataFile);
      } catch (XMLException e) { }
    }
    return null;
  }
}

