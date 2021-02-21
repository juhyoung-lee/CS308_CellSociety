package cellsociety.configuration;

public class MainTester {
  public static void main(String[] args){
    try {
      Simulation g = new Simulation("data/XMLs/WaTor/first.XML");
      System.out.println(g);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}