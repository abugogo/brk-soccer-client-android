package com.soccer.indoorstats.activity.impl.stats;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.db.remote.R_DB_CONSTS;
import com.soccer.db.remote.RemoteDBAdapter;
import com.soccer.entities.EntityManager;
import com.soccer.entities.IWinLoseStrip;
import com.soccer.entities.impl.WinLoseStrip;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.activity.i.IAsyncTaskAct;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.lib.SoccerException;
import com.soccer.preferences.Prefs;

public class StatsStripTab extends Activity implements IAsyncTaskAct {

	private Prefs mPrefs;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats_strip_tab);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPrefs = new Prefs(this);
		String mPID = (String) mPrefs
				.getPreference(PlayersDbAdapter.KEY_ID, "");
		String sUrl = mPrefs.getPreference("server_port", "NULL");
		if (sUrl.equals("NULL")) {
			sUrl = R_DB_CONSTS.SERVER_DEFAULT;
		}

		try {
			RemoteDBAdapter.getPlayerStats(this, sUrl, mPID,
					"Downloading player stats");
		} catch (Exception e) {
			e.printStackTrace();
			showDialog(0, DlgUtils.prepareDlgBundle(e.getMessage()));
		}
	}

	@Override
	public void onSuccess(String result) {
		try {
			ArrayList<IWinLoseStrip> pArr = EntityManager
					.readPlayerStats(result);
			LinearLayout layout = (LinearLayout)findViewById(R.id.linear_stats_strip);
			if(pArr != null) {
				for (Iterator<IWinLoseStrip> iter = pArr.iterator(); iter.hasNext(); ) {
					WinLoseStrip wls = (WinLoseStrip) iter.next();
					TextView textView = new TextView(this);
					String winlose = wls.getType().equals("WIN")?"Wins: ": "Loses: ";
					textView.setText(winlose + wls.getNumber() + ". " + SimpleDateFormat.getDateInstance().format(wls.getStartDate()) + "-" + SimpleDateFormat.getDateInstance().format(wls.getEndDate()));
					textView.setTextColor(Color.BLACK);
					layout.addView(textView);
				}
			}
		} catch (SoccerException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onFailure(int responseCode, String result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProgress() {
		// TODO Auto-generated method stub

	}
}