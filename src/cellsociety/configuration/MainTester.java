package cellsociety.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainTester {
  public static void main(String[] args){
    Map<String, String> info = new HashMap<>();
    info.put("type", "Game of Life");
    info.put("author", "Kenny");
    Map<String, Integer> params = new HashMap<>();
    params.put("width", 2);
    params.put("height", 2);
    ArrayList cells = new ArrayList();
    cells.add("0");
    cells.add("1");
    cells.add("1");
    cells.add("0");
    CreateFile file = new CreateFile(info, params, cells);
  }
}