package hu.ait.tiffanynguyen.minesweeper;

/**
 * Coordinate class contains the x,y coordinate
 * by Tiffany Nguyen
 */
public class Coordinate {

    /*------------------+
     |    Properties    |
     +------------------*/

    int x;
    int y;

    /*------------------+
     |   Constructors   |
     +------------------*/

    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Coordinate() {}

    /*------------------+
     |     Methods      |
     +------------------*/
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
