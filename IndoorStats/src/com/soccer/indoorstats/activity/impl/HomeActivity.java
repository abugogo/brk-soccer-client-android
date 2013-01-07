package com.soccer.indoorstats.activity.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.soccer.db.local.DB_CONSTS;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.activity.impl.stats.StatsTabActivity;
import com.soccer.preferences.Prefs;
import com.soccer.preferences.SoccerPrefsActivity;

public class HomeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_layout);
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

	public void homeClick(View view) {

		if (view != null) {
			Intent appIntent = null;

			switch (view.getId()) {
			case R.id.homeButton1:
				appIntent = new Intent(this, PlayerActivity.class);
				break;
			case R.id.homeButton2:
				appIntent = new Intent(this, GroupActivity.class);
				break;
			case R.id.homeButton3:
				appIntent = new Intent(this, GameActivity.class);
				break;
			case R.id.homeButton4:
				appIntent = new Intent(this, StatsTabActivity.class);
				break;
			case R.id.homeButton5:
				break;
			case R.id.homeButton6:
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				Prefs sharedPrefs = new Prefs(this);
				sharedPrefs.setPreference(DB_CONSTS.KEY_ID, "");
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			}
			if(appIntent != null)
				startActivity(appIntent);
		}
	}
}
