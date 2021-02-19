package cellsociety.model.bylsloop;

import cellsociety.model.Cell;
import java.util.HashMap;
import java.util.Map;

/**
 * Purpose: Represents a cell for the Byl's Loop simulation. Extends the Cell class.
 * Assumptions: Requires specific starting configuration, and rectangular grid.
 * Dependencies: TODO
 * Example of use: TODO
 *
 * @author Jessica Yang
 */
public class BylsLoopCell extends Cell {

  public static final int EMPTY = 0;
  public static final int DATA_PATH = 1;
  public static final int WALL = 2;
  public static final int EXTEND_SIGNAL = 3;
  public static final int LEFT_SIGNAL = 4;
  public static final int DISCONNECT_SIGNAL = 5;

  private final Map<String, Integer> emptyTable = new HashMap<>();
  private final Map<String, Integer> dataPathTable = new HashMap<>();
  private final Map<String, Integer> wallTable = new HashMap<>();
  private final Map<String, Integer> extendSignalTable = new HashMap<>();
  private final Map<String, Integer> leftSignalTable = new HashMap<>();
  private final Map<String, Integer> disconnectSignalTable = new HashMap<>();

  /**
   * Purpose: Constructor for BylsLoopCell class.
   * Assumptions: TODO
   * Parameters: Map config.
   * Exceptions: TODO
   * Returns: BylsLoopCell object.
   */
  public BylsLoopCell(Map<String, Integer> config) {
    super(config);
    setMaxStateValue(DISCONNECT_SIGNAL);
    fillEmptyTable();
    fillDataPathTable();
    fillWallTable();
    fillExtendSignalTable();
    fillLeftSignalTable();
    fillDisconnectSignalTable();
  }

  /**
   * Purpose: Determine new state to update to.
   * Assumptions: neighborStates will be passed in clockwise order, and will contain 4 values.
   * Parameters: int[] neighborStates.
   * Exceptions: TODO
   * Returns: Map object. There should never be any movement.
   * Rules taken from https://fab.cba.mit.edu/classes/865.18/replication/Byl.pdf
   */
  public Map<String, Integer> prepareNextState(int[] neighborStates) {
    String minRotationKey = getMinRotationKey(neighborStates);

    if (getState() == EMPTY) {
      setNextState(emptyTable.getOrDefault(minRotationKey, EMPTY));
    } else if (getState() == DATA_PATH) {
      setNextState(dataPathTable.getOrDefault(minRotationKey, LEFT_SIGNAL));
    } else if (getState() == WALL) {
      setNextState(wallTable.getOrDefault(minRotationKey, WALL));
    } else if (getState() == EXTEND_SIGNAL) {
      setNextState(extendSignalTable.getOrDefault(minRotationKey, EXTEND_SIGNAL));
    } else if (getState() == LEFT_SIGNAL) {
      setNextState(leftSignalTable.getOrDefault(minRotationKey, EXTEND_SIGNAL));
    } else {
      setNextState(disconnectSignalTable.getOrDefault(minRotationKey, WALL));
    }

    setMoveStateValue("state", NO_MOVEMENT);
    return getMoveStateCopy();
  }

  /** Transition table requires neighbor states in clockwise, minimum value order. */
  private String getMinRotationKey(int[] neighborStates) {
    int minRotation = 6000; // number greater than largest possible rotation
    int temp;

    for (int i = 0; i < 4; i++) {
      temp = (neighborStates[i % 4] * 1000) + (neighborStates[(1 + i) % 4] * 100)
          + (neighborStates[(2 + i) % 4] * 10) + (neighborStates[(3 + i) % 4]);
      if (temp < minRotation) {
        minRotation = temp;
      }
    }

    return String.format("%04d", minRotation);
  }

  private void fillEmptyTable() {
    emptyTable.put("0003", DATA_PATH);
    emptyTable.put("0012", WALL);
    emptyTable.put("0013", DATA_PATH);
    emptyTable.put("0015", WALL);
    emptyTable.put("0025", DISCONNECT_SIGNAL);
    emptyTable.put("0031", DISCONNECT_SIGNAL);
    emptyTable.put("0032", EXTEND_SIGNAL);
    emptyTable.put("0042", WALL);
  }

  private void fillDataPathTable() {
    dataPathTable.put("0000", EMPTY);
    dataPathTable.put("0001", EMPTY);
    dataPathTable.put("0003", EXTEND_SIGNAL);
    dataPathTable.put("0004", EMPTY);
    dataPathTable.put("0033", EMPTY);
    dataPathTable.put("0043", DATA_PATH);
    dataPathTable.put("0321", EXTEND_SIGNAL);
    dataPathTable.put("1253", DATA_PATH);
    dataPathTable.put("2453", EXTEND_SIGNAL);
  }

  private void fillWallTable() {
    wallTable.put("0000", EMPTY);
    wallTable.put("0015", DISCONNECT_SIGNAL);
    wallTable.put("0022", EMPTY);
    wallTable.put("0202", EMPTY);
    wallTable.put("0215", DISCONNECT_SIGNAL);
    wallTable.put("0235", EXTEND_SIGNAL);
    wallTable.put("0252", DISCONNECT_SIGNAL);
  }

  private void fillExtendSignalTable() {
    extendSignalTable.put("0001", EMPTY);
    extendSignalTable.put("0003", EMPTY);
    extendSignalTable.put("0011", EMPTY);
    extendSignalTable.put("0012", DATA_PATH);
    extendSignalTable.put("0121", DATA_PATH);
    extendSignalTable.put("0123", DATA_PATH);
    extendSignalTable.put("1122", DATA_PATH);
    extendSignalTable.put("1123", DATA_PATH);
    extendSignalTable.put("1215", DATA_PATH);
    extendSignalTable.put("1223", DATA_PATH);
    extendSignalTable.put("1233", DATA_PATH);
    extendSignalTable.put("1235", DISCONNECT_SIGNAL);
    extendSignalTable.put("1432", DATA_PATH);
    extendSignalTable.put("1452", DISCONNECT_SIGNAL);
  }

  private void fillLeftSignalTable() {
    leftSignalTable.put("0003", DISCONNECT_SIGNAL);
    leftSignalTable.put("0043", LEFT_SIGNAL);
    leftSignalTable.put("0212", LEFT_SIGNAL);
    leftSignalTable.put("0232", LEFT_SIGNAL);
    leftSignalTable.put("0242", LEFT_SIGNAL);
    leftSignalTable.put("0252", EMPTY);
    leftSignalTable.put("0325", DISCONNECT_SIGNAL);
  }

  private void fillDisconnectSignalTable() {
    disconnectSignalTable.put("0022", DISCONNECT_SIGNAL);
    disconnectSignalTable.put("0032", DISCONNECT_SIGNAL);
    disconnectSignalTable.put("0212", LEFT_SIGNAL);
    disconnectSignalTable.put("0222", EMPTY);
    disconnectSignalTable.put("0322", EMPTY);
  }
}
