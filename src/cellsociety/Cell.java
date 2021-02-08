package cellsociety;

public class Cell {
    private int index;
    private int state;
    public Cell(int index, int state) {
        this.index = index;
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public int getIndex() {
        return index;
    }
}
