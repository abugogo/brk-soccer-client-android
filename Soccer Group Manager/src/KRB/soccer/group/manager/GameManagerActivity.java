package KRB.soccer.group.manager;

import KRB.soccer.group.manager.R;
import KRB.soccer.group.manager.R.id;
import KRB.soccer.group.manager.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GameManagerActivity extends Activity implements OnClickListener{

	private Button chooseRedButton;
	private Button chooseYellowButton;
	private Button startMatchButton;

	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.new_game);
	        
	        chooseRedButton = (Button) findViewById(R.id.ChooseRedTeamButton);
	        chooseRedButton.setOnClickListener((OnClickListener) this);

	        chooseYellowButton = (Button) findViewById(R.id.ChooseYellowTeamButton);
	        chooseYellowButton.setOnClickListener((OnClickListener) this);

	        startMatchButton = (Button) findViewById(R.id.startMatchButton);
	        startMatchButton.setOnClickListener((OnClickListener) this);
}
	    
	    public void onClick(View v) {
	    	Intent newIntent = null;

	    	switch (v.getId()){
	    	case R.id.ChooseRedTeamButton:
	    		newIntent = new Intent(v.getContext(), ListOfPlayersActivity.class);
	    		startActivityForResult(newIntent, 0);	    		
	    		break;
	    	
	    	case R.id.ChooseYellowTeamButton:
	    		newIntent = new Intent(v.getContext(), ListOfPlayersActivity.class);
	    		startActivityForResult(newIntent, 0);	    		
	    		break;
	    		
	    	case R.id.startMatchButton:
	    		newIntent = new Intent(v.getContext(), GameInProgressActivity.class);
	    		startActivityForResult(newIntent, 0);	    		
	    		break;
	    		
	    		default:
	    			return;
	    	}
	    	
	    	
	    }
	    
}
