package hu.ait.tiffanynguyen.minesweeper;

/**
 * Created by tiffanynguyen on 9/30/14.
 */
public class Unit {

    int value;
    boolean clicked;
    boolean flagged;

    Unit() {
        value = 0;
        clicked = false;
        flagged = false;
    }

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

    public void setClicked() {
        clicked = !clicked;
    }

    public boolean toggleFlagged() {
        flagged = !flagged;
        return !flagged;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
