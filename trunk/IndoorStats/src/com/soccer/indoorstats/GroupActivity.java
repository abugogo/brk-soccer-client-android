package com.soccer.indoorstats;

import java.io.InputStream;
import java.net.URL;

import android.app.ListActivity;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;

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
		String[] from = new String[] { PlayersDbAdapter.KEY_IMG,
				PlayersDbAdapter.KEY_FNAME, PlayersDbAdapter.KEY_LNAME };

		// and an array of the fields we want to bind those fields to
		int[] to = new int[] { R.id.player_icon, R.id.editPFName,
				R.id.editPLName };

		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter players = new SimpleCursorAdapter(this,
				R.layout.player_row, PlayersCursor, from, to);
		players.setViewBinder(new IconViewBinder());

		setListAdapter(players);
	}

	private static class IconViewBinder implements ViewBinder {

		public boolean setViewValue(View view, Cursor c, int colIndex) {
			if (view instanceof ImageView) {
				String src = c.getString(7);
				if (src != null && !src.equals("")) {
					try {
						InputStream is = (InputStream) new URL(src)
								.getContent();
						Drawable d = Drawable.createFromStream(is,
								"player_icon.png");
						((ImageView) view).setImageDrawable(d);
						return true;
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}
			return false;
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("State", 3);
	}
}