package com.soccer.indoorstats.activity.impl;

import java.util.LinkedList;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.devsmart.android.ui.HorizontalListView;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.markupartist.android.widget.ActionBar.Action;
import com.soccer.db.local.DB_CONSTS;
import com.soccer.entities.IDAOLEvent.EventType;
import com.soccer.entities.IDAOMedal.MedalEnum;
import com.soccer.entities.impl.DAOLEvent;
import com.soccer.entities.impl.DAOMedal;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.imageListUtils.ImageLoader;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.activity.impl.stats.StatsTabActivity;
import com.soccer.indoorstats.adapters.MedalListAdapter;
import com.soccer.indoorstats.adapters.RecordListAdapter;
import com.soccer.indoorstats.services.PlayerService;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.preferences.Prefs;

public class PlayerActivity extends Activity {

	private DAOPlayer mPlayer = null;
	private String mPID = null;
	private ImageLoader imageLoader;
	private Prefs mPrefs;
	private PlayerService mBoundService;
	private boolean mIsBound;
	private ActionBar actionBar;
	private MedalListAdapter medalList;

	public void onCreate(Bundle savedInstanceState) {
		Logger.i("PlayerActivity onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_layout);
		mPrefs = new Prefs(this);
		Bundle b = getIntent().getExtras();
		if (null != b)
			mPID = b.getString("pid");
		if (mPID == null || "".equals(mPID))
			mPID = mPrefs.getPreference(DB_CONSTS.KEY_ID, mPID);

		actionBar = (ActionBar) findViewById(R.id.actionbar);
		String title = mPrefs.getPreference("account_name",
				getString(R.string.player));
		actionBar.setTitle(title);

		// allow editing of yourself only
		if (mPrefs.getPreference(DB_CONSTS.KEY_ID, "").equals(mPID)) {
			final Action EditAction = new EditPlayerAction();
			actionBar.addAction(EditAction);
		}

		// final Action HomeAction = new IntentAction(this, new Intent(this,
		// HomeActivity.class), R.drawable.home_icon);
		// actionBar.addAction(HomeAction);

		setListsAdapters();

		// buttom navigation bar
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
		imageLoader = new ImageLoader(this.getApplicationContext());
	}

	private CompoundButton.OnCheckedChangeListener btnNavBarOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				Toast.makeText(PlayerActivity.this, buttonView.getText(),
						Toast.LENGTH_SHORT).show();

				Intent appIntent = null;

				switch (buttonView.getId()) {
				case R.id.btnGroup:
					appIntent = new Intent(PlayerActivity.this,
							GroupActivity.class);
					break;
				case R.id.btnSeason:
					Toast.makeText(PlayerActivity.this, "not implemented",
							Toast.LENGTH_SHORT).show();
					break;
				case R.id.btnStats:
					appIntent = new Intent(PlayerActivity.this,
							StatsTabActivity.class);
					break;

				case R.id.btnGame:
					appIntent = new Intent(PlayerActivity.this,
							StatsTabActivity.class);
					break;
				}
				if (appIntent != null)
					startActivity(appIntent);

			}
		}
	};

	private void populateFields() {
		Logger.i("PlayerActivity populateFields");
		if (mPID != null && !mPID.equals("")) {
			if (mPlayer != null && mPlayer.getId() != null
					&& mPlayer.getId() != "") {
				((TextView) findViewById(R.id.pfname)).setText(mPlayer
						.getFname());
				((TextView) findViewById(R.id.plname)).setText(mPlayer
						.getLname());
				((TextView) findViewById(R.id.email)).setText(mPlayer
						.getEmail());
				((TextView) findViewById(R.id.ptel1))
						.setText(mPlayer.getTel1());
				((TextView) findViewById(R.id.desc)).setText(mPlayer
						.getDescription());

				String bDay = mPlayer.getBdayAsString(null);
				if (bDay != null)
					((TextView) findViewById(R.id.bday)).setText(bDay);
				ImageView thumb_image = (ImageView) findViewById(R.id.pimage); // thumb
																				// image
				imageLoader.DisplayImage(mPlayer.getP_img(), thumb_image);
			} else {
				Prefs sharedPrefs = new Prefs(this);
				sharedPrefs.setPreference("LoggedIn", -1);
				showDialog(0,
						DlgUtils.prepareDlgBundle("Failed to load player"));
			}
		}
	}

	private void setListsAdapters() {

		// Medal list
		HorizontalListView medalLlstView = (HorizontalListView) findViewById(R.id.medallist);
		LinkedList<DAOMedal> medalData = new LinkedList<DAOMedal>();
		DAOMedal medal = new DAOMedal();
		medal.setMedalType(MedalEnum.AGGR_GOALS);
		medalData.add(medal);
		medal = new DAOMedal();
		medal.setMedalType(MedalEnum.GOALS_PER_GAME);
		medalData.add(medal);
		medal = new DAOMedal();
		medal.setMedalType(MedalEnum.WIN_STREEK);
		medalData.add(medal);

		MedalListAdapter medalLstAdapter = new MedalListAdapter(this, medalData);

		medalLlstView.setAdapter(medalLstAdapter);

		// Record list
		HorizontalListView recordlstView = (HorizontalListView) findViewById(R.id.recordlist);
		LinkedList<DAOLEvent> recordData = new LinkedList<DAOLEvent>();
		DAOLEvent evt = new DAOLEvent();
		evt.setType(EventType.Goal);
		recordData.add(evt);
		evt = new DAOLEvent();
		evt.setType(EventType.Cook);
		recordData.add(evt);
		evt = new DAOLEvent();
		evt.setType(EventType.R_Card);
		recordData.add(evt);
		evt = new DAOLEvent();
		evt.setType(EventType.Y_Card);
		recordData.add(evt);

		RecordListAdapter recordlstAdapter = new RecordListAdapter(this,
				recordData);

		recordlstView.setAdapter(recordlstAdapter);

		// test
		/*HorizontalListView listview = (HorizontalListView) findViewById(R.id.horilistview);
		TestItemsListAdapter adapter = new TestItemsListAdapter(this,
				new LinkedList<String>(Arrays.asList("guy", "yossi", "klum",
						"yossi2", "klum2", "yossi3", "klum3")));
		listview.setAdapter(adapter);*/

	}

	private class EditPlayerAction extends AbstractAction {
		public EditPlayerAction() {
			super(R.drawable.edit);
		}

		@Override
		public void performAction(View view) {
			Intent pupdateIntent = new Intent(PlayerActivity.this,
					PlayerUpdateActivity.class);
			pupdateIntent.putExtra("pid", mPID);
			startActivityForResult(pupdateIntent, 1);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		return DlgUtils.createAlertMessage(this, args);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Logger.i("PlayerActivity onPause");
		doUnbindService();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Logger.i("PlayerActivity onResume");
		doBindService();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mBoundService = (PlayerService) ((PlayerService.LocalBinder) service)
					.getService();
			mPlayer = mBoundService.getPlayer(mPID);
			populateFields();
		}

		public void onServiceDisconnected(ComponentName className) {
			mBoundService = null;
		}
	};

	private void doBindService() {
		bindService(new Intent(PlayerActivity.this, PlayerService.class),
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
