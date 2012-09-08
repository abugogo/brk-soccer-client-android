package com.soccer.dialog;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soccer.indoorstats.R;

public class CheckedListAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<lstItem> data = new ArrayList<lstItem>();
    private static LayoutInflater inflater=null;
    
    public CheckedListAdapter(Activity a, ArrayList<lstItem> d) {
        activity = a;
        setData(d);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public void setData(ArrayList<lstItem> arr) {
    	data.clear();
    	for(int i=0; i<arr.size(); i++) {
        	if(arr.get(i).mChecked)
        		data.add(arr.get(i));
        }
    	notifyDataSetChanged();
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.player_row_in_game, null);

        TextView txtFirst = (TextView)vi.findViewById(R.id.pfname);
        txtFirst.setTag(data.get(position).mId);
        if(data.get(position).mChecked) {
        	txtFirst.setText(data.get(position).mText);
        }
        else
        	vi.setVisibility(View.INVISIBLE);
        
        return vi;
    }
    
    
}