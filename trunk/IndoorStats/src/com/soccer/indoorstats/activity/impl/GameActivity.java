package com.soccer.indoorstats.activity.impl;

/*This code has been created for a demo by Shawn for ShawnBe.com 
 * please do not redistribute this without my permission, you
 * can email me at shawn@shawnbe.com. If these tutorials has helped
 * you please consider donating to me via paypal on my website 
 * ShawnBe.com
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;
import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.db.local.StateDbAdapter;
import com.soccer.db.remote.R_DB_CONSTS;
import com.soccer.db.remote.RemoteDBAdapter;
import com.soccer.dialog.CheckedListAdapter;
import com.soccer.dialog.MultiSelectListDialog;
import com.soccer.dialog.PLineupItems;
import com.soccer.entities.IDAOGame;
import com.soccer.entities.impl.DAOGame;
import com.soccer.entities.impl.DAOLineup;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.activity.i.IAsyncTaskAct;
import com.soccer.indoorstats.activity.states.GameState;
import com.soccer.indoorstats.ingame.IGameEvent.EventType;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.indoorstats.utils.StopWatch;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.preferences.Prefs;

public class GameActivity extends Activity implements OnClickListener,
		IAsyncTaskAct {

	private StopWatch _timer = new StopWatch();
	private GameState _gState = null;
	private StopWatchHandler mHandler = new StopWatchHandler(this);
	private TextView tvTextView;
	private Button btnStart;
	Button btnReset;
	CheckedListAdapter adapter;
	CheckedListAdapter adapter2;
	private PlayersDbAdapter mDbHelper = null;
	private StateDbAdapter mStatesDbHelper = null;
	Prefs sharedPrefs;

	private StateDbAdapter getStatesDbAdapter() {
		if (mStatesDbHelper == null)
			mStatesDbHelper = new StateDbAdapter(this);
		return mStatesDbHelper;
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return _gState;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_main);

		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle(R.string.game);

		/*
		 * final Action PlayerAction = new IntentAction(this, new Intent(this,
		 * PlayerActivity.class), R.drawable.player_icon);
		 * actionBar.addAction(PlayerAction); final Action GroupAction = new
		 * IntentAction(this, new Intent(this, GroupActivity.class),
		 * R.drawable.players_icon); actionBar.addAction(GroupAction);
		 */

		final Action SaveAction = new SaveGameAction();
		actionBar.addAction(SaveAction);

		final Action ResetAction = new ResetGameAction();
		actionBar.addAction(ResetAction);

		final Action HomeAction = new IntentAction(this, new Intent(this,
				HomeActivity.class), R.drawable.home_icon);
		actionBar.addAction(HomeAction);

		tvTextView = (TextView) findViewById(R.id.textViewTimer);

		btnStart = (Button) findViewById(R.id.buttonStart);
		btnReset = (Button) findViewById(R.id.buttonReset);
		btnStart.setOnClickListener(this);
		btnReset.setOnClickListener(this);

		if ((GameState) getLastNonConfigurationInstance() != null)
			_gState = (GameState) getLastNonConfigurationInstance();
		sharedPrefs = new Prefs(this);
	}

	public void createGame() {
		List<DAOLineup> lpList = new ArrayList<DAOLineup>();
		for (PLineupItems pt1 : _gState.get_team1List()) {
			DAOLineup d = new DAOLineup();
			d.setColor('b');
			d.setGoal(pt1.getSumEvents(EventType.Goal));
			d.setOGoal(pt1.getSumEvents(EventType.O_Goal));
			d.setPlayerId(pt1.mId);
			lpList.add(d);
		}
		for (PLineupItems pt2 : _gState.get_team2List()) {
			DAOLineup d = new DAOLineup();
			d.setColor('w');
			d.setGoal(pt2.getSumEvents(EventType.Goal));
			d.setOGoal(pt2.getSumEvents(EventType.O_Goal));
			d.setPlayerId(pt2.mId);
			lpList.add(d);
		}

		IDAOGame daoGame = new DAOGame();
		daoGame.setLineup(lpList);
		daoGame.setBgoals(1);
		daoGame.setGameDate(new Date());
		daoGame.setWgoals(1);
		daoGame.setWinner('b');

		String sUrl = sharedPrefs.getPreference("server_port", "NULL");
		if (sUrl.equals("NULL")) {
			sUrl = R_DB_CONSTS.SERVER_DEFAULT;
		}

		try {
			RemoteDBAdapter.createGame(this, sUrl, "Updating game", daoGame);
		} catch (Exception e) {
			Logger.e("create game failed", e);
			showDialog(0, DlgUtils.prepareDlgBundle(e.getMessage()));
		}

	}

	private class SaveGameAction extends AbstractAction {

		public SaveGameAction() {
			super(R.drawable.save);
		}

		@Override
		public void performAction(View view) {
			createGame();
		}

	}

	private class ResetGameAction extends AbstractAction {

		public ResetGameAction() {
			super(R.drawable.new_file);
		}

		@Override
		public void performAction(View view) {
			resetGame();
		}

	}

	public void resetGame() {
		resetTimer();
		adapter.setData(null);
		adapter2.setData(null);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private int getGameFullTime(int defLength) {
		String ret = sharedPrefs.getPreference("game_length_id",
				String.valueOf(defLength));
		return Integer.parseInt(ret) * 60;
	}

	public void updateTimer() {
		int elapsed = (int) _timer.getElapsedTimeSecs();
		boolean passed = false;
		if (_gState.isBackwards()) {
			elapsed = getGameFullTime(7 * 60) - elapsed;
			if (elapsed < 0) {
				passed = true;
				elapsed = Math.abs(elapsed);
			}
		} else {
			if (elapsed > getGameFullTime(7 * 60))
				passed = true;
		}
		int mins = (int) (elapsed / 60);
		int secs = (int) (elapsed % 60);
		if (tvTextView != null) {
			tvTextView.setText(((mins < 10) ? "0" : "") + mins + ":"
					+ ((secs < 10) ? "0" : "") + secs);
			if (passed)
				tvTextView.setTextColor(Color.RED);
			else
				tvTextView.setTextColor(Color.BLACK);
		}
	}

	public void startTimer() {
		_timer.start(!_gState.isStarted());
		_gState.setStarted(true);
	}

	public void stopTimer() {
		_timer.stop();
	}

	public void resetTimer() {
		stopTimer();
		_gState.setStarted(false);
		if (btnStart != null && tvTextView != null) {
			btnStart.setText("Start");
			tvTextView.setText("00:00");
			tvTextView.setTextColor(Color.BLACK);
		}
	}

	public void onClick(View v) {
		if (btnStart == v) {
			if (btnStart.getText().equals("Start")) {
				mHandler.sendEmptyMessage(StopWatchHandler.MSG_START_TIMER);
				btnStart.setText("Stop");
			} else {
				mHandler.sendEmptyMessage(StopWatchHandler.MSG_STOP_TIMER);
				btnStart.setText("Start");
			}
		} else if (btnReset == v) {
			mHandler.sendEmptyMessage(StopWatchHandler.MSG_RESET_TIMER);
		}

	}

	public void onAddItem(View v) {
		MultiSelectListDialog dlg;
		ArrayList<String> rivalLstIds = new ArrayList<String>();
		if (v == (Button) findViewById(R.id.button2)) {
			for (PLineupItems lI : _gState.get_team2List()) {
				if (lI.mChecked)
					rivalLstIds.add(lI.mId);
			}
			dlg = new MultiSelectListDialog(this, 0, 0,
					_gState.get_team1List(), rivalLstIds) {

				public void onClick(DialogInterface dialog, int which) {
					ListView list = ((AlertDialog) dialog).getListView();
					_gState.get_team1List().get(which).mChecked = list
							.isItemChecked(which);
				}

				@Override
				public boolean onOkClicked(String input) {
					adapter.setData(_gState.get_team1List());
					return true;
				}
			};
		} else {
			for (PLineupItems lI : _gState.get_team1List()) {
				if (lI.mChecked)
					rivalLstIds.add(lI.mId);
			}
			dlg = new MultiSelectListDialog(this, 0, 0,
					_gState.get_team2List(), rivalLstIds) {

				public void onClick(DialogInterface dialog, int which) {
					ListView list = ((AlertDialog) dialog).getListView();
					_gState.get_team2List().get(which).mChecked = list
							.isItemChecked(which);
				}

				@Override
				public boolean onOkClicked(String input) {
					adapter2.setData(_gState.get_team2List());
					return true;
				}
			};

		}
		dlg.create();
		dlg.show();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		restoreState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		restoreState();
	}

	private void restoreState() {
		byte[] state;

		getStatesDbAdapter().open();
		Cursor cP = getStatesDbAdapter().fetchState();
		// startManagingCursor(cP);
		if (cP.getCount() > 0) {
			state = (cP.getBlob(cP
					.getColumnIndexOrThrow(StateDbAdapter.KEY_GAME_STATE)));
			if (state != null && !state.equals("")) {
				ObjectInputStream objectIn;
				Object obj;
				try {
					objectIn = new ObjectInputStream(new ByteArrayInputStream(
							state));
					obj = objectIn.readObject();
					_gState = (GameState) obj;
					if (btnStart != null)
						btnStart.setText((_gState != null
								&& _gState.isStarted() && _gState.is_running()) ? "Stop"
								: "Start");
					_timer.setStartTime(_gState.get_startTime());
					_timer.setStopTime(_gState.get_stopTime());
					_timer.setRunning(_gState.is_running());
					if (_gState.isStarted() && _gState.is_running()) {
						mHandler.sendEmptyMessageDelayed(
								StopWatchHandler.MSG_UPDATE_TIMER,
								StopWatchHandler.REFRESH_RATE);
					} else if (!_gState.isStarted() && !_gState.is_running()) {
						resetTimer();
					} else {
						updateTimer();
					}
				} catch (StreamCorruptedException e) {
					Logger.e("game activity restore state failed due to corrupted stream", e);
				} catch (IOException e) {
					Logger.e("game activity restore state failed due to io issue", e);
				} catch (ClassNotFoundException e) {
					Logger.e("game activity restore state failed due to class not found", e);
				}
			}
		}

		if (_gState == null)
			initState();
		ListView lstView = (ListView) findViewById(R.id.listView1);
		adapter = new CheckedListAdapter(this, _gState.get_team1List());
		lstView.setAdapter(adapter);

		ListView lstView2 = (ListView) findViewById(R.id.listView2);
		adapter2 = new CheckedListAdapter(this, _gState.get_team2List());
		lstView2.setAdapter(adapter2);

		getStatesDbAdapter().close();
	}

	private void initState() {
		_gState = new GameState();

		if (mDbHelper == null)
			mDbHelper = new PlayersDbAdapter(this);
		mDbHelper.open();
		ArrayList<DAOPlayer> pArr = mDbHelper.fetchAllPlayersAsArray();
		mDbHelper.close();

		int s = pArr.size();
		for (int i = 0; i < s; i++) {
			_gState.get_team1List().add(
					new PLineupItems(pArr.get(i).getFname() + " "
							+ pArr.get(i).getLname(), pArr.get(i).getId(),
							false));
			_gState.get_team2List().add(
					new PLineupItems(pArr.get(i).getFname() + " "
							+ pArr.get(i).getLname(), pArr.get(i).getId(),
							false));
			_gState.get_pList().put(pArr.get(i).getId(), pArr.get(i));
		}
	}

	// http://www.easywayserver.com/blog/how-to-serializable-object-in-java-2/
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		saveState();
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	private void saveState() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutput out = new ObjectOutputStream(bos);
			// save timer state
			_gState.set_running(_timer.isRunning());
			_gState.set_startTime(_timer.getStartTime());
			_gState.set_stopTime(_timer.getStopTime());

			out.writeObject(_gState);
			out.flush();
			out.close();
			getStatesDbAdapter().open();
			getStatesDbAdapter().insertOrUpdateState(bos);
			getStatesDbAdapter().close();
		} catch (IOException e) {
			Logger.e("game activity save state failed due to io", e);
		}

	}

	public void setBackwards(View v) {
		_gState.setBackwards(!_gState.isBackwards());
	}

	static class StopWatchHandler extends Handler {
		public static final int MSG_START_TIMER = 0;
		public static final int MSG_STOP_TIMER = 1;
		public static final int MSG_UPDATE_TIMER = 2;
		public static final int MSG_RESET_TIMER = 3;
		public static final int REFRESH_RATE = 1000;

		private final WeakReference<GameActivity> mGAct;

		StopWatchHandler(GameActivity act) {
			mGAct = new WeakReference<GameActivity>(act);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			GameActivity act = mGAct.get();
			switch (msg.what) {
			case MSG_START_TIMER:
				if (act != null)
					act.startTimer();
				sendEmptyMessage(MSG_UPDATE_TIMER);
				break;
			case MSG_UPDATE_TIMER:
				if (act != null)
					act.updateTimer();
				sendEmptyMessageDelayed(MSG_UPDATE_TIMER, REFRESH_RATE);
				break;
			case MSG_STOP_TIMER:
				removeMessages(MSG_UPDATE_TIMER);
				if (act != null)
					act.stopTimer();
				break;
			case MSG_RESET_TIMER:
				removeMessages(MSG_UPDATE_TIMER);
				if (act != null)
					act.resetTimer();
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onSuccess(String result) {
		Toast toast = Toast.makeText(this.getApplicationContext(),
				"Game updated successfully", Toast.LENGTH_SHORT);
		toast.show();
		resetGame();
	}

	@Override
	public void onFailure(int responseCode, String result) {
		showDialog(
				0,
				DlgUtils.prepareDlgBundle("Failed updating game info: "
						+ result));

	}

	@Override
	public void onProgress() {
		// TODO Auto-generated method stub

	}

	@Override
	public Context getAppContext() {
		return getApplicationContext();
	}
}