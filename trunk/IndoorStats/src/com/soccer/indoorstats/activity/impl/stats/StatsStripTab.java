package com.soccer.indoorstats.activity.impl.stats;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
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
	private ArrayList<IWinLoseStrip> m_pArr = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats_strip_tab);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(m_pArr == null) {
			
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
	}

	@Override
	public void onSuccess(String result) {
		try {
			m_pArr = (ArrayList<IWinLoseStrip>)EntityManager
					.readPlayerStats(result);
			TableLayout tblLayout = (TableLayout)findViewById(R.id.strip_table);
			if(m_pArr != null) {
				for (Iterator<IWinLoseStrip> iter = m_pArr.iterator(); iter.hasNext(); ) {
					WinLoseStrip wls = (WinLoseStrip) iter.next();
					int minStrips = getMinStrip(6);
					if( minStrips <= wls.getNumber()) {
						TextView textView = new TextView(this);
						String winlose = wls.getType().equals("WIN")?"Wins: ": "Loses: ";
						textView.setText(winlose + wls.getNumber() + ". " + SimpleDateFormat.getDateInstance().format(wls.getStartDate()) + "-" + SimpleDateFormat.getDateInstance().format(wls.getEndDate()));
						textView.setTextColor(Color.BLACK);
						TableRow tRow = new TableRow(this);
						tRow.addView(textView);
						tblLayout.addView(tRow);
					}
				}
			}
		} catch (SoccerException e) {
			e.printStackTrace();
		}

	}

	private int getMinStrip(int defMin) {
		String ret = mPrefs.getPreference("stats_strip_min_id",
				String.valueOf(defMin));
		return Integer.parseInt(ret);
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