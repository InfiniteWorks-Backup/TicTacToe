package com.infiniteworks.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class TicTacToeMain extends Activity {

    public final static String EXTRA_MSG = "com.infiniteworks.tictactoe.EXTRA_MESG";

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        //Layout that appears after running the app
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_menu);

        //Exit button event to exit from app
        Button btnExit = (Button) findViewById(R.id.bExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tic_tac_toe_main, menu);
		return true;
	}
    // Called when the user clicks the Send button
    public void sendMessage(View view) {
        //Going to assign a message
        Intent intent = new Intent(this, game_window.class);
        String message = "TicTacToeGameWindow_Test";
        intent.putExtra(EXTRA_MSG, message);
        startActivity(intent);
    }
}
