package com.soccer.indoorstats;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

import com.soccer.db.local.PlayersDbAdapter;

public class GroupActivity extends ListActivity {

	PlayersDbAdapter mDbHelper = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_layout);
		mDbHelper = new PlayersDbAdapter(this);
		mDbHelper.open();
		fillData();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mDbHelper.close();
	}

	private void fillData() {
		// Get all of the rows from the database and create the item list
		Cursor PlayersCursor = mDbHelper.fetchAllPlayers();
		startManagingCursor(PlayersCursor);

		// Create an array to specify the fields we want to display in the list
		String[] from = new String[] { PlayersDbAdapter.KEY_FNAME, PlayersDbAdapter.KEY_LNAME };

		// and an array of the fields we want to bind those fields to 
		int[] to = new int[] { R.id.editPFName, R.id.editPLName };

		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter players = new SimpleCursorAdapter(this,
				R.layout.player_row, PlayersCursor, from, to);
		setListAdapter(players);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("State", 3);
	}
}