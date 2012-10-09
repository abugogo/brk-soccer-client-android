package com.soccer.indoorstats.activity.impl.stats;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.activity.impl.GameActivity;
import com.soccer.indoorstats.activity.impl.GroupActivity;
import com.soccer.indoorstats.activity.impl.PlayerUpdateActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class StatsTabActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);
        
        final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle(R.string.stats);

		final Action StatsAction = new IntentAction(this, new Intent(this,
				StatsTabActivity.class), R.drawable.heart);
		actionBar.addAction(StatsAction);
		final Action EditAction = new IntentAction(this, new Intent(this,
				PlayerUpdateActivity.class), R.drawable.edit);
		actionBar.addAction(EditAction);
		final Action GroupAction = new IntentAction(this, new Intent(this,
				GroupActivity.class), R.drawable.players_icon);
		actionBar.addAction(GroupAction);
		final Action GameAction = new IntentAction(this, new Intent(this,
				GameActivity.class), R.drawable.game_icon);
		actionBar.addAction(GameAction);
        /* TabHost will have Tabs */
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        
        /* TabSpec used to create a new tab. 
         * By using TabSpec only we can able to setContent to the tab.
         * By using TabSpec setIndicator() we can set name to tab. */
        
        /* tid1 is firstTabSpec Id. Its used to access outside. */
        TabSpec firstTabSpec = tabHost.newTabSpec("tid1");
        TabSpec secondTabSpec = tabHost.newTabSpec("tid1");
        
        /* TabSpec setIndicator() is used to set name for the tab. */
        /* TabSpec setContent() is used to set content for a particular tab. */
        firstTabSpec.setIndicator("Standing Table").setContent(new Intent(this,StatsTableTab.class));
        secondTabSpec.setIndicator("Strips").setContent(new Intent(this,StatsStripTab.class));
        
        /* Add tabSpec to the TabHost to display. */
        tabHost.addTab(firstTabSpec);
        tabHost.addTab(secondTabSpec);
        
    }
}
