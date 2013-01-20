package com.soccer.indoorstats.adapters;

import java.util.LinkedList;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soccer.entities.impl.PrintableLineup;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.utils.ActivitySwipeDetector;
import com.soccer.indoorstats.utils.ISwipeInterface;

public class TeamSelectionAdapter extends SoccerBaseAdapter<PrintableLineup>
		implements ISwipeInterface {

	public TeamSelectionAdapter(Activity a, LinkedList<PrintableLineup> d) {
		super(a, d);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.player_selection_row, null);

		ActivitySwipeDetector swipe = new ActivitySwipeDetector(this);
		vi.setOnTouchListener(swipe);

		TextView txtPfname = (TextView) vi.findViewById(R.id.pfname); // player
		TextView txtPlname = (TextView) vi.findViewById(R.id.plname); // player
																		// last
																		// name
																		// image

		PrintableLineup plu = new PrintableLineup();
		plu = data.get(position);
		final String pid = plu.getPlayerId();
		final String pfname = plu.getFname();
		final String plname = plu.getLname();

		vi.setTag(R.id.rowid, position);
		vi.setTag(R.id.pidtag, pid);
		vi.setTag(R.id.fntag, pfname);
		vi.setTag(R.id.lntag, plname);

		vi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LinearLayout r = (LinearLayout) v
						.findViewById(R.id.unithumbright);
				LinearLayout l = (LinearLayout) v
						.findViewById(R.id.unithumbleft);
				r.setVisibility(View.INVISIBLE);
				l.setVisibility(View.INVISIBLE);
				data.get(position).setColor(null);

			}
		});

		// Setting all values in listview
		txtPfname.setText(plu.getFname());
		txtPlname.setText(plu.getLname());
		LinearLayout r = (LinearLayout) vi.findViewById(R.id.unithumbright);
		LinearLayout l = (LinearLayout) vi.findViewById(R.id.unithumbleft);
		r.setVisibility(View.INVISIBLE);
		l.setVisibility(View.INVISIBLE);
		if (plu.getColor() != null) {
			if (plu.getColor().equals('b')) {
				r.setVisibility(View.VISIBLE);
			} else {
				l.setVisibility(View.VISIBLE);
			}
		}

		return vi;
	}

	@Override
	public void bottom2top(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void left2right(View v) {
		int pos = (Integer) v.getTag(R.id.rowid);
		LinearLayout r = (LinearLayout) v.findViewById(R.id.unithumbright);
		LinearLayout l = (LinearLayout) v.findViewById(R.id.unithumbleft);
		r.setVisibility(View.VISIBLE);
		l.setVisibility(View.INVISIBLE);
		data.get(pos).setColor('b');
	}

	@Override
	public void right2left(View v) {
		int pos = (Integer) v.getTag(R.id.rowid);
		LinearLayout r = (LinearLayout) v.findViewById(R.id.unithumbright);
		LinearLayout l = (LinearLayout) v.findViewById(R.id.unithumbleft);
		r.setVisibility(View.INVISIBLE);
		l.setVisibility(View.VISIBLE);
		data.get(pos).setColor('w');
	}

	@Override
	public void top2bottom(View v) {
		// TODO Auto-generated method stub

	}
}