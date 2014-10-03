package hu.ait.tiffanynguyen.minesweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Displays the Minesweeper game
 * by Tiffany Nguyen
 */

public class MinesweeperView extends View {

    /*------------------+
     | Global variables |
     +------------------*/

    private int gridSize = 10;

    private Paint paintBg;
    private Paint paintLine;
    private Paint paintMine;
    private Paint paintFlag;
    private Paint paintText;

    private boolean drawFlag;
    private static final float MY_TEXT_SIZE = 30.0f;

    /*------------------+
     |   Constructor    |
     +------------------*/

    public MinesweeperView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintBg = new Paint();
        paintBg.setStyle(Paint.Style.FILL);
        paintBg.setColor(Color.GRAY);

        paintLine = new Paint();
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setColor(Color.WHITE);
        paintLine.setStrokeWidth(1);

        paintMine = new Paint();
        paintMine.setStyle(Paint.Style.STROKE);
        paintMine.setColor(Color.RED);
        paintMine.setStrokeWidth(3);

        paintFlag = new Paint();
        paintFlag.setStyle(Paint.Style.FILL);
        paintFlag.setColor(Color.YELLOW);

        paintText = new Paint();
        paintText.setColor(Color.BLACK);
        final float scale = getResources().getDisplayMetrics().density;
        int textSizePx = (int) (MY_TEXT_SIZE * scale + 0.5f);
        paintText.setTextSize(textSizePx);
        paintText.setAntiAlias(true);

        MinesweeperModel instance = MinesweeperModel.getInstance();
        instance.initializeGrid(gridSize);
    }

    /*------------------+
     |      Methods     |
     +------------------*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBg);
        drawGameArea(canvas);
        drawPlays(canvas);
    }

    private void drawGameArea(Canvas canvas) {
        double xOffset = getWidth() / (double) gridSize;
        double yOffset = getHeight() / (double) gridSize;
        double xCoord = xOffset;
        double yCoord = yOffset;
        for (int i = 0; i < gridSize - 1; i++) {
            canvas.drawLine((int) xCoord, 0, (int) xCoord, getHeight(), paintLine);
            canvas.drawLine(0, (int) yCoord, getWidth(), (int) yCoord, paintLine);
            xCoord += xOffset;
            yCoord += yOffset;
        }
        invalidate();
    }

    private void drawPlays(Canvas canvas) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Cell instance = MinesweeperModel.getInstance().getFieldContent(i, j);
                if (instance.isClicked()) {
                    if (instance.getValue() == MinesweeperModel.MINE) {
                        canvas.drawLine(i * getWidth() / gridSize, j * getHeight() / gridSize,
                                (i + 1) * getWidth() / gridSize,
                                (j + 1) * getHeight() / gridSize, paintMine);

                        canvas.drawLine((i + 1) * getWidth() / gridSize, j * getHeight() / gridSize,
                                i * getWidth() / gridSize, (j + 1) * getHeight() / gridSize, paintMine);
                    } else {
                        canvas.drawText(instance.toString(),
                                scaleToGrid(i, j + 1).getX() + (getWidth() / gridSize) / 5,
                                scaleToGrid(i, j + 1).getY() - (getHeight() / gridSize) / 8,
                                paintText);

                    }
                } if (instance.isFlagged()) {
                    Point point1_draw = new Point(scaleToGrid(i,j).getX(),scaleToGrid(i,j+1).getY());
                    Point point2_draw = new Point(scaleToGrid(i ,j).getX(),scaleToGrid(i,j).getY());
                    Point point3_draw = new Point(scaleToGrid(i+1, j).getX(), scaleToGrid(i,j+1).getY());
                    Path path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    path.moveTo(point1_draw.x,point1_draw.y);
                    path.lineTo(point2_draw.x,point2_draw.y);
                    path.lineTo(point3_draw.x,point3_draw.y);
                    path.lineTo(point1_draw.x,point1_draw.y);
                    path.close();
                    canvas.drawPath(path, paintFlag);
                }
            }
        }
    }

    private Coordinate scaleToGrid(int x, int y) {
        return new Coordinate((x * getWidth()) / gridSize, (y * getHeight()) / gridSize);
    }

    public boolean setFlagger(boolean f) {
        drawFlag = f;
        return drawFlag;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int tX = ((int) event.getX()) / (getWidth() / gridSize);
            int tY = ((int) event.getY()) / (getHeight() / gridSize);

            Cell msModel = MinesweeperModel.getInstance().getFieldContent(tX, tY);
            if (tX < gridSize && tY < gridSize && !msModel.isClicked()) {
                if (drawFlag == false) {
                    if (!msModel.isFlagged()) {
                        msModel.toggleClicked();
                        if (msModel.getValue() == MinesweeperModel.MINE) {
                            MinesweeperModel.getInstance().clickAll();
                            Toast.makeText(getContext(), "You lose...", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    msModel.toggleFlagged();
                }
            }
            invalidate();
        }

        return true;
    }

    // to keep the width and height as a square
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = (w == 0) ? h : (h == 0) ? w : (w < h) ? w : h;
        setMeasuredDimension(d, d);
    }
}
