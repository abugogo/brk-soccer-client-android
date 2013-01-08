package com.soccer.indoorstats.activity.impl;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageView;
import android.widget.TextView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;
import com.soccer.db.local.DB_CONSTS;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.imageListUtils.ImageLoader;
import com.soccer.indoorstats.R;
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

	public void onCreate(Bundle savedInstanceState) {
		Logger.i("PlayerActivity onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_layout);
		mPrefs = new Prefs(this);
		

		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		String title = mPrefs.getPreference("account_name", getString(R.string.player));
		actionBar.setTitle(title);

		final Action EditAction = new IntentAction(this, new Intent(this,
				PlayerUpdateActivity.class), R.drawable.edit);
		actionBar.addAction(EditAction);

		final Action HomeAction = new IntentAction(this, new Intent(this,
				HomeActivity.class), R.drawable.home_icon);
		actionBar.addAction(HomeAction);

		mPID = (String) mPrefs.getPreference(DB_CONSTS.KEY_ID, mPID);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		imageLoader = new ImageLoader(this.getApplicationContext());
	}

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
		mPrefs = new Prefs(this);
		mPID = mPrefs.getPreference(DB_CONSTS.KEY_ID, mPID);
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
