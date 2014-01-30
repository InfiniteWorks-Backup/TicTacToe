package com.infiniteworks.tictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class GridView extends View{

	//Default Constructors needed to be called!
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
	private void initialize(){
		
		//Draw thicker lines!
		paint = new Paint();
		paint.setStrokeWidth(10f);
		
		//TODO: Randomize this later
		playerOneTurn = true;
		
		//gridData[0] is the top left corner
		gridData = new int[3][3]; //Create a new 3x3 grid filled with 0's

		cellBoundary = new RectF();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		height = canvas.getHeight();
		width = canvas.getWidth();
		
		//Draw a few lines in the View
		canvas.drawLine(0, height / 3f, width, height / 3f, paint);
		canvas.drawLine(0, 2f * height / 3f, width, 2f * height / 3f, paint);
		canvas.drawLine(width / 3f, 0, width / 3f, height, paint);
		canvas.drawLine(2f * width / 3f, 0, 2f * width / 3f, height, paint);
		//Padding;  Modifier so that the player markers have a little
		// white space on each cell 
		float padding = 10f;
		
		//Search through the entire array & draw the player's markers
		// where appropriate
		for(int cellNumRow = 0; cellNumRow < gridData.length; cellNumRow++){
			for(int cellNumCol = 0; cellNumCol < gridData[0].length; cellNumCol++){
				if(gridData[cellNumRow][cellNumCol] == 1){
					//Create the boundary the cell needs to adhere by
					//TODO:  Wrap the cell into it's own class
					// and pass boundaries to it for drawing
					float left = padding + (cellNumCol * width / 3f);
					float top = padding + (cellNumRow * height / 3f);
					float right = ((cellNumCol + 1) * width / 3f) - padding;
					float bottom = ((cellNumRow + 1) * height / 3f) - padding;
					cellBoundary.set(left, top, right, bottom);
					drawPlayerOneMarker(canvas, cellBoundary, paint);
					continue;
				}
				if(gridData[cellNumRow][cellNumCol] == 2){
					
					float left = padding + (cellNumCol * width / 3f);
					float top = padding + (cellNumRow * height / 3f);
					float right = ((cellNumCol + 1) * width / 3f) - padding;
					float bottom = ((cellNumRow + 1) * height / 3f) - padding;
					cellBoundary.set(left, top, right, bottom);
					drawPlayerTwoMarker(canvas, cellBoundary, paint);
				}
			}
		}
		super.onDraw(canvas);
	}
	private void drawPlayerOneMarker(Canvas canvas, RectF boundary, Paint paint){
		canvas.drawLine(boundary.left, boundary.top, boundary.right, boundary.bottom, paint);
		canvas.drawLine(boundary.left, boundary.bottom, boundary.right, boundary.top, paint);
	}
	
	private void drawPlayerTwoMarker(Canvas canvas, RectF boundary, Paint paint){
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawOval(boundary, paint);
	}
	
	private int[] getCellLocation(float x, float y, float width, float height){
		int[] result = new int[]{-1,-1};
		result[1] = (int)(x / (width / 3f));
		result[0] = (int)(y / (height / 3f));
		return result;
	}
	
	public static boolean playerOneTurn;
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		int x = (int)event.getX();
		int y = (int)event.getY();
//		Log.i("Position","Touch detected at position (" + x + "," + y + ")");
		int[] cellPosition = getCellLocation(x, y, width, height);

		//Attempt to make the move
		if(makeMove(cellPosition[0],cellPosition[1])){
			playerOneTurn = !playerOneTurn;
			
			View textView = getRootView().findViewWithTag("playerTurnLabel");
			if(textView instanceof TextView){
				if(playerOneTurn)
					((TextView)textView).setText("Player X's Turn");
				else
					((TextView)textView).setText("Player O's Turn");
			}
			//Forces the Activity to refresh itself and all 
			// of its Views
			invalidate();
		}
		
		return false;
	}
	/*
	 * Returns a value if a player has won
	 * -1 for no win
	 * 0 for tie
	 * 1 for Player 1 Win
	 * 2 for Player 2 Win
	 */
	public int playerWin(){
		
		return -1;
	}
	
	//Refactored so that other entities can call this method
	// if allowed (i.e. AI bot)
	public boolean makeMove(int cellRow, int cellColumn){
		//First, validate if the move is allowed;
		// return false if move is invalid
		if(gridData[cellRow][cellColumn] != 0) return false;
		gridData[cellRow][cellColumn] = isPlayerOneTurn() ? 1 : 2;
		return true;
	}
	
	public boolean isPlayerOneTurn(){
		return this.playerOneTurn;
	}
}
