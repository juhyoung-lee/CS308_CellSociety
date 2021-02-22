//package cellsociety.configuration;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.Map;
//
//public class CreateFile {
//
//  File myFile;
//
//  public CreateFile(Map<String, Integer> params) {
//
//    try {
//      FileWriter myWriter = new FileWriter
//          ("data/CreatedFiles/created"+(System.currentTimeMillis()/10000)+".XML");
//      myWriter.write("<root>\n");
//      myWriter.write("  <information>\n");
//      myWriter.write("    <type>"+type+"</type>\n");
//      myWriter.write("    <titles>"+title+"</titles>\n");
//      myWriter.write("    <author>"+author+"</author>\n");
//      myWriter.write("    <description>"+description+"</description>\n");
//      myWriter.write("  </information>\n");
//      myWriter.write("  <parameters>\n");
//      for (String s :params.keySet()){
//        myWriter.write("    <cellRow>");
//        myWriter.write(params.get(s));
//        myWriter.write("  </cellRow>\n");
//      }
//      myWriter.write("  </parameters>\n");
//      myWriter.write("  <cells>\n");
//      for(String s: cellRows){
//        myWriter.write("    <cellRow>");
//        myWriter.write(params.get(s));
//        myWriter.write("  </cellRow>\n");
//      }
//      myWriter.write("  </cells>\n");
//      myWriter.write("</root>");
//      myWriter.close();
//
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//}
