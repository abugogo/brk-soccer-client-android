package com.soccer.indoorstats.activity.impl.stats;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import com.soccer.entities.IDAOLEvent.EventType;
import com.soccer.entities.impl.DAOAggrLEvents;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.services.StatsTableService;
import com.soccer.indoorstats.services.handlers.RequestHandler;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.indoorstats.utils.log.Logger;
//import android.app.ProgressDialog;

public abstract class AbstractEventTableTab extends Fragment {
	protected ArrayList<DAOAggrLEvents> m_pArr = null;
	//protected ProgressDialog mProgDialog;
	protected StatsTableService mBoundStatsService;
	protected boolean mIsBound;

	protected abstract EventType getEventType();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.mProgDialog = new ProgressDialog(getActivity());
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

	public void onGetTableSuccess(List<DAOAggrLEvents> lst) {
		m_pArr = new ArrayList<DAOAggrLEvents>(lst);
		fillData();
		//mProgDialog.dismiss();
	}

	protected abstract void fillData();
	
	protected void innerFillData(TableLayout tblLayout, String type_title) {
		tblLayout.setColumnStretchable(2, true);
		int i = 1;
		TableRow tRow = null;
		tRow = createTableRow("", "Player", type_title);
		tblLayout.addView(tRow, new TableLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		if (m_pArr != null) {

			for (Iterator<DAOAggrLEvents> iter = m_pArr.iterator(); iter
					.hasNext(); i++) {
				DAOAggrLEvents tr = (DAOAggrLEvents) iter.next();
				tRow = createTableRow(tr, i);
				tblLayout.addView(tRow, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			}
		}
	}

	private TableRow createTableRow(String pos, String name, String count) {
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
		v.setWidth(90);
		v.setText(count);
		v.setTextColor(Color.BLACK);
		v.setGravity(Gravity.RIGHT);
		tRow.addView(v);
		
		v = new TextView(getActivity());
		v.setWidth(90);
		v.setText("   ");
		v.setTextColor(Color.BLACK);
		v.setGravity(Gravity.RIGHT);
		tRow.addView(v);

		return tRow;
	}

	private TableRow createTableRow(DAOAggrLEvents tr, int pos) {
		return createTableRow(String.valueOf(pos), tr.getPlayerFName() + " "
				+ tr.getPlayerLName(), String.valueOf(tr.getCount()));
	}

	public void onGetTableFailure(int responseCode, String result) {
		//mProgDialog.dismiss();
		DlgUtils.showAlertMessage(getActivity(), "Failure", result);
		Logger.w("failed to load tables: " + result + " " + responseCode);

	}

	private ServiceConnection mStatsTableConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {

			mBoundStatsService = (StatsTableService) ((StatsTableService.LocalBinder) service)
					.getService();
			if (m_pArr == null) {
				//mProgDialog.setMessage("Getting Tables ...");
				//mProgDialog.show();
				mBoundStatsService.getTable(0,
						new RequestHandler<List<DAOAggrLEvents>>() {

							@Override
							public void onSuccess(List<DAOAggrLEvents> t) {
								onGetTableSuccess(t);
							}

							@Override
							public void onFailure(String reason, int errorCode) {
								onGetTableFailure(errorCode, reason);
							}
						}, getEventType());
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
					new Intent(getActivity().getApplicationContext(),
							StatsTableService.class), mStatsTableConnection,
					Context.BIND_AUTO_CREATE);
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