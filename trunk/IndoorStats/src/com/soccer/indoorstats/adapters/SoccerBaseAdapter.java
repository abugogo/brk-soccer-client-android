package com.soccer.indoorstats.adapters;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class SoccerBaseAdapter<T> extends BaseAdapter {
	protected Activity activity;
	protected LinkedList<T> data;
	protected static LayoutInflater inflater = null;

	public SoccerBaseAdapter(Activity a, LinkedList<T> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public T getItem(int arg0) {
		if (data != null && data.size() > arg0)
			return data.get(arg0);
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void addItem(T t) {
		if (data == null)
			data = new LinkedList<T>();
		if (t != null)
			data.add(t);
	}

	public final LinkedList<T> getData() {
		return data;
	}

	public void setData(LinkedList<T> list) {
		if (data == null)
			data = new LinkedList<T>();
		if (list != null) {
			data.addAll(list);
		}
		else
			data.clear();
		notifyDataSetChanged();
	}

	@Override
	public abstract View getView(int arg0, View arg1, ViewGroup arg2);

}
