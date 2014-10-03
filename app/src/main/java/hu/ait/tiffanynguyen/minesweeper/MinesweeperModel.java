package hu.ait.tiffanynguyen.minesweeper;


import java.util.Random;

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
    private int numberOfMines = 10;

    private Cell grid[][]; // stores the data of a cell
    private int gridSize; // the dimensions of the grid

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
            resetGrid();
        }
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
    public void fillGrid() {
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
    public void generateMines() {
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

    // fills the units around every cell to indicate it is adjacent to a mine
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
            }
        }
    }

}
