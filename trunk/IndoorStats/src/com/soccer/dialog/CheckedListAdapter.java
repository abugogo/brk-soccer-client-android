package com.soccer.dialog;

import java.util.ArrayList;

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
import android.widget.Toast;

import com.soccer.indoorstats.R;
import com.soccer.indoorstats.ingame.GameEvent;
import com.soccer.indoorstats.ingame.IGameEvent;

public class CheckedListAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<lstItem> data = new ArrayList<lstItem>();
	private static LayoutInflater inflater = null;

	public CheckedListAdapter(Activity a, ArrayList<lstItem> d) {
		activity = a;
		setData(d);
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void setData(ArrayList<lstItem> arr) {
		data.clear();
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).mChecked)
				data.add(arr.get(i));
		}
		notifyDataSetChanged();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.player_row_in_game, null);

		TextView txtFirst = (TextView) vi.findViewById(R.id.pfname);
		RelativeLayout rlToTag = (RelativeLayout) vi
				.findViewById(R.id.pplayertaglayout);
		rlToTag.setTag(data.get(position).mId);
		rlToTag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				final CharSequence[] items = { "Goal", "Cook", "Own goal", "Yellow card",
						"Red card", "-Reset-" };

				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder.setTitle("Event");
				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						Toast.makeText(
								v.getContext(),
								data.get(position).mText
										+ " received " + items[item],
								Toast.LENGTH_SHORT).show();
						IGameEvent.EventType evt = IGameEvent.EventType.Goal;;
						boolean resetEvents = false;
						switch(item) {
						case 0:
							evt = IGameEvent.EventType.Goal;
							break;
						case 1:
							evt = IGameEvent.EventType.Cook;
							break;
						case 2:
							evt = IGameEvent.EventType.O_Goal;
							break;
						case 3:
							evt = IGameEvent.EventType.Y_Card;
							break;
						case 4:
							evt = IGameEvent.EventType.R_Card;
							break;
						case 5:
							resetEvents = true;
						}
						if(resetEvents)
							data.get(position).mEvents.clear();
						else
							data.get(position).addEvent(new GameEvent(evt, 0));
						notifyDataSetChanged();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		if (data.get(position).mChecked) {
			String text = new String(data.get(position).mText);
			txtFirst.setText(new String(data.get(position).toStringEvents()));
		} else
			vi.setVisibility(View.INVISIBLE);

		return vi;
	}
}