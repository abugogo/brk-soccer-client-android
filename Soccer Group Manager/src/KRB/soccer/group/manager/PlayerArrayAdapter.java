package KRB.soccer.group.manager;
import java.util.List;

import KRB.soccer.group.manager.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class PlayerArrayAdapter extends ArrayAdapter<Player> {
	private final Context context;
	private final List<Player> playersList;
 
	public PlayerArrayAdapter(Context context, List<Player> playersList) {
		super(context, R.layout.player_row);
		this.context = context;
		this.playersList = playersList;
	}
 

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView =  inflater.inflate(R.layout.player_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.nameLabel);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.picView);
		textView.setText(playersList.get(position).getFirstName() + " " + playersList.get(position).GetLastName());
		
		if(position > 0 )
			imageView.setImageResource(R.drawable.roni ); 
		// Change icon based on name

		return rowView;
	}

}
