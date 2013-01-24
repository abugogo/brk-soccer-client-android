package com.soccer.indoorstats.adapters;

import java.util.LinkedList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.soccer.entities.impl.DAOLEvent;
import com.soccer.indoorstats.R;

public class RecordListAdapter extends SoccerBaseAdapter<DAOLEvent>{

	
	public RecordListAdapter(Activity a, LinkedList<DAOLEvent> d) {
		super(a, d);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.record_row_in_player, null);
		
		
		ImageView medal_image = (ImageView)vi.findViewById(R.id.record_image);
		DAOLEvent evt = data.get(position);
		switch(evt.getType()){
		
		case Goal:
			medal_image.setImageResource(R.drawable.goals_scored);
			break;
			
		case Cook:
			medal_image.setImageResource(R.drawable.rank);
			break;
			
		case R_Card:
			medal_image.setImageResource(R.drawable.red_card);
			break;

		case Y_Card:
			medal_image.setImageResource(R.drawable.twentyfivegoals_icon);
			break;
		default:
			break;
			

		}
		
		return vi;
	}

}
