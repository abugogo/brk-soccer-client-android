package KRB.soccer.group.manager;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class ListOfPlayersActivity extends ListActivity {

	static private List<Player> players;
    private PlayerArrayAdapter mAdapter;    

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 //       setContentView(R.layout.players_list);
	
		
		GroupOfPlayers group = new DemoSupply().MakeDemoGroup("Demo Group");
		players = group.GetPlayers();
		mAdapter = new PlayerArrayAdapter(this, players);
		setListAdapter(mAdapter);
		
		for(int i = 0; i < players.size(); i++)
			mAdapter.add(players.get(i));
		
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		//get selected items
		String selectedValue = (String) getListAdapter().getItem(position);
		Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();

	}

}
