package com.soccer.indoorstats.activity.impl;

import java.io.ObjectInputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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
import com.soccer.entities.impl.DAOGame;
import com.soccer.entities.impl.PrintableLineup;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.activity.states.GameState;
import com.soccer.indoorstats.adapters.LineupListAdapter;
import com.soccer.indoorstats.services.GameService;
import com.soccer.indoorstats.services.handlers.RequestHandler;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.indoorstats.utils.StopWatch;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.preferences.Prefs;

public class GameActivity extends Activity implements OnClickListener {

	private StopWatch _timer = new StopWatch();
	private GameState _gState = new GameState();
	private StopWatchHandler mHandler = new StopWatchHandler(this);
	private TextView tvTextView;
	private Button btnStart;
	private Button btnReset;
	private LineupListAdapter badapter;
	private LineupListAdapter wadapter;
	private LineupObserver listsObserver = null;;
	private LinkedList<PrintableLineup> lineupData = null;
	private Prefs sharedPrefs;
	private GameService mBoundGameService;
	private boolean mIsBound;
	private ActionBar actionBar;

	@Override
	public Object onRetainNonConfigurationInstance() {
		return _gState;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_main);
		sharedPrefs = new Prefs(this);

		actionBar = (ActionBar) findViewById(R.id.actionbar);
		String title = sharedPrefs.getPreference("account_name",
				getString(R.string.game));
		actionBar.setTitle(title);

		final Action teamSelectionAction = new TeamSelectionAction();
		actionBar.addAction(teamSelectionAction);

		final Action SaveAction = new SaveGameAction();
		actionBar.addAction(SaveAction);

		final Action ResetAction = new ResetGameAction();
		actionBar.addAction(ResetAction);

		tvTextView = (TextView) findViewById(R.id.textViewTimer);

		btnStart = (Button) findViewById(R.id.buttonStart);
		btnReset = (Button) findViewById(R.id.buttonReset);
		btnStart.setOnClickListener(this);
		btnReset.setOnClickListener(this);

