package hu.ait.tiffanynguyen.minesweeper;

/**
 * Cell class holds a value and whether the cell has been clicked or flagged
 * by Tiffany Nguyen
 */
public class Cell {
    /*------------------+
     |    Properties    |
     +------------------*/

    int value;
    boolean clicked;
    boolean flagged;

    /*------------------+
     |   Constructor    |
     +------------------*/

    Cell() {
        value = 0;
        clicked = false;
        flagged = false;
    }

    /*------------------+
     |     Methods      |
     +------------------*/

    public int getValue() {
        return value;
    }

    public void setValue(int val) {
        value = val;
    }

    public void incrementValue() {
        value++;
    }

    public boolean isClicked() {
        return clicked;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void toggleClicked() {
        clicked = !clicked;
    }

    public void toggleFlagged() {
        flagged = !flagged;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
