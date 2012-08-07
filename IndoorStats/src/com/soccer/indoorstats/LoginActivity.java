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
import android.view.View;
import android.widget.EditText;

import com.soccer.indoorstats.R;
import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.db.remote.RemoteDBAdapter;
import com.soccer.entities.IDAOPlayer;
import com.soccer.entities.impl.DAOPlayer;

public class LoginActivity extends Activity {

	private PlayersDbAdapter mDbHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	public void loginClick(View view) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		String sUrl = sharedPrefs.getString("server_port", "NULL");
		if (sUrl.equals("NULL")) {
			showAlertMessage("Please set server and port");
		}

		try {
			RemoteDBAdapter rda = new RemoteDBAdapter();
			EditText et = (EditText) findViewById(R.id.editIdNumber);
			String id = et.getText().toString();

			DAOPlayer p = (DAOPlayer) rda.getPlayer(sUrl, id);
			if (p != null && p.getId() != null && p.getId() != "") {
				LoadPlayers();
				Intent appIntent = new Intent(this, actOpen.class);
				appIntent.putExtra("player", p);
				startActivity(appIntent);
			} else {
				showAlertMessage("Failed login");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showAlertMessage(e.getMessage());
		}
	}

	private void LoadPlayers() {
		mDbHelper = new PlayersDbAdapter(this);
		mDbHelper.open();
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
}
