package com.soccer.imageListUtils;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.indoorstats.R;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.player_row, null);

        TextView pfname = (TextView)vi.findViewById(R.id.pfname); // title
        TextView plname = (TextView)vi.findViewById(R.id.plname); // artist name
        TextView tel = (TextView)vi.findViewById(R.id.ptel1); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        
        HashMap<String, String> player = new HashMap<String, String>();
        player = data.get(position);
        
        // Setting all values in listview
        pfname.setText(player.get(PlayersDbAdapter.KEY_FNAME));
        plname.setText(player.get(PlayersDbAdapter.KEY_LNAME));
        tel.setText(player.get(PlayersDbAdapter.KEY_TEL1));
        imageLoader.DisplayImage(player.get(PlayersDbAdapter.KEY_IMG), thumb_image);
        return vi;
    }
}