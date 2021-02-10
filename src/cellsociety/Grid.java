package cellsociety;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Keeps track of Cells in a Grid.
 * @author juhyoung
 *
 * <p>Stores and updates Cells Instantiates Cell objects from XML input Updates cell statuses using
 * neighboring cell information
 *
 * <p>Assumes proper XML input configuration.XML TODO: example here!!
 *
 * <p>Depends on java.util, java.io, javax.xml, org.w3c.dom, and cell society.Cell
 *
 * <p>Example Usage Grid myGrid = new Grid() myGrid.updateCells() visualize(myGrid.getGrid())
 */
public class Grid {

  private HashMap<Cell, String> neighbors;
  private ArrayList<Cell> grid;
  private String gameType;
  private int height;
  private int width;


  /**
   * Constructor.
   *
   * @param gameType
   * @param cellArrangement array of Strings representing a row of cells
   */
  public Grid(String gameType, ArrayList<String> cellArrangement) {
    this.gameType = gameType;
    this.height = cellArrangement.size();
    this.width = cellArrangement.get(0).length();
    setupGrid(cellArrangement);
    setupNeighbors(cellArrangement);
    printGrid();
  }

  /**
   * Loads updates for cells and then updates.
   */
  public void updateCells() {
    for (int i = 0; i < grid.size(); i++) {
      int[] neighborStates = pullNeighborStates(i);
      grid.get(i).prepareNewState(neighborStates);
    }
    for (Cell cell : grid) {
      cell.updateState();
    }
  }

  private int[] pullNeighborStates(int index) {
    return null;
  }

  private void setupGrid(ArrayList<String> cellArrangement) {
    this.grid = new ArrayList<>();
    for (String s : cellArrangement) {
      String[] row = s.split("");
      for (String state : row) {
        Cell cell = chooseCell(Integer.parseInt(state));
        this.grid.add(cell);
      }
    }
  }

  private void setupNeighbors(ArrayList<String> cellArrangement){
    this.neighbors = new HashMap<>();
    for (int i = 0; i < grid.size(); i++) {
    }
  }

  private int[] pullNeighborIndexes(int index) {

  }

  private Cell chooseCell(int state) {
    Cell cell;
    switch (this.gameType) {
      case "Conway's Game of Life":
        cell = new GameOfLifeCell(state);
        break;
      default:
        cell = null;
    }
    return cell;
  }

  /**
   * For testing.
   *
   * <p>TODO: implement Cell class TODO: ADD ROW/COLUMN COUNT CONSIDERATION TO PRINTING
   * TODO: test find XML file on other computers/OS
   */
  private void printGrid() {
    for (Cell cell : this.neighbors.keySet()) {
       System.out.print(cell.getState());
      // figure out when to enter
    }
  }

  public static void main(String[] args) {
    Grid myGrid = new Grid("Conway's Game of Life", new ArrayList<>());
    myGrid.printGrid();
  }
}
