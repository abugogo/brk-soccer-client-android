package com.soccer.indoorstats.activity.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.coboltforge.slidemenu.SlideMenu;
import com.coboltforge.slidemenu.SlideMenuInterface.OnSlideMenuItemClickListener;
import com.soccer.indoorstats.R;

public class MenuExtender implements OnSlideMenuItemClickListener {

	private SlideMenu slidemenu;
	private String mPID = null;
	private Activity mAct = null;
	private static LayoutInflater inflater=null;
	private float initialX = 0;
	private float deltaX = 0;

	public MenuExtender(Activity act, String pId) {
		mAct = act;
		mPID = pId;

		inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	public void initSlideMenu() {

		//Intent i = getIntent();
		//mPID = (String) i.getSerializableExtra("player_id");

		//slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
		View vi = inflater.inflate(R.layout.mainmenu, null);

		slidemenu = (SlideMenu)vi.findViewById(R.id.slideMenu); 
		slidemenu.init(mAct, R.menu.slide, this, 333);

		// set optional header image
		slidemenu.setHeaderImage(R.drawable.ic_launcher);
	}

	public void showMenu() {
		if (!slidemenu.isClickable())
			slidemenu.show();
	}

	public void onSlideMenuItemClick(int itemId) {
		Intent appIntent = null;
		switch (itemId) {
		case R.id.item_player:
			Toast.makeText(mAct, "Item one selected", Toast.LENGTH_SHORT)
					.show();
			appIntent = new Intent(mAct, PlayerActivity.class);
			break;
		case R.id.item_group:
			Toast.makeText(mAct, "Item two selected", Toast.LENGTH_SHORT)
					.show();
			appIntent = new Intent(mAct, GroupActivity.class);
			break;
		case R.id.item_game:
			Toast.makeText(mAct, "Item three selected", Toast.LENGTH_SHORT)
					.show();
			appIntent = new Intent(mAct, GameActivity.class);
			break;
		case R.id.item_exit:
			Toast.makeText(mAct, "Item four selected", Toast.LENGTH_SHORT)
					.show();
			mAct.finish();
			break;
		}

		if (appIntent != null) {
			appIntent.putExtra("player_id", mPID);
			mAct.startActivity(appIntent);
		}

	}
	
	public boolean handleTouchEvent(MotionEvent event) {
		// This prevents touchscreen events from flooding the main thread
		synchronized (event) {
			try {
				// Waits 16ms.
				event.wait(16);

				// when user touches the screen
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					deltaX = 0;

					// get initial positions
					initialX = event.getRawX();
					if(initialX < 10) 
						return true;
				}
				else if (event.getAction() == MotionEvent.ACTION_UP) {
					if(initialX < 10) {
						deltaX = event.getRawX() - initialX;
	
						if (deltaX > 10) {
							showMenu();
							return true;
						}
					}
				}
				else if(initialX < 10) {
					return true;
				}
				
			} catch (InterruptedException e) {
			}
		}
		return false;
	}

}