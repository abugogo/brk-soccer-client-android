package com.soccer.indoorstats.activity.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.markupartist.android.widget.ActionBar.Action;
import com.soccer.dal.entities.PrintableLineup;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.imageListUtils.TeamSelectionAdapter;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.services.PlayerService;

public class TeamSelectionActivity extends ListActivity {

	ListView list;
	TeamSelectionAdapter adapter;
	private ArrayList<DAOPlayer> mPList = null;
	private LinkedHashMap<String, PrintableLineup> mLList = null;
	private PlayerService mBoundService;
	private boolean mIsBound;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_selection_layout);

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		String title = "Teams Lineup";
		actionBar.setTitle(title);

		final Action okAction = new OkAction();
		actionBar.addAction(okAction);
		
		final Action cancelAction = new CancelAction();
		actionBar.addAction(cancelAction);
		
		Intent caller = getIntent();
		mLList = new LinkedHashMap<String, PrintableLineup>();
		@SuppressWarnings("unchecked")
		HashMap<String, PrintableLineup> map = (HashMap<String, PrintableLineup>)caller.getExtras().get("llist");
		if(map != null) {
			mLList.putAll(map);
		}
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
		adapter = new TeamSelectionAdapter(this, mPList, mLList);
		list.setAdapter(adapter);
	}
	
	private class OkAction extends AbstractAction {
		public OkAction() {
			super(R.drawable.v);
		}

		@Override
		public void performAction(View view) {
			Intent appIntent = new Intent();
			appIntent.putExtra("llist", adapter.getLpdata());
			setResult(RESULT_OK, appIntent);
			finish();
		}
	}
	
	private class CancelAction extends AbstractAction {
		public CancelAction() {
			super(R.drawable.x);
		}

		@Override
		public void performAction(View view) {
			finish();
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