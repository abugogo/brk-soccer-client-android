package com.soccer.indoorstats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.soccer.indoorstats.R;
import com.soccer.entities.impl.DAOPlayer;

public class PlayerActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_layout);
		Intent i = getIntent();
		DAOPlayer p = (DAOPlayer) i.getSerializableExtra("player");
		((EditText) findViewById(R.id.editPlayerName)).setText(p.getFname()
				+ " " + p.getLname());
		((EditText) findViewById(R.id.editEmail)).setText(p.getEmail());
		((EditText) findViewById(R.id.editPhone)).setText(p.getTel1());
	}
}
