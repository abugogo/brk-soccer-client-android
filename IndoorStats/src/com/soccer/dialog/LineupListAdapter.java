package com.soccer.dialog;

import java.sql.Time;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soccer.dal.entities.PrintableLineup;
import com.soccer.entities.IDAOLEvent.EventType;
import com.soccer.entities.impl.DAOLEvent;
import com.soccer.indoorstats.R;

public class LineupListAdapter extends BaseAdapter {

	private Activity activity;
	private LinkedHashMap<String, PrintableLineup> data = new LinkedHashMap<String, PrintableLineup>();
	private static LayoutInflater inflater = null;

	public LineupListAdapter(Activity a, LinkedHashMap<String, PrintableLineup> d) {
		activity = a;
		setData(d);
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public PrintableLineup getItem(int position) {
		if (position < data.size() && !(position < 0))
			return (PrintableLineup) data.values().toArray()[position];
		return null;
	}

	public void addItem(PrintableLineup l) {
		if (data == null)
			data = new LinkedHashMap<String, PrintableLineup>();
		if (l != null)
			data.put(l.getPlayerId(), l);
	}

	public long getItemId(int position) {
		return position;
	}

	public final LinkedHashMap<String, PrintableLineup> getData() {
		return data;
	}

	public void setData(LinkedHashMap<String, PrintableLineup> map) {
		clearData();
		data.putAll(map);
		notifyDataSetChanged();
	}
	
	public void clearData() {
		data.clear();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.player_row_in_game, null);

		TextView txtFirst = (TextView) vi.findViewById(R.id.pfname);
		RelativeLayout rlToTag = (RelativeLayout) vi
				.findViewById(R.id.pplayertaglayout);
		final PrintableLineup lp = getItem(position);
		if (lp != null) {
			rlToTag.setTag(lp.getPlayerId());
			rlToTag.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					final CharSequence[] items = { "Goal", "Cook", "Own goal",
							"Yellow card", "Red card", "-Reset-" };

					AlertDialog.Builder builder = new AlertDialog.Builder(v
							.getContext());
					builder.setTitle("Event");
					builder.setItems(items,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									EventType evt = EventType.Goal;
									boolean resetEvents = false;
									DAOLEvent dle = new DAOLEvent();
									switch (item) {
									case 0:
										evt = EventType.Goal;
										break;
									case 1:
										evt = EventType.Cook;
										break;
									case 2:
										evt = EventType.O_Goal;
										break;
									case 3:
										evt = EventType.Y_Card;
										break;
									case 4:
										evt = EventType.R_Card;
										break;
									case 5:
										resetEvents = true;
									}
									if (resetEvents)
										lp.clearEvents();
									else {
										dle.setType(evt);
										dle.setTime(new Time(0));
										lp.addEvent(dle);
									}
									notifyDataSetChanged();
								}
							});
					AlertDialog alert = builder.create();
					alert.show();
				}
			});

			txtFirst.setText(new String(lp.toStringEvents()));
		}
		return vi;
	}
}