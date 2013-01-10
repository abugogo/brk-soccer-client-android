package com.soccer.indoorstats.activity.impl.stats;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.activity.impl.GameActivity;
import com.soccer.indoorstats.activity.impl.GroupActivity;
import com.soccer.indoorstats.activity.impl.HomeActivity;

public class StatsTabActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats_tab);

		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle(R.string.stats);

		/*
		 * final Action StatsAction = new IntentAction(this, new Intent(this,
		 * StatsTabActivity.class), R.drawable.heart);
		 * actionBar.addAction(StatsAction); final Action EditAction = new
		 * IntentAction(this, new Intent(this, PlayerUpdateActivity.class),
		 * R.drawable.edit); actionBar.addAction(EditAction); final Action
		 * GroupAction = new IntentAction(this, new Intent(this,
		 * GroupActivity.class), R.drawable.players_icon);
		 * actionBar.addAction(GroupAction); final Action GameAction = new
		 * IntentAction(this, new Intent(this, GameActivity.class),
		 * R.drawable.game_icon); actionBar.addAction(GameAction);
		 */
		final Action HomeAction = new IntentAction(this, new Intent(this,
				HomeActivity.class), R.drawable.home_icon);
		actionBar.addAction(HomeAction);
		/* TabHost will have Tabs */
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

		/*
		 * TabSpec used to create a new tab. By using TabSpec only we can able
		 * to setContent to the tab. By using TabSpec setIndicator() we can set
		 * name to tab.
		 */

		/* tid1 is firstTabSpec Id. Its used to access outside. */
		TabSpec firstTabSpec = tabHost.newTabSpec("tid1");
		TabSpec secondTabSpec = tabHost.newTabSpec("tid1");

		/* TabSpec setIndicator() is used to set name for the tab. */
		/* TabSpec setContent() is used to set content for a particular tab. */
		firstTabSpec.setIndicator("Standing Table").setContent(
				new Intent(this, StatsTableTab.class));
		secondTabSpec.setIndicator("Strips").setContent(
				new Intent(this, StatsStripTab.class));

		/* Add tabSpec to the TabHost to display. */
		// tabHost.addTab(firstTabSpec);
		// tabHost.addTab(secondTabSpec);
		addTab(tabHost, new Intent(this, StatsTableTab.class),
				"Standing Table", R.drawable.arrow);
		addTab(tabHost, new Intent(this, StatsStripTab.class), "Strips",
				R.drawable.arrow);

		//buttom navigation bar
		RadioButton radioButton;
		radioButton = (RadioButton) findViewById(R.id.btnStats);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		radioButton = (RadioButton) findViewById(R.id.btnGame);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		radioButton = (RadioButton) findViewById(R.id.btnGroup);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		radioButton = (RadioButton) findViewById(R.id.btnSeason);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		
	}

	private CompoundButton.OnCheckedChangeListener btnNavBarOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				Toast.makeText(StatsTabActivity.this, buttonView.getText(),
						Toast.LENGTH_SHORT).show();

				Intent appIntent = null;

				switch (buttonView.getId()) {
				case R.id.btnGroup:
					appIntent = new Intent(StatsTabActivity.this,
							GroupActivity.class);
					break;
				case R.id.btnSeason:
					Toast.makeText(StatsTabActivity.this, "not implemented",
							Toast.LENGTH_SHORT).show();
					break;
				case R.id.btnGame:
					appIntent = new Intent(StatsTabActivity.this,
							GameActivity.class);
					break;
				}
				if (appIntent != null)
					startActivity(appIntent);

			}
		}
	};

	
	private void addTab(TabHost tabHost, Intent intent, String label,
			int drawableId) {
		TabHost.TabSpec spec = tabHost.newTabSpec(label);

		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_button, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(label);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);

		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}

}
