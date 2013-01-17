package com.soccer.indoorstats.adapters;

import java.util.LinkedList;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soccer.entities.IDAOGame.GameStatus;
import com.soccer.entities.impl.DAOGame;
import com.soccer.indoorstats.R;

public class GamesListAdapter extends SoccerBaseAdapter<DAOGame> {

	public GamesListAdapter(Activity a, LinkedList<DAOGame> d) {
		super(a, d);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.game_row, null);

		TextView team1 = (TextView) vi.findViewById(R.id.team1name);
		TextView team2 = (TextView) vi.findViewById(R.id.team2name);
		TextView score = (TextView) vi.findViewById(R.id.game_score); 

		DAOGame game = new DAOGame();
		game = data.get(position);
		final String gid = game.getGameId();
		vi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		GameStatus gs = game.getStatus();
		if (gs != null) {
			switch (gs) {
			case Failed:
				score.setTextColor(Color.RED);
				break;
			case Pending:
				score.setTextColor(Color.YELLOW);
				break;
			default:
				score.setTextColor(Color.parseColor("#040404"));
				break;
			}
		}
		else {
			score.setTextColor(Color.parseColor("#040404"));
		}
		team1.setText("Blue");
		team2.setText("White");
		score.setText(game.getBgoals() + ":" + game.getWgoals());
		return vi;
	}

}