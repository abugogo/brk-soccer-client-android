package com.soccer.indoorstats.activity.impl.stats;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.soccer.indoorstats.R;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

public class StatsTabActivity extends FragmentActivity {
	private static final String[] CONTENT = new String[] { "Standings",
			"Scorers", "Assists", "Own Goals", "Yellow Cards", "Red Cards" };
	private static final int[] ICONS = new int[] { R.drawable.perm_group_pitch,
			R.drawable.perm_group_ball, R.drawable.perm_group_ball,
			R.drawable.perm_group_ball, R.drawable.perm_group_flag,
			R.drawable.perm_group_flag, };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_tabs);

		FragmentPagerAdapter adapter = new FragmentTabSelector(
				getSupportFragmentManager());

		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}

	class FragmentTabSelector extends FragmentPagerAdapter implements
			IconPagerAdapter {

		public FragmentTabSelector(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment f = null;

			switch (position) {
			case 0:
				f = new StatsTableTab();
				break;
			case 1:
				f = new ScorersTableTab();
				break;
			case 2:
				f = new AssistsTableTab();
				break;
			case 3:
				f = new OwnGoalTableTab();
				break;
			case 4:
				f = new YellowCardsTableTab();
				break;
			case 5:
				f = new RedCardsTableTab();
				break;
			}
			if (f != null) {
				return f;
			}
			return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase(Locale
					.getDefault());
		}

		@Override
		public int getIconResId(int index) {
			return ICONS[index];
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}

}
