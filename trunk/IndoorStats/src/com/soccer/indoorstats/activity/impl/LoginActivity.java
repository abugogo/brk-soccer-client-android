package com.soccer.indoorstats.activity.impl;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.db.remote.RemoteDBAdapter;
import com.soccer.entities.EntityManager;
import com.soccer.entities.IDAOPlayer;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.activity.i.IAsyncTaskAct;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.lib.SoccerException;
import com.soccer.preferences.Prefs;
import com.soccer.preferences.SoccerPrefsActivity;

public class LoginActivity extends Activity implements IAsyncTaskAct {
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
				RemoteDBAdapter.getPlayers(this, sUrl, "Downloading data");
			} catch (Exception e) {
				e.printStackTrace();
				showDialog(0, DlgUtils.prepareDlgBundle(e.getMessage()));
			}
		} else {
			loadApp(id, sharedPrefs);
		}
	}

	private void LoadPlayers(ArrayList<IDAOPlayer> pArr) {
		try {
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

	private void loadApp(String id, Prefs sharedPrefs) {
		if (id != "") {
			sharedPrefs.setPreference("LoggedIn", Integer.parseInt(id));
			//Intent appIntent = new Intent(this, actOpen.class);
			Intent appIntent = new Intent(this, PlayerActivity.class);
			appIntent.putExtra("player_id", id);
			startActivity(appIntent);
		} else {
			sharedPrefs.setPreference("LoggedIn", -1);
			showDialog(0, DlgUtils.prepareDlgBundle("Failed login"));
		}
	}

	public void onSuccess(String result) {
		try {
			ArrayList<IDAOPlayer> pArr = EntityManager.readPlayers(result);
			LoadPlayers(pArr);
		} catch (SoccerException e) {
			e.printStackTrace();
		}

		Prefs sharedPrefs = new Prefs(this);
		EditText et = (EditText) findViewById(R.id.editIdNumber);
		String id = et.getText().toString();
		loadApp(id, sharedPrefs);
	}

	public void onFailure(int responseCode, String result) {
		// TODO Auto-generated method stub

	}

	public void onProgress() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		return DlgUtils.createAlertMessage(this, args);
	}

}
