package com.infiniteworks.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CellView extends View {
	private Integer cellValue;

	public CellView(Context context) {
		super(context);
	}

	public CellView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CellView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public int getCellValue() {
		if (this.cellValue == null)
			return 0;
		return this.cellValue;
	}

	public void setCellValue(int cellValue) {
		this.cellValue = cellValue;
	}

}
