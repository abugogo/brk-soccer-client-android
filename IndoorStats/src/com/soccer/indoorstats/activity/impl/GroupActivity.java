package com.soccer.indoorstats.activity.impl;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.imageListUtils.LazyAdapter;
import com.soccer.indoorstats.R;

public class GroupActivity extends ListActivity {

	PlayersDbAdapter mDbHelper = null;
	ListView list;
	LazyAdapter adapter;
	private MenuExtender slidingMenu;
	private ArrayList<DAOPlayer> mPList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_layout);
		if (slidingMenu == null) {
			slidingMenu = new MenuExtender(this, "");
			slidingMenu.initSlideMenu();
		}
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		mDbHelper = new PlayersDbAdapter(this);
		mDbHelper.open();
		mPList = mDbHelper.fetchAllPlayersAsArray();
		mDbHelper.close();
		fillData();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	private void fillData() {

		list = (ListView) findViewById(android.R.id.list);

		// Getting adapter by passing xml data ArrayList
		adapter = new LazyAdapter(this, mPList);
		list.setAdapter(adapter);

		// Click event for single list row
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int arg2, long arg3) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View vi = view;
				if (view == null)
					vi = inflater.inflate(R.layout.player_row, null);
				TextView tel = (TextView) vi.findViewById(R.id.ptel1);
				/*
				 * Intent intent = new Intent(Intent.ACTION_CALL);
				 * intent.setData(Uri.parse("tel:" + tel.getText()));
				 */

				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
						+ tel.getText()));
				startActivity(intent);

				return false;
			}
		});

		list.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				return slidingMenu.handleTouchEvent(event);
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("State", 3);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return slidingMenu.handleTouchEvent(event);
	}

}