package com.soccer.indoorstats.adapters;

import java.util.LinkedList;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soccer.entities.DAOGameListEntry;
import com.soccer.entities.IDAOGame.GameStatus;
import com.soccer.indoorstats.R;

public class GamesListAdapter extends SoccerBaseAdapter<DAOGameListEntry> {

	public GamesListAdapter(Activity a, LinkedList<DAOGameListEntry> d) {
		super(a, d);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		

		DAOGameListEntry game = data.get(position);

		if (!game.isSection()) {
			if (convertView == null || convertView.getId() != R.layout.game_row)
				vi = inflater.inflate(R.layout.game_row, null);
			TextView team1 = (TextView) vi.findViewById(R.id.team1name);
			TextView team2 = (TextView) vi.findViewById(R.id.team2name);
			TextView score = (TextView) vi.findViewById(R.id.game_score);

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
					vi.setBackgroundResource(R.drawable.list_selector_normal_red);
					break;
				case Pending:
					vi.setBackgroundResource(R.drawable.list_selector_normal_yellow);
					break;
				default:
					vi.setBackgroundResource(R.drawable.list_selector_normal);
					break;
				}
			} else {
				vi.setBackgroundResource(R.drawable.list_selector_normal);
			}
			team1.setText("Blue");
			team2.setText("White");
			score.setText(game.getBgoals() + ":" + game.getWgoals());
		}
		else {
			if (convertView == null || convertView.getId() != R.layout.game_row_title)
				vi = inflater.inflate(R.layout.game_row_title, null);
			TextView ttl = (TextView) vi.findViewById(R.id.game_section_title);
			ttl.setText(game.getTitle());
		}
		
		return vi;
	}
}