package com.soccer.indoorstats.activity.impl.stats;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.soccer.db.local.DB_CONSTS;
import com.soccer.db.remote.R_DB_CONSTS;
import com.soccer.entities.EntityManager;
import com.soccer.entities.IWinLoseStrip;
import com.soccer.entities.impl.WinLoseStrip;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.preferences.Prefs;
import com.soccer.rest.LoopjRestClient;

public class StatsStripTab extends Activity {

	private Prefs mPrefs;
	private ArrayList<IWinLoseStrip> m_pArr = null;
	private ProgressDialog mProgDialog;

	/** Called when the activity is first created. */
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
			String mPID = (String) mPrefs.getPreference(
					DB_CONSTS.KEY_ID, "");
			String sUrl = mPrefs.getPreference("server_port", "NULL");
			if (sUrl.equals("NULL")) {
				sUrl = R_DB_CONSTS.SERVER_DEFAULT;
			}

			try {
				this.mProgDialog.setMessage("Getting Player stats...");
				this.mProgDialog.show();
				LoopjRestClient.get(this,
						sUrl.concat("/SoccerServer/rest/")
								.concat(mPrefs
										.getPreference("account_name", ""))
								.concat("/players/").concat(mPID)
								.concat("/stats"), null,
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONArray res) {
								onGetStatsSuccess(res.toString());
							}

							@Override
							public void onFailure(Throwable tr, String res) {
								onGetStatsFailure(0, tr.getMessage());
							}

							@Override
							public void onFinish() {
								if (mProgDialog.isShowing())
									mProgDialog.dismiss();
								Logger.i("Get Player Stats finished");
							}
						});

			} catch (Exception e) {
				Logger.e("get stats failed", e);
				showDialog(0, DlgUtils.prepareDlgBundle(e.getMessage()));
			}
		}
	}

	public void onGetStatsSuccess(String result) {
		m_pArr = (ArrayList<IWinLoseStrip>) EntityManager
				.readPlayerStats(result);
		TableLayout tblLayout = (TableLayout) findViewById(R.id.strip_table);
		if (m_pArr != null) {
			for (Iterator<IWinLoseStrip> iter = m_pArr.iterator(); iter
					.hasNext();) {
				WinLoseStrip wls = (WinLoseStrip) iter.next();
				int minStrips = getMinStrip(6);
				if (minStrips <= wls.getNumber()) {
					TextView textView = new TextView(this);
					String winlose = wls.getType().equals("WIN") ? "Wins: "
							: "Loses: ";
					textView.setText(winlose
							+ wls.getNumber()
							+ ". "
							+ SimpleDateFormat.getDateInstance().format(
									wls.getStartDate())
							+ "-"
							+ SimpleDateFormat.getDateInstance().format(
									wls.getEndDate()));
					textView.setTextColor(Color.BLACK);
					TableRow tRow = new TableRow(this);
					tRow.addView(textView);
					tblLayout.addView(tRow);
				}
			}
		}
	}

	private int getMinStrip(int defMin) {
		String ret = mPrefs.getPreference("stats_strip_min_id",
				String.valueOf(defMin));
		return Integer.parseInt(ret);
	}

	public void onGetStatsFailure(int responseCode, String result) {
		// TODO Auto-generated method stub
		Logger.w("failed to load stats: " + result + " " + responseCode);

	}

}