package cellsociety.configuration;

public class MainTester {
  public static void main(String[] args){
    Configure c = new Configure("data/XMLs/WaTor/first.XML");
    Simulation g = c.getSimulation();
    System.out.println(g);
  }
}
