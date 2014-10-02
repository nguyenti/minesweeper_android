package hu.ait.tiffanynguyen.minesweeper;

/**
 * Created by tiffanynguyen on 9/21/14.
 */
public class Coordinate {

    int x;
    int y;

    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Coordinate() {}

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Coordinate intToCoord(int val, int scale) {
        int x = val % scale;
        int y = (val - x) / scale;
        return new Coordinate(x, y);
    }
}
