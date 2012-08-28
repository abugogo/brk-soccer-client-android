package com.soccer.indoorstats;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

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
				
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View vi=view;
		        if(view==null)
		            vi = inflater.inflate(R.layout.player_row, null);
		        TextView tel = (TextView)vi.findViewById(R.id.ptel1);
		        /*Intent intent = new Intent(Intent.ACTION_CALL);
		        intent.setData(Uri.parse("tel:" + tel.getText()));*/
				
		        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + tel.getText()));
                startActivity(intent);


			}

			
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("State", 3);
	}
}