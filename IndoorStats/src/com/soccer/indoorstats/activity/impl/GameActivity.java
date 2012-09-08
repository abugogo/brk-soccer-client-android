package com.soccer.indoorstats.activity.impl;

/*This code has been created for a demo by Shawn for ShawnBe.com 
 * please do not redistribute this without my permission, you
 * can email me at shawn@shawnbe.com. If these tutorials has helped
 * you please consider donating to me via paypal on my website 
 * ShawnBe.com
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;
import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.dialog.CheckedListAdapter;
import com.soccer.dialog.MultiSelectListDialog;
import com.soccer.dialog.lstItem;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.utils.StopWatch;

public class GameActivity extends Activity implements OnClickListener {

	private static final int MSG_START_TIMER = 0;
	private static final int MSG_STOP_TIMER = 1;
	private static final int MSG_UPDATE_TIMER = 2;
	private static final int MSG_RESET_TIMER = 3;
	
	private static StopWatch timer = new StopWatch();
	private static final int REFRESH_RATE = 1000;

	//private MenuExtender slidingMenu;
	final ArrayList<lstItem> team1List = new ArrayList<lstItem>();
	final ArrayList<lstItem> team2List = new ArrayList<lstItem>();
	private static TextView tvTextView;
	private static Button btnStart;
	Button btnReset;
	CheckedListAdapter adapter;
	CheckedListAdapter adapter2;
	HashMap<String, DAOPlayer> mPList = new HashMap<String, DAOPlayer>();
	private PlayersDbAdapter mDbHelper = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_main);
		/*if (slidingMenu == null) {
			slidingMenu = new MenuExtender(this, "");
			slidingMenu.initSlideMenu();
		}*/

		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        //actionBar.setHomeAction(new IntentAction(this, createIntent(this), R.drawable.ic_title_home_demo));
        actionBar.setTitle(R.string.game);
        actionBar.setHomeLogo(R.drawable.soccerstats);

        final Action PlayerAction = new IntentAction(this, new Intent(this, PlayerActivity.class), R.drawable.player_white);
        actionBar.addAction(PlayerAction);
        final Action GroupAction = new IntentAction(this, new Intent(this, GroupActivity.class), R.drawable.players_white);
        actionBar.addAction(GroupAction);

		tvTextView = (TextView) findViewById(R.id.textViewTimer);

		btnStart = (Button) findViewById(R.id.buttonStart);
		btnReset = (Button) findViewById(R.id.buttonReset);
		btnStart.setOnClickListener(this);
		btnReset.setOnClickListener(this);

		mDbHelper = new PlayersDbAdapter(this);
		mDbHelper.open();
		ArrayList<DAOPlayer> pArr = mDbHelper.fetchAllPlayersAsArray();
		mDbHelper.close();

		int s = pArr.size();
		for (int i = 0; i < s; i++) {
			team1List.add(new lstItem(pArr.get(i).getFname() + " "
					+ pArr.get(i).getLname(), pArr.get(i).getId(), false));
			team2List.add(new lstItem(pArr.get(i).getFname() + " "
					+ pArr.get(i).getLname(), pArr.get(i).getId(), false));
			mPList.put(pArr.get(i).getId(), pArr.get(i));
		}
		ListView lstView = (ListView) findViewById(R.id.listView1);
		adapter = new CheckedListAdapter(this, team1List);
		lstView.setAdapter(adapter);

		ListView lstView2 = (ListView) findViewById(R.id.listView2);
		adapter2 = new CheckedListAdapter(this, team2List);
		lstView2.setAdapter(adapter2);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void onClick(View v) {
		if (btnStart == v) {
			if (btnStart.getText().equals("Start")) {
				mHandler.sendEmptyMessage(MSG_START_TIMER);
				btnStart.setText("Stop");
			} else {
				mHandler.sendEmptyMessage(MSG_STOP_TIMER);
				btnStart.setText("Start");
			}
		} else if (btnReset == v) {
			mHandler.sendEmptyMessage(MSG_RESET_TIMER);
		}

	}

	public void onAddItem(View v) {
		MultiSelectListDialog dlg;

		if (v == (Button) findViewById(R.id.button2)) {
			dlg = new MultiSelectListDialog(this, 0, 0, team1List) {

				public void onClick(DialogInterface dialog, int which) {
					ListView list = ((AlertDialog) dialog).getListView();
					team1List.get(which).mChecked = list.isItemChecked(which);
				}

				@Override
				public boolean onOkClicked(String input) {
					adapter.setData(team1List);
					return true;
				}
			};
		} else {
			dlg = new MultiSelectListDialog(this, 0, 0, team2List) {

				public void onClick(DialogInterface dialog, int which) {
					ListView list = ((AlertDialog) dialog).getListView();
					team2List.get(which).mChecked = list.isItemChecked(which);
				}

				@Override
				public boolean onOkClicked(String input) {
					adapter2.setData(team2List);
					return true;
				}
			};

		}
		dlg.create();
		dlg.show();
	}

	public void OnPlayerEvent(final View view) {
		
		final CharSequence[] items = { "Goal", "Cook", "Yellow card",
				"Red card" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Event");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				Toast.makeText(getApplicationContext(), mPList.get(view.getTag()).getFname() + " received " + items[item],
						Toast.LENGTH_SHORT).show();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/*
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return slidingMenu.handleTouchEvent(event);
	}*/

	final private static int FULL_TIME = 7*60;
	private static boolean backwards = false;
	private static boolean started = false;
	
	public void setBackwards(View v) {
		backwards = !backwards;
	}
	private static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_START_TIMER:
				timer.start(!started);
				started = true;
				mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
				break;
			case MSG_UPDATE_TIMER:
				int elapsed = (int) timer.getElapsedTimeSecs();
				if (backwards)
					elapsed = FULL_TIME - elapsed;
				int mins = (int) (elapsed / 60);
				int secs = (int) (elapsed % 60);
				tvTextView.setText(((mins < 10)?"0":"") + mins + ":" + ((secs < 10)?"0":"") + secs);
				mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER, REFRESH_RATE);
				break;
			case MSG_STOP_TIMER:
				mHandler.removeMessages(MSG_UPDATE_TIMER);
				timer.stop();
				break;
			case MSG_RESET_TIMER:
				mHandler.removeMessages(MSG_UPDATE_TIMER);
				timer.stop();
				started = false;
				btnStart.setText("Start");
				tvTextView.setText("00:00");
				break;

			default:
				break;
			}
		}
	};
}