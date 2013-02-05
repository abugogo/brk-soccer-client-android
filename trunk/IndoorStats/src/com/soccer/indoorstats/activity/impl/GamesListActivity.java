package com.soccer.indoorstats.activity.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.View;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.soccer.entities.DAOGameListEntry;
import com.soccer.entities.EntityManager;
import com.soccer.entities.IDAOGame.GameStatus;
import com.soccer.entities.impl.DAOGame;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.adapters.GamesListAdapter;
import com.soccer.indoorstats.services.GameService;
import com.soccer.indoorstats.services.handlers.RequestHandler;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.preferences.Prefs;

public class GamesListActivity extends ListActivity {

	GamesListAdapter adapter;
	private LinkedList<DAOGameListEntry> mGList;
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
		final ListView list = (ListView) findViewById(android.R.id.list);
		actionBar.setProgressBarVisibility(View.VISIBLE);
		mBoundService.getAllGames(0, new RequestHandler<String>() {

			@Override
			public void onSuccess(String sgames) {
				Logger.i("Games retrievl success");
				long t1 = SystemClock.elapsedRealtime();
				ArrayList<DAOGame> gArr = EntityManager.readGames(sgames);
				long interval = SystemClock.elapsedRealtime() - t1;
				Logger.i("Games readgames done in " + interval);
				
				if (gArr != null) {
					Date lastDate = null;
					DAOGameListEntry ge;
					for (DAOGame g : gArr) {
						if(!sameDay(g.getGameDate(), lastDate)) {
							ge = new DAOGameListEntry(true, g.getGameDate(), g.getStatus());
							mGList.add(ge);
							lastDate = g.getGameDate();
						}
						ge = new DAOGameListEntry(g, false);
						mGList.add(ge);
					}

					adapter = new GamesListAdapter(GamesListActivity.this,
							mGList);
					list.setAdapter(adapter);
				}
				actionBar.setProgressBarVisibility(View.INVISIBLE);
				
			}

			@Override
			public void onFailure(String reason, int errorCode) {
				Logger.i("Games fetching failure");
				actionBar.setProgressBarVisibility(View.INVISIBLE);
				DlgUtils.showAlertMessage(GamesListActivity.this,
						"Failed Games Retrieval", reason);
			}
		});

	}

	private boolean sameDay(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return false;
		return (d1.getDay() == d2.getDay() && d1.getMonth() == d2.getMonth() && d1
				.getYear() == d2.getYear());
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mBoundService = (GameService) ((GameService.LocalBinder) service)
					.getService();
			ArrayList<GameStatus> ags = new ArrayList<GameStatus>();
			ags.add(GameStatus.Failed);
			ags.add(GameStatus.Pending);
			List<DAOGame> lstFailed = mBoundService.getGames(ags);
			mGList = new LinkedList<DAOGameListEntry>();
			if (lstFailed != null) {
				GameStatus gs = GameStatus.Success;
				for (DAOGame g : lstFailed) {
					DAOGameListEntry ge;
					if (g.getStatus() != gs) {
						ge = new DAOGameListEntry(true, g.getGameDate(),
								g.getStatus());
						mGList.add(ge);
						gs = g.getStatus();
					}
					ge = new DAOGameListEntry(g, false);
					mGList.add(ge);
				}
			}

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