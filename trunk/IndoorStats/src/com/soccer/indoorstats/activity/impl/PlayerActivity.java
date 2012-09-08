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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;
import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.db.remote.RemoteDBAdapter;
import com.soccer.dialog.PromptDialog;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.imageListUtils.ImageLoader;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.activity.i.IAsyncTaskAct;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.preferences.Prefs;

public class PlayerActivity extends Activity implements IAsyncTaskAct {

	private PlayersDbAdapter mDbHelper;
	private DAOPlayer mPlayer = null;
	private String mPID = null;
	private ImageLoader imageLoader;
	//private MenuExtender slidingMenu;
	private Prefs mPrefs;
	
	public void onCreate(Bundle savedInstanceState) {
		Log.i("LifeCycle", "PlayerActivity onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_layout);
		
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeLogo(R.drawable.soccerstats);
        //actionBar.setHomeAction(new IntentAction(this, createIntent(this), R.drawable.ic_title_home_demo));
        actionBar.setTitle(R.string.player);

        final Action GroupAction = new IntentAction(this, new Intent(this, GroupActivity.class), R.drawable.players_white);
        actionBar.addAction(GroupAction);
        final Action GameAction = new IntentAction(this, new Intent(this, GameActivity.class), R.drawable.videos_white);
        actionBar.addAction(GameAction);
        
        
		mDbHelper = new PlayersDbAdapter(this);
		mDbHelper.open();
		Intent i = getIntent();
		mPID = (String) i.getSerializableExtra("player_id");

		mPrefs = new Prefs(this);
		mPID = (String) mPrefs.getPreference(PlayersDbAdapter.KEY_ID, mPID);

		
		/*if (slidingMenu == null) {
			slidingMenu = new MenuExtender(this, mPID);
			slidingMenu.initSlideMenu();
		}*/
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		imageLoader = new ImageLoader(this.getApplicationContext());

	}

	/*@Override
	public boolean onTouchEvent(MotionEvent event) {
		return slidingMenu.handleTouchEvent(event);
	}*/

	private void populateFields() {
		Log.i("Info", "PlayerActivity populateFields");
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
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
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
		Log.i("LifeCycle", "PlayerActivity onPause");
		super.onPause();
		saveState();
	}

	@Override
	protected void onResume() {
		Log.i("LifeCycle", "PlayerActivity onResume");
		super.onResume();
		mPID = mPrefs.getPreference(PlayersDbAdapter.KEY_ID, mPID);
		populateFields();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i("LifeCycle", "PlayerActivity onSaveInstanceState");
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

	@Override
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
	}

	public void promptUpdate(final View view) {
		int captionStrId = -1;
		switch(view.getId()) {
		case R.id.email:
			captionStrId = R.string.emailTtl;
			break;
		case R.id.ptel1:
			captionStrId = R.string.phoneTtl;
			break;
		case R.id.bday:
			captionStrId = R.string.bdayTtl;
			break;
		}
		
		if(captionStrId != -1) {
			PromptDialog dlg = new PromptDialog(this, captionStrId,
					R.string.newVal, ((TextView) view).getText().toString()) {
				@Override
				public boolean onOkClicked(String input) {
					((TextView) view).setText(input);
					return true;
				}
			};
			dlg.show();
		}
	}

	private void updatePlayer() {
		if (mPlayer != null) {
			String newEmail = ((TextView) findViewById(R.id.email)).getText().toString();
			String newPhone = ((TextView) findViewById(R.id.ptel1)).getText().toString();
			String newBday = ((TextView) findViewById(R.id.bday)).getText().toString();
			if(!mPlayer.getBdayAsString("").equals(newBday)) {
				Toast toast = Toast.makeText(this.getApplicationContext(), "Birthday modification is not supported", Toast.LENGTH_SHORT);
				toast.show();
			}
			boolean dirty = (!mPlayer.getEmail().equals(newEmail) ||
					!mPlayer.getTel1().equals(newPhone));
			if(dirty) {
				// add support to birth date
				mPlayer.setEmail(newEmail);
				mPlayer.setTel1(newPhone);
				String sUrl = mPrefs.getPreference("server_port", "NULL");
				try {
					RemoteDBAdapter.updatePlayer(this, sUrl,
							"Updating player info", mPlayer);
	
				} catch (Exception e) {
					showDialog(
							0,
							DlgUtils.prepareDlgBundle("Failed to update player:"
									+ e.getMessage()));
					e.printStackTrace();
				}
			}
			else {
				Toast toast = Toast.makeText(this.getApplicationContext(), "No changes detected", Toast.LENGTH_SHORT);
				toast.show();
			}
			
		}

	}

	public void onSuccess(String result) {
		mDbHelper.updatePlayer(mPlayer);
		Toast toast = Toast.makeText(this.getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT);
		toast.show();
	}

	public void onFailure(int responseCode, String result) {
		showDialog(0, DlgUtils.prepareDlgBundle("Failed to update player"));

	}

	public void onProgress() {

	}

}
