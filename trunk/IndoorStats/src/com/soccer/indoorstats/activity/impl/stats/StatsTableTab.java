package com.soccer.indoorstats.activity.impl.stats;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.soccer.db.remote.R_DB_CONSTS;
import com.soccer.db.remote.RemoteDBAdapter;
import com.soccer.entities.EntityManager;
import com.soccer.entities.ITableRow;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.activity.i.IAsyncTaskAct;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.lib.SoccerException;
import com.soccer.preferences.Prefs;
import com.soccer.rest.LoopjRestClient;

public class StatsTableTab extends Activity{
	private ArrayList<ITableRow> m_pArr = null;
	private Prefs mPrefs;
	private ProgressDialog mProgDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats_strip_tab);
		this.mProgDialog = new ProgressDialog(this);

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
				this.mProgDialog.setMessage("Getting Tables ...");
				this.mProgDialog.show();
				LoopjRestClient.get(sUrl.concat("/SoccerServer/rest/").concat(mPrefs.getPreference("account_name", "")).concat("/table/"),
						null, new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONArray res) {
								onGetTableSuccess(res.toString());
							}

							@Override
							public void onFailure(Throwable tr, String res) {
								onGetTableFailure(0, tr.getMessage());
							}

							@Override
							public void onFinish() {
								if (mProgDialog.isShowing())
									mProgDialog.dismiss();
								Logger.i("Get Tables finished");
							}
						});
				
			} catch (Exception e) {
				Logger.e("get current table failed", e);
				showDialog(0, DlgUtils.prepareDlgBundle(e.getMessage()));
			}
		}
	}

	public void onGetTableSuccess(String result) {
		try {
			m_pArr = (ArrayList<ITableRow>) EntityManager.readTable(result);
			TableLayout tblLayout = (TableLayout) findViewById(R.id.strip_table);
			tblLayout.setColumnStretchable(5, true);
			int i = 1;
			TableRow tRow = null; 
			tRow = createTableRow("", "Player", "W", "D", "L", "Points");
			tblLayout.addView(tRow, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			if (m_pArr != null) {

				for (Iterator<ITableRow> iter = m_pArr.iterator(); iter
						.hasNext(); i++) {
					com.soccer.entities.impl.TableRow tr = (com.soccer.entities.impl.TableRow) iter
							.next();
					tRow = createTableRow(tr, i);
					tblLayout.addView(tRow, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				}
			}
		} catch (SoccerException e) {
			Logger.e("onSuccess of stats table tab failed", e);
		}

	}
	
	private TableRow createTableRow(String pos, String name, String wins, String draws, String loses, String points) {
		TableRow tRow = new TableRow(this);
		tRow.setBackgroundResource(R.drawable.list_selector);
		
		TextView v = new TextView(this);
		v.setText(pos);
		v.setTextColor(Color.BLACK);
		tRow.addView(v);
		v = new TextView(this);
		v.setWidth(145);
		v.setEllipsize(TruncateAt.MARQUEE);
		v.setText(name);
		v.setTextColor(Color.BLACK);
		tRow.addView(v);
		v = new TextView(this);
		v.setWidth(30);
		v.setText(wins);
		v.setTextColor(Color.BLACK);
		tRow.addView(v);
		v = new TextView(this);
		v.setWidth(30);
		v.setText(draws);
		v.setTextColor(Color.BLACK);
		tRow.addView(v);
		v = new TextView(this);
		v.setWidth(30);
		v.setText(loses);
		v.setTextColor(Color.BLACK);
		tRow.addView(v);
		v = new TextView(this);
		v.setPadding(0, 0, 20, 0);
		v.setWidth(65);
		v.setText(points);
		v.setTextColor(Color.BLACK);
		v.setGravity(Gravity.RIGHT);
		tRow.addView(v);

		return tRow;
	}
	
	private TableRow createTableRow(com.soccer.entities.impl.TableRow tr, int pos) {
		return createTableRow(String.valueOf(pos), tr.getFname() + " " + tr.getLname(), String.valueOf(tr.getWins()), String.valueOf(tr.getGames() - tr.getWins() - tr.getLosses()), String.valueOf(tr.getLosses()), String.valueOf(tr.getPoints()));
	}

	private void addTableRow(TableLayout tblLayout, View vi) {
		TableRow tRow = new TableRow(this);
		tRow.addView(vi);
		tblLayout.addView(tRow);
	}
	
	private View createView(String pos, String name, String wins, String draws, String loses, String points) {
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
	
	private View createView(com.soccer.entities.impl.TableRow tr, int pos) {
		return createView(String.valueOf(pos), tr.getFname() + " " + tr.getLname(), String.valueOf(tr.getWins()), String.valueOf(tr.getGames() - tr.getWins() - tr.getLosses()), String.valueOf(tr.getLosses()), String.valueOf(tr.getPoints()));
	}

	public void onGetTableFailure(int responseCode, String result) {
		// TODO Auto-generated method stub
		Logger.w("failed to load tables: " + result + " " + responseCode);

	}

}