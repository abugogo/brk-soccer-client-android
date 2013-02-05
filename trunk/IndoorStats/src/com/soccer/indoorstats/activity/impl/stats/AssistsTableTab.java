package com.soccer.indoorstats.activity.impl.stats;

import com.soccer.entities.IDAOLEvent.EventType;
import com.soccer.indoorstats.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;

public class AssistsTableTab extends AbstractEventTableTab {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout layout = (LinearLayout) inflater.inflate(
					R.layout.assists_table_tab, container, false);
		return layout;
	}

	protected void fillData() {
		TableLayout tblLayout = (TableLayout) getActivity().findViewById(
				R.id.assists_table);
		innerFillData(tblLayout, "Assists");
	}

	@Override
	protected EventType getEventType() {
		return EventType.Cook;
	}
}