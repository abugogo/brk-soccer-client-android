package com.soccer.indoorstats.activity.impl.stats;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import com.soccer.entities.EntityManager;
import com.soccer.entities.ITableRow;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.services.StatsTableService;
import com.soccer.indoorstats.services.handlers.RequestHandler;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.lib.SoccerException;

public class StatsTableTab extends Fragment {
	private ArrayList<ITableRow> m_pArr = null;
	private ProgressDialog mProgDialog;
	private StatsTableService mBoundStatsService;
	private boolean mIsBound;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mProgDialog = new ProgressDialog(getActivity());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.prime_table_tab, container, false);

		return layout;
	}

	@Override
	public void onPause() {
		super.onPause();
		doUnbindServices();
	}

	@Override
	public void onResume() {
		super.onResume();
		doBindServices();
	}

	public void onGetTableSuccess(String result) {
		try {
			m_pArr = (ArrayList<ITableRow>) EntityManager.readTable(result);
			fillData();
			mProgDialog.dismiss();
		} catch (SoccerException e) {
			Logger.e("onSuccess of stats table tab failed", e);
		}

	}

	private void fillData() {
		TableLayout tblLayout = (TableLayout) getActivity().findViewById(
				R.id.prime_table);
		tblLayout.setColumnStretchable(5, true);
		int i = 1;
		TableRow tRow = null;
		tRow = createTableRow("", "Player", "W", "D", "L", "Points");
		tblLayout.addView(tRow, new TableLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		if (m_pArr != null) {

			for (Iterator<ITableRow> iter = m_pArr.iterator(); iter.hasNext(); i++) {
				com.soccer.entities.impl.TableRow tr = (com.soccer.entities.impl.TableRow) iter
						.next();
				tRow = createTableRow(tr, i);
				tblLayout.addView(tRow, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			}
		}
	}

	private TableRow createTableRow(String pos, String name, String wins,
			String draws, String loses, String points) {
		TableRow tRow = new TableRow(getActivity());
		tRow.setBackgroundResource(R.drawable.list_selector);

		TextView v = new TextView(getActivity());
		v.setText(pos);
		v.setTextColor(Color.BLACK);
		tRow.addView(v);
		v = new TextView(getActivity());
		v.setWidth(145);
		v.setEllipsize(TruncateAt.MARQUEE);
		v.setText(name);
		v.setTextColor(Color.BLACK);
		tRow.addView(v);
		v = new TextView(getActivity());
		v.setWidth(30);
		v.setText(wins);
		v.setTextColor(Color.BLACK);
		tRow.addView(v);
		v = new TextView(getActivity());
		v.setWidth(30);
		v.setText(draws);
		v.setTextColor(Color.BLACK);
		tRow.addView(v);
		v = new TextView(getActivity());
		v.setWidth(30);
		v.setText(loses);
		v.setTextColor(Color.BLACK);
		tRow.addView(v);
		v = new TextView(getActivity());
		v.setPadding(0, 0, 20, 0);
		v.setWidth(90);
		v.setText(points);
		v.setTextColor(Color.BLACK);
		v.setGravity(Gravity.RIGHT);
		tRow.addView(v);

		return tRow;
	}

	private TableRow createTableRow(com.soccer.entities.impl.TableRow tr,
			int pos) {
		return createTableRow(String.valueOf(pos),
				tr.getFname() + " " + tr.getLname(),
				String.valueOf(tr.getWins()),
				String.valueOf(tr.getGames() - tr.getWins() - tr.getLosses()),
				String.valueOf(tr.getLosses()), String.valueOf(tr.getPoints()));
	}

	public void onGetTableFailure(int responseCode, String result) {
		mProgDialog.dismiss();
		DlgUtils.showAlertMessage(getActivity(), "Failure", result);
		Logger.w("failed to load tables: " + result + " " + responseCode);

	}

	private ServiceConnection mStatsTableConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			
			mBoundStatsService = (StatsTableService) ((StatsTableService.LocalBinder) service)
					.getService();
			if (m_pArr == null) {
				mProgDialog.setMessage("Getting Tables ...");
				mProgDialog.show();
				mBoundStatsService.getPrimeTable(0,
						new RequestHandler<String>() {

							@Override
							public void onSuccess(String t) {
								onGetTableSuccess(t);
							}

							@Override
							public void onFailure(String reason, int errorCode) {
								onGetTableFailure(errorCode, reason);
							}
						});
			} else
				fillData();
		}

		public void onServiceDisconnected(ComponentName className) {
			mBoundStatsService = null;
		}
	};

	private void doBindServices() {
		if (!mIsBound) {
			getActivity().bindService(
					new Intent(getActivity().getApplicationContext(), StatsTableService.class),
					mStatsTableConnection, Context.BIND_AUTO_CREATE);
			mIsBound = true;

		}
	}

	private void doUnbindServices() {
		if (mIsBound) {
			getActivity().unbindService(mStatsTableConnection);
			mIsBound = false;
		}
	}
}