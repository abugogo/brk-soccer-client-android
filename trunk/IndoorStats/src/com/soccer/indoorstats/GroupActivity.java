package com.soccer.indoorstats;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter.ViewBinder;

import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.imageListUtils.LazyAdapter;

public class GroupActivity extends ListActivity {

	PlayersDbAdapter mDbHelper = null;
	ListView list;
    LazyAdapter adapter;

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
		ArrayList<HashMap<String, String>> pList = new ArrayList<HashMap<String, String>>();
		// Get all of the rows from the database and create the item list
		Cursor PlayersCursor = mDbHelper.fetchAllPlayers();
		if (PlayersCursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(PlayersDbAdapter.KEY_FNAME, PlayersCursor.getString(4));
				map.put(PlayersDbAdapter.KEY_LNAME, PlayersCursor.getString(5));
				map.put(PlayersDbAdapter.KEY_TEL1, PlayersCursor.getString(6));
				map.put(PlayersDbAdapter.KEY_IMG, PlayersCursor.getString(7));
				pList.add(map);
			} while (PlayersCursor.moveToNext());
		}
		if (PlayersCursor != null && !PlayersCursor.isClosed()) {
			PlayersCursor.close();
		}

		list = (ListView) findViewById(android.R.id.list);

		// Getting adapter by passing xml data ArrayList
		adapter = new LazyAdapter(this, pList);
		list.setAdapter(adapter);

		// Click event for single list row
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}

			
		});
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