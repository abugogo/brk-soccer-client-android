package com.soccer.indoorstats.activity.impl;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;
import com.soccer.entities.EntityManager;
import com.soccer.entities.IDAOGame.GameStatus;
import com.soccer.entities.impl.DAOGame;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.imageListUtils.GamesListAdapter;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.activity.impl.stats.StatsTabActivity;
import com.soccer.indoorstats.services.GameService;
import com.soccer.indoorstats.services.handlers.RequestHandler;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.preferences.Prefs;

public class GamesListActivity extends ListActivity {

	GamesListAdapter adapter;
	private ArrayList<DAOGame> mGList;
	private GameService mBoundService;
	private boolean mIsBound;
	private ActionBar actionBar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_layout);
		// top action bar
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		Prefs sharedPrefs = new Prefs(this);
		String title = sharedPrefs.getPreference("account_name",
				getString(R.string.group));
		actionBar.setTitle(title);
		final Action HomeAction = new IntentAction(this, new Intent(this,
				HomeActivity.class), R.drawable.home_icon);
		actionBar.addAction(HomeAction);
		// bottom bar
		RadioButton radioButton;
		radioButton = (RadioButton) findViewById(R.id.btnGame);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		radioButton = (RadioButton) findViewById(R.id.btnGroup);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		radioButton = (RadioButton) findViewById(R.id.btnSeason);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		radioButton = (RadioButton) findViewById(R.id.btnStats);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}

	private CompoundButton.OnCheckedChangeListener btnNavBarOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				Toast.makeText(GamesListActivity.this, buttonView.getText(),
						Toast.LENGTH_SHORT).show();

				Intent appIntent = null;

				switch (buttonView.getId()) {
				case R.id.btnGame:
					appIntent = new Intent(GamesListActivity.this,
							GameActivity.class);
					break;
				case R.id.btnSeason:
					Toast.makeText(GamesListActivity.this, "not implemented",
							Toast.LENGTH_SHORT).show();
					break;
				case R.id.btnStats:
					appIntent = new Intent(GamesListActivity.this,
							StatsTabActivity.class);
					break;
				}
				if (appIntent != null)
					startActivity(appIntent);

			}
		}
	};

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
		final ListView list = (ListView) findViewById(android.R.id.list);
		actionBar.setProgressBarVisibility(View.VISIBLE);
		mBoundService.getAllGames(0, new RequestHandler<JSONArray>() {

			@Override
			public void onSuccess(JSONArray arr) {
				Logger.i("Games retrievl success");
				ArrayList<DAOGame> gArr = EntityManager.readGames(arr.toString());
				mGList.addAll(gArr);
				actionBar.setProgressBarVisibility(View.INVISIBLE);
				adapter = new GamesListAdapter(GamesListActivity.this, mGList);
				list.setAdapter(adapter);
			}

			@Override
			public void onFailure(String reason, int errorCode) {
				Logger.i("Games fetching failure");
				actionBar.setProgressBarVisibility(View.INVISIBLE);
				DlgUtils.showAlertMessage(GamesListActivity.this, "Failed Games Retrieval", reason);
			}
		});
		
		
	}	

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mBoundService = (GameService) ((GameService.LocalBinder) service)
					.getService();
			ArrayList<GameStatus> ags = new ArrayList<GameStatus>();
			ags.add(GameStatus.Failed);
			ags.add(GameStatus.Pending);
			mGList = mBoundService.getGames(ags);
			fillData();
		}

		public void onServiceDisconnected(ComponentName className) {
			mBoundService = null;
		}
	};

	private void doBindService() {
		bindService(new Intent(GamesListActivity.this, GameService.class),
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