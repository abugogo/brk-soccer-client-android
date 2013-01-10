package com.soccer.indoorstats.activity.impl;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ListView;

import com.soccer.entities.impl.DAOLineup;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.imageListUtils.TeamSelectionAdapter;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.services.PlayerService;

public class TeamSelectionActivity extends ListActivity {

	ListView list;
	TeamSelectionAdapter adapter;
	private ArrayList<DAOPlayer> mPList;
	private PlayerService mBoundService;
	private boolean mIsBound;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_layout);
		
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

		adapter = new TeamSelectionAdapter(this, mPList, new HashMap<String, DAOLineup>());
		list.setAdapter(adapter);
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
		bindService(new Intent(TeamSelectionActivity.this, PlayerService.class),
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