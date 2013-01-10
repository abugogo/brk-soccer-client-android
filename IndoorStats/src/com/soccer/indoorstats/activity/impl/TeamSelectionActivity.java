package com.soccer.indoorstats.activity.impl;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.markupartist.android.widget.ActionBar.Action;
import com.soccer.entities.impl.DAOLineup;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.imageListUtils.TeamSelectionAdapter;
import com.soccer.indoorstats.R;

public class TeamSelectionActivity extends ListActivity {

	ListView list;
	TeamSelectionAdapter adapter;
	private ArrayList<DAOPlayer> mPList = null;
	private HashMap<String, DAOLineup> mLList = null;

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
		mLList = (HashMap<String, DAOLineup>)caller.getExtras().get("llist");
		mPList = (ArrayList<DAOPlayer>)caller.getExtras().get("plist");
		fillData();
	}

	@Override
	protected void onResume() {
		
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
			Intent appIntent = new Intent(TeamSelectionActivity.this, GameActivity.class);
			appIntent.putExtra("llist", adapter.getLpdata());
			startActivity(appIntent);
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
}