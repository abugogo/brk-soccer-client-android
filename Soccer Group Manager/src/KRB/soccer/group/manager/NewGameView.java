package KRB.soccer.group.manager;

import KRB.soccer.group.manager.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class NewGameView extends LinearLayout implements OnClickListener{
	
	private View mView;
	private Button chooseRedButton;
	private Button chooseYellowButton;
	private Button startMatchButton;

	public NewGameView(Context context) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.new_game, this);

        chooseRedButton = (Button) findViewById(R.id.ChooseRedTeamButton);
        chooseRedButton.setOnClickListener((OnClickListener) this);

        chooseYellowButton = (Button) findViewById(R.id.ChooseYellowTeamButton);
        chooseYellowButton.setOnClickListener((OnClickListener) this);

        startMatchButton = (Button) findViewById(R.id.startMatchButton);
        startMatchButton.setOnClickListener((OnClickListener) this);

	}

	        
	    public void onClick(View v) {
	    	Intent chooseTeamIntent = null;

	    	switch (v.getId()){
	    	case R.id.ChooseRedTeamButton:
	    		chooseTeamIntent = new Intent(v.getContext(), ListOfPlayersActivity.class);
//	    		startActivityForResult(chooseTeamIntent, 0);	    		
	    		break;
	    	
	    	case R.id.ChooseYellowTeamButton:
	    		chooseTeamIntent = new Intent(v.getContext(), ListOfPlayersActivity.class);
//	    		startActivityForResult(chooseTeamIntent, 0);	    		
	    		break;
	    		
	    	case R.id.startMatchButton:
	    		 AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
	    		 alertDialog.setMessage("Not implemented yet!");
	    		 alertDialog.show();
	    		break;
	    		
	    		default:
	    			return;
	    	}
	    	
	    	
	    }
	    

}
