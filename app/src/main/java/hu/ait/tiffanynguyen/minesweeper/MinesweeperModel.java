package hu.ait.tiffanynguyen.minesweeper;

import android.util.Log;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Minesweeper game functionality
 * by Tiffany Nguyen
 */
public class MinesweeperModel {

    /*------------------+
     |   Constructors   |
     +------------------*/

    // Only one instance
    private static MinesweeperModel instance = null;

    // outside this class, you cannot make a new object
    private MinesweeperModel() {
    }

    // key part: return the specific instance if it is already made
    // makes new one if not (see code above)
    public static MinesweeperModel getInstance() {
        if (instance == null) {
            instance = new MinesweeperModel();
        }
        return instance;
    }

    /*------------------+
     |    Properties    |
     +------------------*/

    // can be edited later on to either be an input or can be changed here
    public static int numberOfMines = 3; // 10

    private Cell grid[][]; // stores the data of a cell
    private int gridSize; // the dimensions of the grid
    public int clickedCount = 0;
    public static final int MINE = -1;

    /*------------------+
     |  Getter/Setter   |
     +------------------*/

    public Cell getFieldContent(int x, int y) {
        return grid[y][x];
    }

    public void setFieldContent(Coordinate c, int content) {
        grid[c.getY()][c.getX()].setValue(content);
    }

    /*------------------+
     |   Grid methods   |
     +------------------*/

    // initialize grid
    public void initializeGrid(int size) {
        if (grid == null) {
            grid = new Cell[size][size];
            gridSize = size;
        }
        resetGrid();
    }

    // resets all elements in grid
    public void resetGrid() {
        for (int j = 0; j < gridSize; j++) {
            for (int i = 0; i < gridSize; i++) {
                grid[j][i] = new Cell();
            }
        }
        fillGrid();
    }

    // fill the grid with mines and counts
    private void fillGrid() {
        generateMines(); // generates the mines to fill around
        for (int j = 0; j < gridSize; j++) {
            for (int i = 0; i < gridSize; i++) {
                if (grid[j][i].getValue() == -1) {
                    fillAround(i, j);
                }
            }
        }
    }

    // generate numberOfMines in the grid, placing them randomly in the array
    private void generateMines() {
        Random rand = new Random();
        for (int i = 0; i < numberOfMines; i++) {
            Coordinate c = new Coordinate();
            c = c.intToCoord(rand.nextInt(gridSize * gridSize), gridSize);
            // make sure coordinate has not been previously set as mine
            if (grid[c.getY()][c.getX()].getValue() == 0) {
                setFieldContent(c, MINE);
            } else {
                i--;
            }
        }
    }

    // fills the units around a cell at x, y to indicate it is adjacent to a mine
    private void fillAround(int x, int y) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                try {
                    if (grid[y + i][x + j].getValue() != -1) {
                        grid[y + i][x + j].incrementValue();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    // deal with the instances that the cell at [y+i][x+j] is next to a wall
                    // and does not have a cell at the location
                    continue;
                }
            }
        }
    }

    /*------------------+
     |  Other methods   |
     +------------------*/

    // clicks all elements in grid
    public void clickAll() {
        for (int j = 0; j < gridSize; j++) {
            for (int i = 0; i < gridSize; i++) {
                if (!grid[j][i].isClicked()) {
                    grid[j][i].toggleClicked();
                }
                if (grid[j][i].isFlagged()) {
                    grid[j][i].toggleFlagged();
                }
            }
        }
    }

    public boolean checkWin() {
        return clickedCount == (gridSize * gridSize - numberOfMines);
    }

    public void removeZeroBlock(int x, int y) throws InterruptedException {
        LinkedBlockingQueue<Integer> toVisit = new LinkedBlockingQueue<Integer>(gridSize*gridSize);
//        LinkedHashSet<Coordinate> visited = new LinkedHashSet<Coordinate>();

        toVisit.put(new Coordinate(x, y).coordToInt(gridSize));
//        boolean first = (grid[y][x].getValue() != 0); // check that the first clicked element is not 0

        while (!toVisit.isEmpty()) {
            int thing = toVisit.poll();
            Coordinate it = new Coordinate().intToCoord(thing, gridSize);
//            Coordinate[] coords = new Coordinate[8];

            Log.i("LOG_ELEMENT", "NEW ELEMENT----------------------- \n at " + it.getY() + ' ' + it.getX());

            int val = grid[it.getY()][it.getX()].getValue();
//            Log.i("LOG_THE_VALUE", Integer.toString(val));

            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    try {
                        // check if index does not exceed bounds. Should fail if it does
                        Cell cell = grid[it.getY() + i][it.getX() + j];
                        Log.i("LOG_VALUE", "BEFORE \n" + (it.getY() + i) + " " + (it.getX() + j)
                                + " int " + it.coordToInt(gridSize) + "\n val: " + val + " cur: "
                                + cell.toString() + "\n click: " + cell.isClicked()
                                + " flag: " + cell.isFlagged() + "\n val zero: " + (val == 0)
                                + " cell zero: " + (cell.getValue() == 0));

                        int p = new Coordinate(it.getX() + j, it.getY() + i).coordToInt(gridSize);
                        if (!cell.isClicked() && !cell.isFlagged()) {
                            if (cell.getValue() == 0) {
//                            coords[count++] = new Coordinate(y + i, x + j);
                                toVisit.put(p);
                                grid[it.getY() + i][it.getX() + j].toggleClicked();
                            } else if (val == 0) {
//                                if (first) {
//                                    toVisit.put(new Coordinate(it.getY() + i, it.getX() + j).coordToInt(gridSize));
//                                    first = false;
//                                }
                                grid[it.getY() + i][it.getX() + j].toggleClicked();
                            }
                        }
                        Log.i("LOG_VALUE", "\n" +  (it.getY() + i) + " " + (it.getX() + j)
                                + " intcoord: " + p + " val: " + val + " cur: " +
                                cell.toString() + "\n click: " + cell.isClicked()
                                + " flag: " + cell.isFlagged() + "\n val zero: " + (val == 0)
                                + " cell zero: " + (cell.getValue() == 0));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // deal with the instances that the cell at [y+i][x+j] is next to a wall
                        // and does not have a cell at the location
                        Log.i("LOG_OUTOFBOUNDS", "Out of bounds at " + (it.getY() + i) + " " + (it.getX() + j));
                        continue;
                    }
                }
            }
        }
    }

}
