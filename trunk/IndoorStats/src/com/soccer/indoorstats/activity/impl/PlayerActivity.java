package com.soccer.indoorstats.activity.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;
import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.imageListUtils.ImageLoader;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.preferences.Prefs;

public class PlayerActivity extends Activity {

	private PlayersDbAdapter mDbHelper;
	private DAOPlayer mPlayer = null;
	private String mPID = null;
	private ImageLoader imageLoader;
	private Prefs mPrefs;

	public void onCreate(Bundle savedInstanceState) {
		Logger.i("PlayerActivity onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_layout);

		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle(R.string.player);

		/*final Action StatsAction = new IntentAction(this, new Intent(this,
				StatsTabActivity.class), R.drawable.heart);
		actionBar.addAction(StatsAction);*/
		final Action EditAction = new IntentAction(this, new Intent(this,
				PlayerUpdateActivity.class), R.drawable.edit);
		actionBar.addAction(EditAction);
		/*final Action GroupAction = new IntentAction(this, new Intent(this,
				GroupActivity.class), R.drawable.players_icon);
		actionBar.addAction(GroupAction);
		final Action GameAction = new IntentAction(this, new Intent(this,
				GameActivity.class), R.drawable.game_icon);
		actionBar.addAction(GameAction);*/

		final Action HomeAction = new IntentAction(this, new Intent(this,
				HomeActivity.class), R.drawable.home_icon);
		actionBar.addAction(HomeAction);
		
		mDbHelper = new PlayersDbAdapter(this);
		mDbHelper.open();
		Intent i = getIntent();
		mPID = (String) i.getSerializableExtra("player_id");

		mPrefs = new Prefs(this);
		mPID = (String) mPrefs.getPreference(PlayersDbAdapter.KEY_ID, mPID);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		imageLoader = new ImageLoader(this.getApplicationContext());
	}

	private void populateFields() {
		Logger.i("PlayerActivity populateFields");
		if (mPID != null && !mPID.equals("")) {
			LoadPlayerFromDB(mPID);

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

	private void LoadPlayerFromDB(String id) {
		DAOPlayer p = new DAOPlayer();
		Cursor cP = mDbHelper.fetchPlayer(Long.parseLong(id));
		startManagingCursor(cP);
		if (cP.getCount() > 0) {
			p.setIdNum(BigInteger.valueOf(1));
			p.setFname(cP.getString(cP
					.getColumnIndexOrThrow(PlayersDbAdapter.KEY_FNAME)));
			p.setLname(cP.getString(cP
					.getColumnIndexOrThrow(PlayersDbAdapter.KEY_LNAME)));
			p.setEmail(cP.getString(cP
					.getColumnIndexOrThrow(PlayersDbAdapter.KEY_EMAIL)));
			p.setP_img(cP.getString(cP
					.getColumnIndexOrThrow(PlayersDbAdapter.KEY_IMG)));
			Date d = null;
			try {
				String sBDate = cP.getString(cP
						.getColumnIndexOrThrow(PlayersDbAdapter.KEY_BDAY));
				if (sBDate != null && !sBDate.equals(""))
					d = (Date) new SimpleDateFormat(
							"EEE MMM d HH:mm:ss zzz yyyy", Locale.ENGLISH)
							.parse(sBDate);

			} catch (IllegalArgumentException e) {
				Logger.e("player activity loadplayersfromdb failed due to illegal state", e);
			} catch (ParseException e) {
				Logger.e("player activity loadplayersfromdb failed due to parser exception", e);
			}
			if (d != null)
				p.setBday(d);
			p.setId(cP.getString(cP
					.getColumnIndexOrThrow(PlayersDbAdapter.KEY_ID)));
			p.setTel1(cP.getString(cP
					.getColumnIndexOrThrow(PlayersDbAdapter.KEY_TEL1)));
		}

		mPlayer = p;
	}

	@Override
	protected void onPause() {
		Logger.i("PlayerActivity onPause");
		super.onPause();
		saveState();
	}

	@Override
	protected void onResume() {
		Logger.i("PlayerActivity onResume");
		super.onResume();
		mPID = mPrefs.getPreference(PlayersDbAdapter.KEY_ID, mPID);
		populateFields();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Logger.i("PlayerActivity onSaveInstanceState");
		super.onSaveInstanceState(outState);
		saveState();
	}

	private void saveState() {
		mPrefs.setPreference(PlayersDbAdapter.KEY_ID, mPID);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
	}

	

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 0, 0, "Save");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			updatePlayer();
			return true;
		}
		return false;
	}*/

}
