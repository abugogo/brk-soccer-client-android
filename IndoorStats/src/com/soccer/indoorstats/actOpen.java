package com.soccer.indoorstats;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.soccer.indoorstats.R;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.preferences.SoccerPrefsActivity;

public class actOpen extends TabActivity {

	private String mPID = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TabHost tabHost = getTabHost();
		Intent i = getIntent();
		mPID = (String)i.getSerializableExtra("player_id");
		
		// Tab for Photos
		TabSpec playerspec = tabHost.newTabSpec("Player");
		playerspec.setIndicator("Player",
				getResources().getDrawable(R.drawable.icon_photos_tab));
		Intent playerIntent = new Intent(this, PlayerActivity.class);
		playerIntent.putExtra("player_id", mPID);
		playerspec.setContent(playerIntent);
		


		// Tab for Songs
		TabSpec groupspec = tabHost.newTabSpec("Group");
		// setting Title and Icon for the Tab
		groupspec.setIndicator("Group",
				getResources().getDrawable(R.drawable.icon_songs_tab));
		Intent groupIntent = new Intent(this, GroupActivity.class);
		groupIntent.putExtra("player_id", mPID);
		groupspec.setContent(groupIntent);

		// Tab for Videos
		TabSpec gamespec = tabHost.newTabSpec("Game");
		gamespec.setIndicator("Game",
				getResources().getDrawable(R.drawable.icon_videos_tab));
		Intent gameIntent = new Intent(this, GameActivity.class);
		gameIntent.putExtra("player_id", mPID);
		gamespec.setContent(gameIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(playerspec); // Adding photos tab
		tabHost.addTab(groupspec); // Adding songs tab
		tabHost.addTab(gamespec); // Adding videos tab
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

}
