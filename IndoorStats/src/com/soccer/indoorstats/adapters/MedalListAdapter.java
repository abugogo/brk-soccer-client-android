package com.soccer.indoorstats.adapters;

import java.util.LinkedList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.soccer.entities.IDAOMedal.MedalEnum;
import com.soccer.entities.impl.DAOMedal;
import com.soccer.indoorstats.R;

public class MedalListAdapter extends SoccerBaseAdapter<DAOMedal>{

	
	

	public MedalListAdapter(Activity a, LinkedList<DAOMedal> d) {
		super(a, d);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.medals_row_in_player, null);
		
		
		ImageView medal_image = (ImageView)vi.findViewById(R.id.medal_image);
		MedalEnum medal = data.get(position).getMedalType();
		switch(medal){
		
		case GOALS_PER_GAME:
			medal_image.setImageResource(R.drawable.hattrik_icon);
			break;
			
		case AGGR_GOALS:
			medal_image.setImageResource(R.drawable.twentyfivegoals_icon);
			break;
			
		case WIN_STREEK:
			medal_image.setImageResource(R.drawable.tengamewinstreek_icon);

		}
		
		return vi;
	}

}
