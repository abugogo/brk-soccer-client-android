package com.soccer.indoorstats.activity.impl.stats;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.soccer.db.remote.R_DB_CONSTS;
import com.soccer.db.remote.RemoteDBAdapter;
import com.soccer.entities.EntityManager;
import com.soccer.entities.ITableRow;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.activity.i.IAsyncTaskAct;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.lib.SoccerException;
import com.soccer.preferences.Prefs;

public class StatsTableTab extends Activity implements IAsyncTaskAct {
	private ArrayList<ITableRow> m_pArr = null;
	private Prefs mPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats_strip_tab);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (m_pArr == null) {

			mPrefs = new Prefs(this);
			String sUrl = mPrefs.getPreference("server_port", "NULL");
			if (sUrl.equals("NULL")) {
				sUrl = R_DB_CONSTS.SERVER_DEFAULT;
			}

			try {
				RemoteDBAdapter.getCurrentTable(this, sUrl,
						"Downloading table data");
			} catch (Exception e) {
				e.printStackTrace();
				showDialog(0, DlgUtils.prepareDlgBundle(e.getMessage()));
			}
		}
	}

	@Override
	public void onSuccess(String result) {
		try {
			m_pArr = (ArrayList<ITableRow>) EntityManager.readTable(result);
			TableLayout tblLayout = (TableLayout) findViewById(R.id.strip_table);
			int i = 1;
			if (m_pArr != null) {
				for (Iterator<ITableRow> iter = m_pArr.iterator(); iter
						.hasNext(); i++) {
					com.soccer.entities.impl.TableRow tr = (com.soccer.entities.impl.TableRow) iter
							.next();
					TextView textView = new TextView(this);
					String entry = i + ")" + tr.getFname() + " "
							+ tr.getLname() + ": " + tr.getWins() + "-"
							+ (tr.getGames() - tr.getWins() - tr.getLosses())
							+ "-" + tr.getLosses() + " " + tr.getPoints();
					textView.setText(entry);
					textView.setTextColor(Color.BLACK);
					TableRow tRow = new TableRow(this);
					tRow.addView(textView);
					tblLayout.addView(tRow);
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