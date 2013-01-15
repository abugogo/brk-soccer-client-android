package com.soccer.imageListUtils;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soccer.entities.impl.DAOPlayer;
import com.soccer.entities.impl.PrintableLineup;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.utils.ActivitySwipeDetector;
import com.soccer.indoorstats.utils.ISwipeInterface;

public class TeamSelectionAdapter extends BaseAdapter implements
		ISwipeInterface {

	private ListActivity activity;
	private ArrayList<DAOPlayer> data = new ArrayList<DAOPlayer>();
	private HashMap<String, PrintableLineup> lpdata = new HashMap<String, PrintableLineup>();;
	private static LayoutInflater inflater = null;

	public TeamSelectionAdapter(ListActivity a, ArrayList<DAOPlayer> d,
			HashMap<String, PrintableLineup> lpd) {
		activity = a;
		if (d != null)
			data = d;
		if (lpd != null)
			lpdata = lpd;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.player_selection_row, null);

		ActivitySwipeDetector swipe = new ActivitySwipeDetector(this);
		vi.setOnTouchListener(swipe);

		TextView txtPfname = (TextView) vi.findViewById(R.id.pfname); // player
		TextView txtPlname = (TextView) vi.findViewById(R.id.plname); // player
																	// last name
																	// image

		DAOPlayer player = new DAOPlayer();
		player = data.get(position);
		final String pid = player.getId();
		final String pfname = player.getFname();
		final String plname = player.getLname();
		
		vi.setTag(R.id.pidtag, pid);
		vi.setTag(R.id.fntag, pfname);
		vi.setTag(R.id.lntag, plname);
		
		vi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LinearLayout r = (LinearLayout) v.findViewById(R.id.unithumbright);
				LinearLayout l = (LinearLayout) v.findViewById(R.id.unithumbleft);
				r.setVisibility(View.INVISIBLE);
				l.setVisibility(View.INVISIBLE);
				lpdata.remove(pid);

			}
		});

		// Setting all values in listview
		txtPfname.setText(player.getFname());
		txtPlname.setText(player.getLname());
		LinearLayout r = (LinearLayout) vi.findViewById(R.id.unithumbright);
		LinearLayout l = (LinearLayout) vi.findViewById(R.id.unithumbleft);
		r.setVisibility(View.INVISIBLE);
		l.setVisibility(View.INVISIBLE);
		PrintableLineup lp = lpdata.get(pid);
		if (lp != null) {
			if (lp.getColor().equals('b')) {
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
		LinearLayout r = (LinearLayout) v.findViewById(R.id.unithumbright);
		LinearLayout l = (LinearLayout) v.findViewById(R.id.unithumbleft);
		r.setVisibility(View.VISIBLE);
		l.setVisibility(View.INVISIBLE);
		PrintableLineup d = new PrintableLineup();
		d.setColor('b');
		d.setPlayerId((String) v.getTag(R.id.pidtag));
		d.setFname((String) v.getTag(R.id.fntag));
		d.setLname((String) v.getTag(R.id.lntag));
		lpdata.put(d.getPlayerId(), d);
	}

	@Override
	public void right2left(View v) {
		LinearLayout r = (LinearLayout) v.findViewById(R.id.unithumbright);
		LinearLayout l = (LinearLayout) v.findViewById(R.id.unithumbleft);
		r.setVisibility(View.INVISIBLE);
		l.setVisibility(View.VISIBLE);
		PrintableLineup d = new PrintableLineup();
		d.setColor('w');
		d.setPlayerId((String) v.getTag(R.id.pidtag));
		d.setFname((String) v.getTag(R.id.fntag));
		d.setLname((String) v.getTag(R.id.lntag));
		lpdata.put(d.getPlayerId(), d);
	}

	@Override
	public void top2bottom(View v) {
		// TODO Auto-generated method stub

	}

	public HashMap<String, PrintableLineup> getLpdata() {
		return lpdata;
	}
}