package com.soccer.indoorstats.adapters;

import java.util.LinkedList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soccer.indoorstats.R;

public class TestItemsListAdapter extends SoccerBaseAdapter<String>{

	
	public TestItemsListAdapter(Activity a, LinkedList<String> d) {
		super(a, d);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.testitem, null);
		
		
		TextView txtV = (TextView)vi.findViewById(R.id.test_text_view);
		txtV.setText(data.get(position));
		
		return vi;
	}

}
