package com.soccer.indoorstats.activity.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.adapters.LazyAdapter;
import com.soccer.indoorstats.services.PlayerService;
import com.soccer.preferences.Prefs;

public class GroupActivity extends ListActivity {

	ListView list;
	LazyAdapter adapter;
	private LinkedList<DAOPlayer> mPList;
	private int sign = 1;
	private PlayerService mBoundService;
	private boolean mIsBound;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_layout);
		// top action bar
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		Prefs sharedPrefs = new Prefs(this);
		String title = sharedPrefs.getPreference("account_name",
				getString(R.string.group));
		actionBar.setTitle(title);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}

	@Override
	protected void onPause() {
		doUnbindService();
		super.onPause();
	}

	@Override
	protected void onResume() {
		doBindService();
		super.onResume();
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
	}

	public void onSort(View v) {
		Comparator<DAOPlayer> comp = new SortByName();
		Collections.sort(mPList, comp);
		adapter.notifyDataSetChanged();
		sign *= -1;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("State", 3);
	}

	private final class SortByName implements Comparator<DAOPlayer> {
		public int compare(DAOPlayer i1, DAOPlayer i2) {
			return i1.getFname().compareTo(i2.getFname()) * sign;
		}
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mBoundService = (PlayerService) ((PlayerService.LocalBinder) service)
					.getService();
			mPList = mBoundService.getAllPlayers();
			fillData();
		}

		public void onServiceDisconnected(ComponentName className) {
			mBoundService = null;
		}
	};

	private void doBindService() {
		bindService(new Intent(GroupActivity.this, PlayerService.class),
				mConnection, Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	private void doUnbindService() {
		if (mIsBound) {
			unbindService(mConnection);
			mIsBound = false;
		}
	}
}