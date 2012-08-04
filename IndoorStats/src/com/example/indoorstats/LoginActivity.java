package com.example.indoorstats;

import com.soccer.connector.RemoteDBAdapter;
import com.soccer.dal.entities.api.IDAOPlayer;
import com.soccer.dal.entities.impl.DAOPlayer;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class LoginActivity extends Activity {

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
			EditText et = (EditText)findViewById(R.id.editIdNumber); 
			String id = et.getText().toString();
			
			DAOPlayer p = (DAOPlayer) rda.getPlayer(sUrl, id);
			Intent appIntent = new Intent(this, actOpen.class);
			appIntent.putExtra("player", p);
			startActivity(appIntent);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showAlertMessage(e.getMessage());
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
