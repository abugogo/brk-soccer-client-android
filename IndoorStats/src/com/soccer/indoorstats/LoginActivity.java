package com.soccer.indoorstats;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.db.remote.RemoteDBAdapter;
import com.soccer.entities.IDAOPlayer;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.preferences.Prefs;
import com.soccer.preferences.SoccerPrefsActivity;

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
		menu.add(Menu.NONE, 0, 0, "Preferences");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			startActivity(new Intent(this, SoccerPrefsActivity.class));
			return true;
		}
		return false;
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
