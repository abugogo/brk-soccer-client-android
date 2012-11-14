package com.soccer.indoorstats.activity.impl.stats;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
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
			View titles = createTableRow("", "Player", "W", "D", "L", "Points");
			addTableRow(tblLayout, titles);
			if (m_pArr != null) {

				for (Iterator<ITableRow> iter = m_pArr.iterator(); iter
						.hasNext(); i++) {
					com.soccer.entities.impl.TableRow tr = (com.soccer.entities.impl.TableRow) iter
							.next();
					View vi = createTableRow(tr, i);
					addTableRow(tblLayout, vi);
				}
			}
		} catch (SoccerException e) {
			e.printStackTrace();
		}

	}

	private void addTableRow(TableLayout tblLayout, View vi) {
		TableRow tRow = new TableRow(this);
		tRow.addView(vi);
		tblLayout.addView(tRow);
	}
	
	private View createTableRow(String pos, String name, String wins, String draws, String loses, String points) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View vi = inflater.inflate(R.layout.player_row_in_table, null);
		TextView v = (TextView) vi.findViewById(R.id.tbl_pos);
		v.setText(pos);
		v = (TextView) vi.findViewById(R.id.tbl_pfname);
		v.setText(name);
		v = (TextView) vi.findViewById(R.id.tbl_wins);
		v.setText(wins);
		v = (TextView) vi.findViewById(R.id.tbl_draws);
		v.setText(draws);
		v = (TextView) vi.findViewById(R.id.tbl_loses);
		v.setText(loses);
		v = (TextView) vi.findViewById(R.id.tbl_pnts);
		v.setText(points);
		return vi;
	}
	
	private View createTableRow(com.soccer.entities.impl.TableRow tr, int pos) {
		return createTableRow(String.valueOf(pos), tr.getFname() + " " + tr.getLname(), String.valueOf(tr.getWins()), String.valueOf(tr.getGames() - tr.getWins() - tr.getLosses()), String.valueOf(tr.getLosses()), String.valueOf(tr.getPoints()));
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