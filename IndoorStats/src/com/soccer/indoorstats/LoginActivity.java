package com.soccer.indoorstats;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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
	private static final String SERVER_DEFAULT = "http://ellgad.com/";
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
		int loggedInId = sharedPrefs.getIntPreference("LoggedIn", -1);

		if (loggedInId == -1 || loggedInId != Integer.parseInt(id)) {
			String sUrl = sharedPrefs.getPreference("server_port", "NULL");
			if (sUrl.equals("NULL")) {
				sUrl = LoginActivity.SERVER_DEFAULT;
			}

			try {
				LoadPlayers(sUrl);

			} catch (Exception e) {
				e.printStackTrace();
				showAlertMessage(e.getMessage());
			}
		}

		if (id != "") {
			sharedPrefs.setPreference("LoggedIn", Integer.parseInt(id));
			Intent appIntent = new Intent(this, actOpen.class);
			appIntent.putExtra("player_id", id);
			startActivity(appIntent);
		} else {
			sharedPrefs.setPreference("LoggedIn", -1);
			showAlertMessage("Failed login");
		}
	}

	private void LoadPlayers(String sUrl) {

		RemoteDBAdapter rdb = new RemoteDBAdapter();
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
			e.printStackTrace();
		}
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
		super.onDestroy();
		mDbHelper.close();
	}
}