		if ((GameState) getLastNonConfigurationInstance() != null)
			_gState = (GameState) getLastNonConfigurationInstance();
	}

	public void createGame() {
		List<PrintableLineup> lpList = new ArrayList<PrintableLineup>();
		LinkedList<PrintableLineup> lpbList = badapter.getData();
		LinkedList<PrintableLineup> lpwList = wadapter.getData();
		int bG = 0, wG = 0;
		if (lpbList != null) {
			for (PrintableLineup pt1 : lpbList) {
				bG += pt1.getGoal();
				wG += pt1.getOGoal();
				lpList.add(pt1);
			}
		}
		if (lpwList != null) {
			for (PrintableLineup pt2 : lpwList) {
				wG += pt2.getGoal();
				bG += pt2.getOGoal();
				lpList.add(pt2);
			}
		}

		char winner = 'd';
		if (Math.abs(wG - bG) > 0) {
			if (wG - bG > 0)
				winner = 'w';
			else
				winner = 'b';

			for (PrintableLineup ap : lpList) {
				ap.setPoints((ap.getColor().equals(winner)) ? (short) 3 : 0);
			}
		} else {
			for (PrintableLineup ap : lpList) {
				ap.setPoints((short) 1);
			}
		}

		DAOGame daoGame = new DAOGame();
		daoGame.setLineup(lpList);
		daoGame.setBgoals(bG);
		daoGame.setGameDate(new Date());
		daoGame.setWgoals(wG);
		daoGame.setWinner(winner);

		try {
			if (mIsBound) {
				actionBar.setProgressBarVisibility(View.VISIBLE);
				mBoundGameService.updateGame(daoGame,
						new RequestHandler<JSONObject>() {

							@Override
							public void onSuccess(JSONObject t) {
								Logger.i("Game created success");
								actionBar
										.setProgressBarVisibility(View.INVISIBLE);
								onCreateGameSuccess();
							}

							@Override
							public void onFailure(String reason, int errorCode) {
								Logger.i("Game creation failure");
								actionBar
										.setProgressBarVisibility(View.INVISIBLE);
								onCreateGameFailure(errorCode, reason);
							}
						});
			}
		} catch (Exception e) {
			Logger.e("create game failed", e);
			showDialog(0, DlgUtils.prepareDlgBundle(e.getMessage()));
		}

	}

	private class TeamSelectionAction extends AbstractAction {
		public TeamSelectionAction() {
			super(R.drawable.elevator);
		}

		@Override
		public void performAction(View view) {
			teamSelection();
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
			super(R.drawable.rip);
		}

		@Override
		public void performAction(View view) {
			resetGame();
		}
	}

	public void resetGame() {
		resetTimer();
		_gState = new GameState();
		badapter.setData(null);
		wadapter.setData(null);
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
				tvTextView.setTextColor(Color.WHITE);
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
			tvTextView.setTextColor(Color.WHITE);
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

	public void teamSelection() {
		Intent teamSelection = new Intent(this, TeamSelectionActivity.class);
		teamSelection.putExtra("llist", _gState.get_lpList());
		startActivityForResult(teamSelection, 1);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			if (data != null && data.getExtras() != null) {
				Object objSentData = data.getExtras().get("llist");
				if (null != objSentData) {
					ArrayList<PrintableLineup> lst = (ArrayList<PrintableLineup>) objSentData;
					if (lst != null) {
						lineupData = new LinkedList<PrintableLineup>();
						lineupData.addAll(lst);
					}
				}
			}
		} else
			lineupData = null;
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		super.onResume();
		doBindServices();
	}

	private void restoreState() {
		ObjectInputStream objIn = mBoundGameService.getCurGameState();
		if (objIn != null)
			_gState.deserialize(objIn);
		if (_gState != null) {
			if (btnStart != null)
				btnStart.setText((_gState.isStarted() && _gState.is_running()) ? "Stop"
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
		} else
			initState();
		setListsAdapters();
	}

	private void setListsAdapters() {

		ListView blstView = (ListView) findViewById(R.id.listView1);

		if (badapter == null)
			badapter = new LineupListAdapter(this,
					new LinkedList<PrintableLineup>());
		// badapter.registerDataSetObserver(new LineupObserver());

		ListView wlstView = (ListView) findViewById(R.id.listView2);

		if (wadapter == null)
			wadapter = new LineupListAdapter(this,
					new LinkedList<PrintableLineup>());
		// wadapter.registerDataSetObserver(new LineupObserver());

		if (listsObserver == null) {
			listsObserver = new LineupObserver();
			badapter.registerDataSetObserver(listsObserver);
			wadapter.registerDataSetObserver(listsObserver);
		}

		if (_gState != null) {
			if (lineupData != null) {
				_gState.set_lpList(lineupData);
				lineupData = null;
			}
			badapter.setData(null);
			wadapter.setData(null);
			for (PrintableLineup lp : _gState.get_lpList()) {
				if (lp.getColor() != null) {
					if (lp.getColor().equals('b'))
						badapter.addItem(lp);
					else
						wadapter.addItem(lp);
				}
				badapter.notifyDataSetChanged();
				wadapter.notifyDataSetChanged();
			}
		}

		blstView.setAdapter(badapter);
		wlstView.setAdapter(wadapter);
	}

	private class LineupObserver extends DataSetObserver {
		@Override
		public void onChanged() {
			LinkedList<PrintableLineup> lpbList = badapter.getData();
			LinkedList<PrintableLineup> lpwList = wadapter.getData();
			int bG = 0, wG = 0;
			if (lpbList != null) {
				for (PrintableLineup pt1 : lpbList) {
					bG += pt1.getGoal();
					wG += pt1.getOGoal();
				}
			}
			if (lpwList != null) {
				for (PrintableLineup pt2 : lpwList) {
					wG += pt2.getGoal();
					bG += pt2.getOGoal();
				}
			}

			TextView score = (TextView) findViewById(R.id.txtGameScore);
			score.setText(bG + ":" + wG);
		}
	}

	private void initState() {
		_gState = new GameState();
	}

	// http://www.easywayserver.com/blog/how-to-serializable-object-in-java-2/
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// saveState();
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
		doUnbindServices();
	}

	private void saveState() {
		if (mIsBound && mBoundGameService != null) {
			_gState.set_running(_timer.isRunning());
			_gState.set_startTime(_timer.getStartTime());
			_gState.set_stopTime(_timer.getStopTime());
			mBoundGameService.saveGameState(_gState.serialize());
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

	public void onCreateGameSuccess() {
		Toast toast = Toast.makeText(this.getApplicationContext(),
				"Game updated successfully", Toast.LENGTH_SHORT);
		toast.show();
		resetGame();
	}

	public void onCreateGameFailure(int responseCode, String result) {
		DlgUtils.showAlertMessage(this, "Game Update Failed", result);
	}

	private ServiceConnection mGameConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mBoundGameService = (GameService) ((GameService.LocalBinder) service)
					.getService();
			restoreState();
		}

		public void onServiceDisconnected(ComponentName className) {
			mBoundGameService = null;
		}
	};

	private void doBindServices() {
		if (!mIsBound) {
			bindService(new Intent(GameActivity.this, GameService.class),
					mGameConnection, Context.BIND_AUTO_CREATE);
			mIsBound = true;
		}
	}

	private void doUnbindServices() {
		if (mIsBound) {
			unbindService(mGameConnection);
			mIsBound = false;
		}
	}

}