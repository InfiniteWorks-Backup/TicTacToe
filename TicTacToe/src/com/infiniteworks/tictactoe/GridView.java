package com.infiniteworks.tictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import java.util.Random;

public class GridView extends View {

    private final int PLAYER_ONE_VALUE = 1;
    private final int PLAYER_TWO_VALUE = 10;
    private static Random randBool = new Random();

    // Default Constructors needed to be called!
    public GridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public GridView(Context context) {
        super(context);
        initialize();
    }

    private int[][] gridData;
    private RectF cellBoundary;
    private int width, height;
    private Paint paint;

    public static boolean getRandomBoolean() {
        return randBool.nextBoolean();
    }

    private void initialize() {

        // Draw thicker lines!
        paint = new Paint();
        paint.setStrokeWidth(10f);

        //Randomized
        playerOneTurn = getRandomBoolean();

        // gridData[0] is the top left corner
        gridData = new int[3][3]; // Create a new 3x3 grid filled with 0's

        cellBoundary = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        height = canvas.getHeight();
        width = canvas.getWidth();

        // Draw a few lines in the View
        canvas.drawLine(0, height / 3f, width, height / 3f, paint);
        canvas.drawLine(0, 2f * height / 3f, width, 2f * height / 3f, paint);
        canvas.drawLine(width / 3f, 0, width / 3f, height, paint);
        canvas.drawLine(2f * width / 3f, 0, 2f * width / 3f, height, paint);
        // Padding; Modifier so that the player markers have a little
        // white space on each cell
        float padding = 25f;

        // Search through the entire array & draw the player's markers
        // where appropriate
        for (int cellNumRow = 0; cellNumRow < gridData.length; cellNumRow++) {
            for (int cellNumCol = 0; cellNumCol < gridData[0].length; cellNumCol++) {
                if (gridData[cellNumRow][cellNumCol] == PLAYER_ONE_VALUE) {
                    // Create the boundary the cell needs to adhere by
                    // TODO: Wrap the cell into it's own class
                    // and pass boundaries to it for drawing
                    float left = padding + (cellNumCol * width / 3f);
                    float top = padding + (cellNumRow * height / 3f);
                    float right = ((cellNumCol + 1) * width / 3f) - padding;
                    float bottom = ((cellNumRow + 1) * height / 3f) - padding;
                    cellBoundary.set(left, top, right, bottom);
                    drawPlayerOneMarker(canvas, cellBoundary, paint);
                    continue;
                }
                if (gridData[cellNumRow][cellNumCol] == PLAYER_TWO_VALUE) {

                    float left = padding + (cellNumCol * width / 3f);
                    float top = padding + (cellNumRow * height / 3f);
                    float right = ((cellNumCol + 1) * width / 3f) - padding;
                    float bottom = ((cellNumRow + 1) * height / 3f) - padding;
                    cellBoundary.set(left, top, right, bottom);
                    drawPlayerTwoMarker(canvas, cellBoundary, paint);
                }
            }
        }

        // With the Grid drawn and any player markers, check to see if a player
        // has won
        int[] playerWin = playerWin();
        if (playerWin[0] != -1) {


            View textView = getRootView().findViewWithTag("playerTurnLabel");
            if (textView instanceof TextView) {

                switch (playerWin[0]) {
                    case 0:
                        ((TextView) textView).setText("The game is a Tie!");
                        paint.setColor(Color.RED);
                        break;
                    case 1:
                        ((TextView) textView).setText("Player 1 Wins!");
                        paint.setColor(Color.BLUE);
                        break;
                    case 2:
                        ((TextView) textView).setText("Player 2 Wins!");
                        paint.setColor(Color.RED);
                        break;
                    default:
                        ((TextView) textView).setText("Something is wrong with the game!");
                }
            }
            float startX = (playerWin[1] * 2f) * width / 6f;
            float startY = (playerWin[2] * 2f) * height / 6f;
            float stopX = (playerWin[3] * 2f + 2) * width / 6f;
            float stopY = (playerWin[4] * 2f + 2) * height / 6f;
            canvas.drawLine(startX, startY, stopX, stopY, paint);

        }
        super.onDraw(canvas);
    }

    private void drawPlayerOneMarker(Canvas canvas, RectF boundary, Paint paint) {
        canvas.drawLine(boundary.left, boundary.top, boundary.right, boundary.bottom, paint);
        canvas.drawLine(boundary.left, boundary.bottom, boundary.right, boundary.top, paint);
    }

