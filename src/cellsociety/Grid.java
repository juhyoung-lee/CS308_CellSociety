package cellsociety;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Keeps track of Cells in a Grid.
 * @author juhyoung
 *
 * <p>Stores and updates Cells Instantiates Cell objects from XML input Updates cell statuses using
 * neighboring cell information
 *
 * <p>Assumes neighborCount is in XML
 *
 * <p>Depends on java.util, java.io, javax.xml, org.w3c.dom, and cell society.Cell
 *
 * <p>Example Usage Grid myGrid = new Grid() myGrid.updateCells() visualize(myGrid.getGrid())
 */
public class Grid {

  private HashMap<Cell, int[]> neighbors;
  private ArrayList<Cell> grid;
  private final String gameType;
  private final int height;
  private final int width;
  private final int neighborCount;

  public Grid(String gameType, ArrayList<String> cellArrangement) {
    this.gameType = gameType;
    this.height = cellArrangement.size();
    this.width = cellArrangement.get(0).length();
    // TODO: MAKE NEIGHBOR COUNT DYNAMIC
    this.neighborCount = 8;
    setupGrid(cellArrangement);
    setupNeighbors(cellArrangement);
    printGrid();
  }

  /**
   * Loads updates for cells and then updates.
   * TODO: buff up for variations
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

  /**
   * Returns states of cells for printing/viewing.
   *
   * @return array list of cell states
   */
  public ArrayList<Integer> viewGrid() {
    ArrayList<Integer> cellStates = new ArrayList<>();
    for (Cell cell : this.grid) {
      cellStates.add(cell.getState());
    }
    return cellStates;
  }

  private int[] pullNeighborStates(int index) {
    // TODO: return type will be hashmap
    Cell currentCell = this.grid.get(index);
    Cell neighborCell;
    int[] neighborIndexes = this.neighbors.get(currentCell);
    int[] neighborStates = new int[neighborIndexes.length];

    for (int i = 0; i < neighborIndexes.length; i++){
      neighborCell = this.grid.get(neighborIndexes[i]);
      neighborStates[i] = neighborCell.getState();
    }
    return neighborStates;
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
      this.neighbors.put(this.grid.get(i), pullNeighborIndexes(i));
    }
  }

  // used by setupNeighbors()
  private int[] pullNeighborIndexes(int index) {
    // TODO: how to make dynamic?
    // AHH UGLY CODE
    int[] variance = switch (this.neighborCount) {
      case 8 -> new int[]{-1 - this.width, -1 * this.width, 1 - this.width, -1, 1, -1 + this.width, this.width, 1 + this.width};
      case 4 -> new int[]{-1 * this.width, -1, 1, this.width};
      default -> new int[]{};
    };
    ArrayList<Integer> possibleIndexes = new ArrayList<>();
    for (int i : variance) {
      possibleIndexes.add(variance[i] + index);
    }
    ArrayList<Integer> validIndexes = removeInvalidIndexes(index, possibleIndexes);
    return convertIntArrayList(validIndexes);
  }

  // used by pullNeighborIndexes()
  private int[] convertIntArrayList(ArrayList<Integer> validIndexes) {
    int[] neighbors = new int[validIndexes.size()];
    for (int i = 0; i < neighbors.length; i++) {
      neighbors[i] = validIndexes.get(i);
    }
    return neighbors;
  }

  // used by pullNeighborIndexes()
  private ArrayList<Integer> removeInvalidIndexes(int centerIndex, ArrayList<Integer> possibleIndexes) {
    for (int i : possibleIndexes) {
      if (i < 0 || i >= this.width * this.height) {
        possibleIndexes.remove(i);
      }
      // checks if incrementing center index by 1 changed rows
      // ie first/last row in index
      if (i + 1 == centerIndex || i - 1 == centerIndex){
       if (i / this.width != centerIndex / this.width){
         possibleIndexes.remove(i);
       }
      }
    }
    return possibleIndexes;
  }

  // used by setupGrid()
  private Cell chooseCell(int state) {
    // TODO: buff up for variations
    // variations will have to accept parameters hmMMMM
    return switch (this.gameType) {
      case "Conway's Game of Life" -> new GameOfLifeCell(0);
      case "Percolation" -> new PercolationCell(0);
      case "Fire" -> new FireCell(0, 0);
      case "Segregation" -> new SegregationCell(0, 0);
      case "WaTor" -> new WaTorCell(0, 0, 0, 0, 0);
      default -> null;
    };
  }

  // For testing.
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
