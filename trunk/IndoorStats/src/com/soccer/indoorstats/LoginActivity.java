package com.soccer.indoorstats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.db.remote.RemoteDBAdapter;
import com.soccer.entities.IDAOPlayer;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.preferences.Prefs;

public class LoginActivity extends Activity {

	private PlayersDbAdapter mDbHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mDbHelper = new PlayersDbAdapter(this);
		mDbHelper.open();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	public void loginClick(View view) {
		Prefs sharedPrefs = new Prefs(this);
		EditText et = (EditText) findViewById(R.id.editIdNumber);
		String id = et.getText().toString();
		boolean loggedin = (sharedPrefs.getIntPreference("LoggedIn", -1) == 1);

		if(!loggedin) {
			String sUrl = sharedPrefs.getPreference("server_port", "NULL");
			if (sUrl.equals("NULL")) {
				sUrl = "http://23.23.186.205:8080/";
			}
	
			try {
				LoadPlayers();
	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showAlertMessage(e.getMessage());
			}
		}

		if (id != "") {
			
			Intent appIntent = new Intent(this, actOpen.class);
			appIntent.putExtra("player_id", id);
			sharedPrefs.setPreference("LoggedIn", 1);
			startActivity(appIntent);
		} else {
			showAlertMessage("Failed login");
		}
	}

	private DAOPlayer LoadPlayerFromDB(String id) {
		DAOPlayer p = new DAOPlayer();
		Cursor cP = mDbHelper.fetchPlayer(Long.parseLong(id));
		startManagingCursor(cP);
		p.setFname(cP.getString(cP
				.getColumnIndexOrThrow(PlayersDbAdapter.KEY_FNAME)));
		p.setLname(cP.getString(cP
				.getColumnIndexOrThrow(PlayersDbAdapter.KEY_LNAME)));
		p.setEmail(cP.getString(cP
				.getColumnIndexOrThrow(PlayersDbAdapter.KEY_EMAIL)));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		java.sql.Date d = new java.sql.Date(0);
		try {
			d = new java.sql.Date(formatter.parse(cP.getString(cP
					.getColumnIndexOrThrow(PlayersDbAdapter.KEY_BDAY))).getTime());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(d!= null)
			p.setBday(d);
		p.setId(cP.getString(cP
				.getColumnIndexOrThrow(PlayersDbAdapter.KEY_ID)));
		p.setTel1(cP.getString(cP
				.getColumnIndexOrThrow(PlayersDbAdapter.KEY_TEL1)));
		
		return p;
	}

	private void LoadPlayers() {
		
		RemoteDBAdapter rdb = new RemoteDBAdapter();
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String sUrl = sharedPrefs.getString("server_port", "NULL");
		if (sUrl.equals("NULL")) {
			sUrl = "http://23.23.186.205:8080/";
		}
		try {
			ArrayList<IDAOPlayer> pArr = rdb.getPlayers(sUrl);
			if (pArr != null) {
				mDbHelper.deletePlayers();
				Iterator<IDAOPlayer> itr = pArr.iterator();
				while (itr.hasNext()) {
					DAOPlayer p = (DAOPlayer) itr.next();
					mDbHelper.createPlayer(p);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showPlayerInfoOnScreen(IDAOPlayer p) {
		StringBuilder builder = new StringBuilder();

		builder.append("\n" + p.getFname());
		builder.append("\n" + p.getLname());

		showAlertMessage(builder.toString());
	}

	private void showAlertMessage(String msg) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage(msg);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("State", 1);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mDbHelper.close();
	}
}
