package KRB.soccer.group.manager;


import com.soccer.preferences.SoccerPrefsActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SoccerGroupManagerActivity extends Activity implements OnClickListener{
	
	private Button letsPlayButton;
	private Button statsButton;
	private Button manageGroupButton;
	private  MyServerConnection serverConnection = null;
	
	static private GroupOfPlayers myGoroup = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) { 
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        serverConnection = new MyServerConnection();
        myGoroup = GetGroupInfo( "demo group");
        PresentGroupInfo();
        
        letsPlayButton = (Button) findViewById(R.id.letsPlayButton);
        letsPlayButton.setOnClickListener((OnClickListener) this);

        statsButton = (Button) findViewById(R.id.groupStats);
        statsButton.setOnClickListener((OnClickListener) this);

        manageGroupButton = (Button) findViewById(R.id.groupManagment);
        manageGroupButton.setOnClickListener((OnClickListener) this);
    }
    
    public void onClick(View v) {
    	
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
	 	alertDialog.setMessage("Not implemented yet!");
    	
    	switch (v.getId()){
    	case R.id.letsPlayButton:
//    		LayoutInflater inflater = this.getLayoutInflater();
//    		View newGame = inflater.inflate(R.layout.new_game,null);
//    		LinearLayout mainLayout = (LinearLayout)findViewById(R.layout.main);
//    		mainLayout.addView(newGame);

    	    //Add view using Java Code        
//    	    newGameView = new NewGameView(this.getApplicationContext()) ; 
//    	    LayoutParams gameViewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
//    	    newGameView.setLayoutParams(gameViewLayoutParams);    
//    	    
    	    
    		
            Intent letsPlayIntent = new Intent(v.getContext(), GameManagerActivity.class);
            startActivityForResult(letsPlayIntent, 0);
    		break;
    	
    		
    	case R.id.groupManagment:
    		alertDialog.setMessage(serverConnection._result);
    		alertDialog.show();
    		break;
    		
    	case R.id.groupStats:
   		 	alertDialog.show();
   		 	break;

    	default:
    			return;
    	}
    	
    	
    }
    
    public void PresentGroupInfo(){
    	TextView txtGroupName = (TextView)findViewById(R.id.GroupName);
    	txtGroupName.setText(myGoroup.GetGroupName());
    	
       	TextView txtAdminName = (TextView)findViewById(R.id.Admin);
    	txtAdminName.setText(myGoroup.GetGroupAdmin());
    	
    }
    
    private GroupOfPlayers GetGroupInfo( String name ){
        serverConnection.execute("getGroup");
        GroupOfPlayers myGroup = serverConnection.GetGroupOfPlayers( name );
        return myGroup;
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 0, 0, "Preferences");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			startActivity(new Intent(this, SoccerPrefsActivity.class));
			return true;
		}
		return false;
	}

}