package com.soccer.indoorstats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.preferences.Prefs;

public class PlayerActivity extends Activity {

	private PlayersDbAdapter mDbHelper;
	private DAOPlayer mPlayer = null;
	private String mPID = null;

	public void onCreate(Bundle savedInstanceState) {
		Log.i("LifeCycle", "PlayerActivity onCreate");
		super.onCreate(savedInstanceState);
		mDbHelper = new PlayersDbAdapter(this);
		mDbHelper.open();
		mPID = (savedInstanceState == null) ? null
				: (String) savedInstanceState
						.getSerializable(PlayersDbAdapter.KEY_ID);
		setContentView(R.layout.player_layout);

		if (mPID == null) {
			Intent i = getIntent();
			mPID = (String) i.getSerializableExtra("player_id");
		}

	}

	private void populateFields() {
		Log.i("Info", "PlayerActivity populateFields");
		if (mPID != null) {
			LoadPlayerFromDB(mPID);

			if (mPlayer != null && mPlayer.getId() != null
					&& mPlayer.getId() != "") {
				((EditText) findViewById(R.id.editPlayerName)).setText(mPlayer
						.getFname() + " " + mPlayer.getLname());
				((EditText) findViewById(R.id.editEmail)).setText(mPlayer
						.getEmail());
				((EditText) findViewById(R.id.editPhone)).setText(mPlayer
						.getTel1());
				String bDay = mPlayer.getBdayAsString(null);
				if (bDay != null)
					((EditText) findViewById(R.id.editBirth)).setText(bDay);
			}
			else {
				Prefs sharedPrefs = new Prefs(this);
				sharedPrefs.setPreference("LoggedIn", -1);
				Bundle args = new Bundle();
				args.putString("msg", "Failed to load player");
				showDialog(0, args);
				
			}

		}
		
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		return createAlertMessage(args.getString("msg"));
	}

	private Dialog createAlertMessage(String msg) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage(msg);
		return alertDialog;
	}

	private void LoadPlayerFromDB(String id) {
		DAOPlayer p = new DAOPlayer();
		Cursor cP = mDbHelper.fetchPlayer(Long.parseLong(id));
		startManagingCursor(cP);
		if(cP.getCount() > 0) {
			p.setFname(cP.getString(cP
					.getColumnIndexOrThrow(PlayersDbAdapter.KEY_FNAME)));
			p.setLname(cP.getString(cP
					.getColumnIndexOrThrow(PlayersDbAdapter.KEY_LNAME)));
			p.setEmail(cP.getString(cP
					.getColumnIndexOrThrow(PlayersDbAdapter.KEY_EMAIL)));
			Date d = null;
			try {
				String sBDate = cP.getString(cP
						.getColumnIndexOrThrow(PlayersDbAdapter.KEY_BDAY));
				if (sBDate != null && !sBDate.equals(""))
					d = (Date) new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.ENGLISH)
							.parse(sBDate);
	
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (d != null)
				p.setBday(d);
			p.setId(cP.getString(cP.getColumnIndexOrThrow(PlayersDbAdapter.KEY_ID)));
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
		populateFields();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i("LifeCycle", "PlayerActivity onSaveInstanceState");
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(PlayersDbAdapter.KEY_ID, mPID);
	}

	private void saveState() {
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mDbHelper.close();
	}
}
