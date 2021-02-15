package cellsociety.model;

import cellsociety.model.fire.FireCell;
import cellsociety.model.gameoflife.GameOfLifeCell;
import cellsociety.model.percolation.PercolationCell;
import cellsociety.model.segregation.SegregationCell;
import cellsociety.model.wator.WaTorCell;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Keeps track of Cells in a Grid.
 * @author juhyoung
 * Stores and updates Cells Instantiates Cell objects from XML input Updates cell statuses using neighboring cell information
 * Assumes neighborCount is in XML
 * Depends on java.util, java.io, javax.xml, org.w3c.dom, and cell society.Cell
 * Example Usage Grid myGrid = new Grid() myGrid.updateCells() visualize(myGrid.getGrid())
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
  }

  /**
   * Purpose: Loads updates for cells and then updates.
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
   * Purpose: Returns states of cells for printing/viewing.
   * @return array list of cell states
   */
  public ArrayList<Integer> viewGrid() {
    ArrayList<Integer> cellStates = new ArrayList<>();
    for (Cell cell : this.grid) {
      cellStates.add(cell.getState());
    }
    return cellStates;
  }

  /**
   * Purpose: Returns grid size
   * @return [width, height]
   */
  public int[] getDimensions() {
    return new int[]{this.width, this.height};
  }

  private int[] pullNeighborStates(int index) {
    // TODO: return type will be hashmap
    Cell currentCell = this.grid.get(index);
    Cell neighborCell;
    int[] neighborIndexes = this.neighbors.get(currentCell);
    int[] neighborStates = new int[neighborIndexes.length];

    for (int i = 0; i < neighborIndexes.length; i++) {
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

  private void setupNeighbors(ArrayList<String> cellArrangement) {
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
      case 8 -> new int[]{-1 - this.width, -1 * this.width, 1 - this.width, -1, 1, -1 + this.width,
          this.width, 1 + this.width};
      case 4 -> new int[]{-1 * this.width, -1, 1, this.width};
      default -> new int[]{};
    };
    ArrayList<Integer> possibleIndexes = new ArrayList<>();
    for (int i : variance) {
      possibleIndexes.add(i + index);
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
  private ArrayList<Integer> removeInvalidIndexes(int centerIndex,
      ArrayList<Integer> possibleIndexes) {
    // ugly method
    ArrayList<Integer> validIndexes = new ArrayList<>(possibleIndexes);
    for (int i : possibleIndexes) {
      if (i < 0 || i >= this.width * this.height) {
        validIndexes.remove((Integer) i);
      }
      // checks if incrementing center index by 1 changed rows
      // ie first/last row in index
      if (i + 1 == centerIndex || i - 1 == centerIndex) {
        if (i / this.width != centerIndex / this.width) {
          validIndexes.remove((Integer) i);
        }
      }
    }
    return validIndexes;
  }

  // used by setupGrid()
  private Cell chooseCell(int state) {
    // TODO: buff up for variations
    HashMap<String,Integer> input = new HashMap<>();
    input.put("state", state);
    return switch (this.gameType) {
      case "Conway's Game of Life" -> new GameOfLifeCell(input);
      case "Percolation" -> new PercolationCell(input);
      case "Fire" -> new FireCell(input);
      case "Segregation" -> new SegregationCell(input);
      case "WaTor" -> new WaTorCell(input);
      default -> null;
    };
  }

  // For testing.
  private void printGrid() {
    int rowCounter = 0;
    for (Cell cell : this.grid) {
      if (rowCounter == this.width) {
        System.out.println();
        rowCounter = 0;
      }
      System.out.print(cell.getState());
      rowCounter++;
    }
  }

  public static void main(String[] args) {
    ArrayList<String> input = new ArrayList<>();
    input.add("00000");
    input.add("01110");
    input.add("00000");
    Grid myGrid = new Grid("Conway's Game of Life", input);

    /**
     * prints neighbors
    for (int i = 0; i < myGrid.grid.size(); i++) {
      System.out.print("Cell "+i+": ");
      int[] array = myGrid.neighbors.get(myGrid.grid.get(i));
      for (int j : array) {
        System.out.print(j);
      }
      System.out.println();
    }
     **/

    for(int i = 0; i < 5; i++){
      System.out.println("\n" + "Step " + i);
      myGrid.printGrid();
      System.out.println();
      myGrid.updateCells();
    }
  }
}

// next step: work with kenny to accept xml parameters
// update to match jessica hashmap state