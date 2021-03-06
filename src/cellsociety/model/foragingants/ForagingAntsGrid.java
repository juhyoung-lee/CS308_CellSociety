package cellsociety.model.foragingants;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Allows for the simulation of ForagingAnts.
 * Assumptions: game will follow the rules laid out in ForagingAntsCell.
 * Dependencies: java.util.*, GridHelper, Grid, Cell, ForagingAntsCell
 * Examples:
 * '''
 * Grid grid = new ForagingAntsGrid(cellA, gridP, cellP);
 * grid.updateCells();
 * '''
 */
public class ForagingAntsGrid extends Grid {

  /**
   * Fills grid and instantiates cells.
   *
   * @param cellArrangement cell states and positions from XML
   * @param gridParameters grid settings from XML
   * @param cellParameters cell settings from XML
   * @throws Exception when parameters are invalid
   */
  public ForagingAntsGrid(List<String> cellArrangement, String[] gridParameters,
      Map<String, Integer> cellParameters) throws Exception {
    super(cellArrangement, gridParameters, cellParameters);
  }

  /**
   * Returns ForagingAntsCell object.
   *
   * @param parameters cell state and game parameters from XML
   * @return ForagingAntsCell object
   */
  @Override
  protected Cell chooseCell(Map<String, Integer> parameters) {
    return new ForagingAntsCell(parameters);
  }

  /**
   * Ants can only move cardinally so this enforces small neighborhood sizes.
   *
   * @param index center index
   * @return values for computing neighboring indexes
   * @throws Exception when small neighborhood does not match input from XML
   */
  @Override
  protected int[] decideNeighborhood(int index) throws Exception {
    return decideSmallNeighborhood(index);
  }



  /**
   * Returns array list of indexes that should be checked to receive a moving cell. Differentiates
   * before between when ants have and don't have food. Follows pheromone trails when available, and
   * walks randomly when not.
   *
   * @param index of cell trying to move
   * @return indexes to be checked for receiving update
   */
  @Override
  protected List<Integer> findPotentialMoves(int index) {
    Cell center = getGrid().get(index);
    int[] neighbors = getNeighbors(center);
    boolean hasFood = getIssues(index).get(ForagingAntsCell.HAS_FOOD) == 1;
    int[][] sortedNeighbors = sortNeighborsByPheromone(hasFood, neighbors);

    if (sortedNeighbors[0][1] == 0) {
      return hasNoPheromones(index, hasFood, neighbors);
    }

    List<Integer> ordering = new ArrayList<>();
    for (int[] i : sortedNeighbors) {
      ordering.add(i[0]);
    }
    return ordering;
  }

  /**
   * Returns appropriately random arrays in the case that neighboring cells all lack pheromones.
   * When searching for food, moves randomly away from home. When ant has food, moves randomly.
   *
   * @param index     ant index
   * @param hasFood   whether or not ant has food
   * @param neighbors index of neighboring cells
   * @return arraylist of neighbors appropriately shuffled
   */
  private List<Integer> hasNoPheromones(int index, boolean hasFood, int[] neighbors) {
    if (hasFood) {
      return shuffleArray(neighbors);
    } else {
      List<Integer> movingAway = new ArrayList<>();
      for (int i : neighbors) {
        if (i > index) {
          movingAway.add(i);
        }
      }
      Collections.shuffle(movingAway);
      return movingAway;
    }
  }

  /**
   * Creates a 2D array where each row is cell index, food pheromone count, and home pheromone
   * count. Sorts by food or home pheromones depending on if ant has food. Removes unnecessary
   * pheromone column. If it has food, the ant only needs home pheromone and vice versa.
   *
   * @param hasFood   whether the ant has food or not
   * @param neighbors int[] of neighbor indexes
   * @return [[neighbor index, pheromone],...] sorted by relevant pheromone
   */
  private int[][] sortNeighborsByPheromone(boolean hasFood, int[] neighbors) {
    int[][] neighborStates = new int[neighbors.length][3];
    for (int i = 0; i < neighbors.length; i++) {
      ForagingAntsCell neighbor = (ForagingAntsCell) getGrid().get(neighbors[i]);
      neighborStates[i][0] = neighbors[i];
      neighborStates[i][1] = neighbor.getPheromone()[0];
      neighborStates[i][2] = neighbor.getPheromone()[1];
    }
    if (hasFood) {
      Arrays.sort(neighborStates, Comparator.comparingInt(row -> -1 * row[2]));
      neighborStates = removeColumn(neighborStates, 1);
    } else {
      Arrays.sort(neighborStates, Comparator.comparingInt(row -> -1 * row[1]));
      neighborStates = removeColumn(neighborStates, 2);
    }
    return neighborStates;
  }

  /**
   * Fisher-Yates shuffle array function. By Dan Bray at https://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
   * Modified to return array list.
   *
   * @param array int array
   * @return shuffled int array as arraylist
   */
  private List<Integer> shuffleArray(int[] array) {
    int index, temp;
    Random random = new Random();
    for (int i = array.length - 1; i > 0; i--) {
      index = random.nextInt(i + 1);
      temp = array[index];
      array[index] = array[i];
      array[i] = temp;
    }

    List<Integer> list = new ArrayList<>();
    for (int i : array) {
      list.add(i);
    }
    return list;
  }

  /**
   * Extracts first column from 2D int array.
   *
   * @param input int[][]
   * @return first column of int[][]
   */
  private int[][] removeColumn(int[][] input, int column) {
    int[][] array = new int[input.length][input[0].length - 1];
    for (int i = 0; i < array.length; i++) {
      array[i] = removeIndex(input[i], column);
    }
    return array;
  }

  /**
   * Removes index from int[].
   *
   * @param input int[]
   * @param index to remove
   * @return int[] without value at index
   */
  private int[] removeIndex(int[] input, int index) {
    int[] array = new int[input.length - 1];
    for (int i = 0; i < input.length; i++) {
      if (i < index) {
        array[i] = input[i];
      }
      if (i > index) {
        array[i - 1] = input[i];
      }
    }
    return array;
  }
}