    private void drawPlayerTwoMarker(Canvas canvas, RectF boundary, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawOval(boundary, paint);
    }

    private int[] getCellLocation(float x, float y, float width, float height) {
        int[] result = new int[] { -1, -1 };
        result[1] = (int) (x / (width / 3f));
        result[0] = (int) (y / (height / 3f));
        return result;
    }

    public static boolean playerOneTurn;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // We don't want to do anything if the game is finished!
        if (playerWin()[0] != -1)
            return true;
        int x = (int) event.getX();
        int y = (int) event.getY();
        // Log.i("Position","Touch detected at position (" + x + "," + y + ")");
        int[] cellPosition = getCellLocation(x, y, width, height);

        // Attempt to make the move
        if (makeMove(cellPosition[0], cellPosition[1])) {
            playerOneTurn = !playerOneTurn;

            View textView = getRootView().findViewWithTag("playerTurnLabel");
            if (textView instanceof TextView) {
                if (playerOneTurn)
                    ((TextView) textView).setText("Player X's Turn");
                else
                    ((TextView) textView).setText("Player O's Turn");
            }
            // Forces the Activity to refresh itself and all
            // of its Views
            invalidate();
        }

        return false;
    }

    /*
     * Returns a value if a player has won -1 for no win 0 for tie 1 for Player
     * 1 Win 2 for Player 2 Win
     *
     * @return int[]: [0] = player win [1] = startCellX [2] = startCellY [3] =
     * endCellX [4] = endCellY
     */
    public int[] playerWin() {

        int player_one_win_value = PLAYER_ONE_VALUE * 3;
        int player_two_win_value = PLAYER_TWO_VALUE * 3;
        // Use to track if empty tiles exist
        boolean emptyTiles = false;
        // Check all the tiles to see if an empty one exists.
        // If they do, then break out of the loops early and move on!
        for (int r = 0; r < gridData.length; r++) {
            for (int c = 0; c < gridData[0].length; c++) {
                if ((emptyTiles = gridData[r][c] == 0))
                    break;
            }
            if (emptyTiles)
                break;
        }

        // If a line value is equal to (3*PlayerValue), in this case 3 or 6
        // the game has been finished!
        int lineValue = 0;

        // Loop through all rows horizontally
        for (int r = 0; r < gridData.length; r++) {
            lineValue = gridData[r][0] + gridData[r][1] + gridData[r][2];
            if (lineValue == player_one_win_value)
                return new int[] { 1, 0, r, 2, r };
            if (lineValue == player_two_win_value)
                return new int[] { 2, 0, r, 2, r };
        }

        // Loop through all rows vertically
        for (int c = 0; c < gridData[0].length; c++) {
            lineValue = gridData[0][c] + gridData[1][c] + gridData[2][c];
            if (lineValue == player_one_win_value)
                return new int[] { 1, c, 0, c, 2 };
            if (lineValue == player_two_win_value)
                return new int[] { 2, c, 0, c, 2 };
        }

        // Check both diagonals
        lineValue = gridData[0][0] + gridData[1][1] + gridData[2][2];
        if (lineValue == player_one_win_value)
            return new int[] { 1, 0, 0, 2, 2 };
        if (lineValue == player_two_win_value)
            return new int[] { 2, 0, 0, 2, 2 };

        lineValue = gridData[2][0] + gridData[1][1] + gridData[0][2];
        if (lineValue == player_one_win_value)
            return new int[] { 1, 2, 0, 0, 2 };

        if (lineValue == player_two_win_value)
            return new int[] { 2, 2, 0, 0, 2 };

        // Check if emptyTiles is still false; If so, return a tie value!
        if (!emptyTiles)
            return new int[] { 0, -1, -1, -1, -1 };

        return new int[] { -1, -1, -1, -1, -1 };
    }

    // Refactored so that other entities can call this method
    // if allowed (i.e. AI bot)
    public boolean makeMove(int cellRow, int cellColumn) {
        // First, validate if the move is allowed;
        // return false if move is invalid
        if (gridData[cellRow][cellColumn] != 0)
            return false;
        gridData[cellRow][cellColumn] = isPlayerOneTurn() ? PLAYER_ONE_VALUE : PLAYER_TWO_VALUE;
        return true;
    }

    public boolean isPlayerOneTurn() {
        return this.playerOneTurn;
    }
}
