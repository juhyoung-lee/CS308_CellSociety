package cellsociety.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateFile {

  public CreateFile(Map<String, String> info,Map<String, Integer> params, List<String> cells) {

    List<String> cellRows = createCellRows(params, cells);

    try {
      FileWriter myWriter = new FileWriter
          ("data/CreatedFiles/created"+(System.currentTimeMillis()/10000)+".XML");
      myWriter.write("<root>\n");
      myWriter.write("  <information>\n");
      for (String s :info.keySet()){
        myWriter.write("    <"+s+">");
        myWriter.write(info.get(s));
        myWriter.write("</"+s+">\n");
      }
      myWriter.write("  </information>\n");
      myWriter.write("  <parameters>\n");
      for (String s :params.keySet()){
        myWriter.write("    <"+s+">");
        myWriter.write(params.get(s).toString());
        myWriter.write("</"+s+">\n");
      }
      myWriter.write("  </parameters>\n");
      myWriter.write("  <cells>\n");
      for (String s : cellRows) {
        myWriter.write("    <cellRow>"+s+"</cellRow>\n");
      }
      myWriter.write("  </cells>\n");
      myWriter.write("</root>");
      myWriter.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<String> createCellRows(Map<String, Integer> params, List<String> cells) {
    List<String> returned = new ArrayList<>();
    String temp = "";
    int width = params.get("width");
    int height= params.get("height");
    for(int i = 0; i<height;i++){
      for (int j = 0; j < width; j++) {
        temp = temp+ cells.get(i*height+j);
      }
      returned.add(temp);
      temp="";
    }
    return returned;
  }

}
