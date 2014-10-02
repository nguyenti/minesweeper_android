package hu.ait.tiffanynguyen.minesweeper;

import android.util.Log;

import java.util.Random;

/**
 * Created by tiffanynguyen on 9/22/14.
 */
public class MinesweeperModel {

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

    private int numberOfMines = 10;

    private Unit grid[][];
    private int gridSize;

    public static final int MINE = -1;

    public void initializeGrid(int size) {
        if (grid == null) {
            grid = new Unit[size][size];
            gridSize = size;
            resetGrid();
        }
    }

    private void zeroGrid() {
        for (int j = 0; j < gridSize; j++) {
            for (int i = 0; i < gridSize; i++) {
                grid[j][i] = new Unit();
            }
        }
    }

    public void clickAll() {
        for (int j = 0; j < gridSize; j++) {
            for (int i = 0; i < gridSize; i++) {
                if (!grid[j][i].isClicked()) {
                    grid[j][i].setClicked();
                }
            }
        }
    }

    public void resetGrid() {
        zeroGrid();
        fillGrid();
    }

    public Unit getFieldContent(int x, int y) {
        return grid[y][x];
    }

    public void setFieldContent(Coordinate c, int content) {
       grid[c.getY()][c.getX()].setValue(content);
    }

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

    private void fillAround(int x, int y) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                try {
                    if (grid[y + i][x + j].getValue() != -1) {
                        grid[y + i][x + j].incrementValue();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
    }
}
