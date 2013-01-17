package com.soccer.indoorstats.adapters;

import java.util.LinkedList;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soccer.entities.impl.DAOPlayer;
import com.soccer.imageListUtils.ImageLoader;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.activity.impl.PlayerActivity;
import com.soccer.indoorstats.utils.ActivitySwipeDetector;
import com.soccer.indoorstats.utils.ISwipeInterface;

public class LazyAdapter extends SoccerBaseAdapter<DAOPlayer> implements ISwipeInterface {

	public ImageLoader imageLoader;

	public LazyAdapter(ListActivity a, LinkedList<DAOPlayer> d) {
		super(a, d);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.player_row, null);

		ActivitySwipeDetector swipe = new ActivitySwipeDetector(this);
		vi.setOnTouchListener(swipe);

		TextView pfname = (TextView) vi.findViewById(R.id.pfname); // player
		TextView plname = (TextView) vi.findViewById(R.id.plname); // player
																	// last name
		TextView tel = (TextView) vi.findViewById(R.id.ptel1); // telephone
		ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image); // thumb
																				// image

		DAOPlayer player = new DAOPlayer();
		player = data.get(position);
		final String pid = player.getId();
		vi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent appIntent = new Intent(activity, PlayerActivity.class);
				Bundle b = new Bundle();
				b.putString("pid", pid); 
				appIntent.putExtras(b); 
				activity.startActivity(appIntent);

			}
		});
		
		// Setting all values in listview
		pfname.setText(player.getFname());
		plname.setText(player.getLname());
		tel.setText(player.getTel1());
		imageLoader.DisplayImage(player.getP_img(), thumb_image);
		return vi;
	}

	@Override
	public void bottom2top(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void left2right(View v) {
		TextView tel = (TextView) v.findViewById(R.id.ptel1);
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ tel.getText()));

		LazyAdapter.this.activity.startActivity(intent);
	}

	@Override
	public void right2left(View v) {
		TextView tel = (TextView) v.findViewById(R.id.ptel1);
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:"
				+ tel.getText()));

		LazyAdapter.this.activity.startActivity(intent);
	}

	@Override
	public void top2bottom(View v) {
		// TODO Auto-generated method stub

	}

}