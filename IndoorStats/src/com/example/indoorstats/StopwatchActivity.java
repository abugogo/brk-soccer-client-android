package com.example.indoorstats;

/*This code has been created for a demo by Shawn for ShawnBe.com 
 * please do not redistribute this without my permission, you
 * can email me at shawn@shawnbe.com. If these tutorials has helped
 * you please consider donating to me via paypal on my website 
 * ShawnBe.com
 */

import android.app.Activity;
import android.content.res.Configuration;
import android.widget.TextView;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
//import com.google.ads.*;

import android.widget.LinearLayout;


public class StopwatchActivity extends Activity{
    /** Called when the activity is first created. */
	
	//AdView adView;
	private TextView tempTextView; //Temporary TextView 
	private Button tempBtn; //Temporary Button
	private Handler mHandler = new Handler();
	private long startTime;
	private long stopTime;
	private long elapsedTime;
	private final int REFRESH_RATE = 100;
	private String hours,minutes,seconds,milliseconds;
	private long secs,mins,hrs,msecs;
	private boolean stopped = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.swmain);
        
        
        
        checkScreenDensity();
        
    	/*-------Setting the TextView Fonts-----------*/
    	  
    	Typeface font = Typeface.createFromAsset(getAssets(), "altehaasgroteskbold.ttf"); 
    	tempTextView = (TextView) findViewById(R.id.timer); 
    	tempTextView.setTypeface(font); 
//    	tempTextView = (TextView) findViewById(R.id.timerMs); 
//   	tempTextView.setTypeface(font); 
    	font = Typeface.createFromAsset(getAssets(), "coolvetica.ttf"); 
    	tempTextView = (TextView) findViewById(R.id.backgroundText);
    	tempTextView.setTypeface(font);
    	Button tempBtn = (Button)findViewById(R.id.startButton);
    	tempBtn.setTypeface(font);
    	tempBtn = (Button)findViewById(R.id.resetButton);
    	tempBtn.setTypeface(font);
    	tempBtn = (Button)findViewById(R.id.stopButton);
    	tempBtn.setTypeface(font);
    	
    }
    
 
    
    @Override
    public void onDestroy() {
      super.onDestroy();
    }
    
    private void checkScreenDensity(){
    	tempTextView = (TextView)findViewById(R.id.backgroundText);
    	switch (getResources().getDisplayMetrics().densityDpi) {
    	case DisplayMetrics.DENSITY_LOW:
    		tempTextView.setVisibility(View.GONE);
    	    break;
    	case DisplayMetrics.DENSITY_MEDIUM:
    		tempTextView.setVisibility(View.GONE);
    	    break;
    	case DisplayMetrics.DENSITY_HIGH:
    		tempTextView.setVisibility(View.VISIBLE);
    	    break;
    	}
    }
    
    public void startClick (View view){
    	showStopButton();
    	if(stopped){
    		startTime = System.currentTimeMillis() - elapsedTime; 
    	}
    	else{
    		startTime = System.currentTimeMillis();
    	}
    	mHandler.removeCallbacks(startTimer);
        mHandler.postDelayed(startTimer, 0);
    }
    
    public void stopClick (View view){
    	hideStopButton();
    	mHandler.removeCallbacks(startTimer);
    	stopped = true;
    }
    
    public void resetClick (View view){
    	stopped = false;
    	((TextView)findViewById(R.id.timer)).setText("00:00:00");  	
    }
    
    private Runnable startTimer = new Runnable() {
	 	   public void run() {
	 		   elapsedTime = System.currentTimeMillis() - startTime;
	 		   updateTimer(elapsedTime);
	 	       mHandler.postDelayed(this,REFRESH_RATE);
	 	   }
	 	};
	 	
	private void updateTimer (float time){
		secs = (long)(time/1000);
		mins = (long)((time/1000)/60);
		hrs = (long)(((time/1000)/60)/60);
		
		/* Convert the seconds to String 
		 * and format to ensure it has
		 * a leading zero when required
		 */
		secs = secs % 60;
		seconds=String.valueOf(secs);
    	if(secs == 0){
    		seconds = "00";
    	}
    	if(secs <10 && secs > 0){
    		seconds = "0"+seconds;
    	}
    	
		/* Convert the minutes to String and format the String */
    	
    	mins = mins % 60;
		minutes=String.valueOf(mins);
    	if(mins == 0){
    		minutes = "00";
    	}
    	if(mins <10 && mins > 0){
    		minutes = "0"+minutes;
    	}
		
    	/* Convert the hours to String and format the String */
    	
    	hours=String.valueOf(hrs);
    	if(hrs == 0){
    		hours = "00";
    	}
    	if(hrs <10 && hrs > 0){
    		hours = "0"+hours;
    	}
    	
    	/* Although we are not using milliseconds on the timer in this example
    	 * I included the code in the event that you wanted to include it on your own
    	 */
    	milliseconds = String.valueOf((long)time);
    	if(milliseconds.length()==2){
    		milliseconds = "0"+milliseconds;
    	}
      	if(milliseconds.length()<=1){
    		milliseconds = "00";
    	}
		milliseconds = milliseconds.substring(milliseconds.length()-3, milliseconds.length()-2);
    	
		/* Setting the timer text to the elapsed time */
		((TextView)findViewById(R.id.timer)).setText(hours + ":" + minutes + ":" + seconds);
	}
    
    public void goalClick (View view){
    	;
    }
    
    private void showStopButton(){
    	((Button)findViewById(R.id.startButton)).setVisibility(View.GONE);
    	((Button)findViewById(R.id.resetButton)).setVisibility(View.GONE);
    	((Button)findViewById(R.id.stopButton)).setVisibility(View.VISIBLE);	
    }
    
    private void hideStopButton(){
    	((Button)findViewById(R.id.startButton)).setVisibility(View.VISIBLE);
    	((Button)findViewById(R.id.resetButton)).setVisibility(View.VISIBLE);
    	((Button)findViewById(R.id.stopButton)).setVisibility(View.GONE);	
    }
    
    
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			TextView timer = (TextView) findViewById(R.id.timer);
			timer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 90);
		} 
		else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			TextView timer = (TextView) findViewById(R.id.timer);
			timer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 70);
		}
	}

}