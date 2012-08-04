package com.soccer.preferences;

import java.util.List;

/*import com.soccer.async.AsyncGetPlayerListener;
import com.soccer.connector.RemoteDBAdapter;
import com.soccer.connector.callers.Players;*/
import com.example.indoorstats.R;
import com.soccer.connector.RemoteDBAdapter;
import com.soccer.dal.entities.api.IDAOPlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class ShowSettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_settings);

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		String sUrl = sharedPrefs.getString("server_port", "NULL");
		if (sUrl.equals("NULL")) {
			showAlertMessage("Please set server and port");
		}

		try {
			String option = sharedPrefs.getString("rest_call_id", "1");
			RemoteDBAdapter rda = new RemoteDBAdapter();
			
			switch ((int)Integer.parseInt(option)){
			case 1:
				IDAOPlayer p = rda.getPlayer(sUrl, "3");
				showPlayerInfoOnScreen(p);
				break;
			case 2:
				//List<IDAOPlayer> lst = RemoteDBAdapter.getAllPlayers(sUrl);
				//showPlayersInfoOnScreen(lst);
				break;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showAlertMessage(e.getMessage());
		}
	}

	public void showPlayerInfoOnScreen(IDAOPlayer p) {
		StringBuilder builder = new StringBuilder();

		builder.append("\n" + p.getFname());
		builder.append("\n" + p.getLname());

		TextView settingsTextView = (TextView) findViewById(R.id.settings_text_view);
		settingsTextView.setText(builder.toString());
	}

	public void showPlayersInfoOnScreen(List<IDAOPlayer> pList) {
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<pList.size(); i++) {
			builder.append("\n" + pList.get(i).getFname() + " " + pList.get(i).getLname());
		}
		
		TextView settingsTextView = (TextView) findViewById(R.id.settings_text_view);
		settingsTextView.setText(builder.toString());
	}

	private void showAlertMessage(String msg) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage(msg);		
	}

}
