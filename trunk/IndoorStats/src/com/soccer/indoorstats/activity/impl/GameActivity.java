package com.soccer.indoorstats.activity.impl;

/*This code has been created for a demo by Shawn for ShawnBe.com 
 * please do not redistribute this without my permission, you
 * can email me at shawn@shawnbe.com. If these tutorials has helped
 * you please consider donating to me via paypal on my website 
 * ShawnBe.com
 */

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.soccer.dialog.MultiSelectListDialog;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.utils.StopWatch;

public class GameActivity extends Activity implements OnClickListener {
	private static final int MSG_START_TIMER = 0;
	private static final int MSG_STOP_TIMER = 1;
	private static final int MSG_UPDATE_TIMER = 2;
	private static final int MSG_RESET_TIMER = 3;
	private static final String INIT_TIME = "0:0";

	private static StopWatch timer = new StopWatch();
	private static final int REFRESH_RATE = 1000;

	private MenuExtender slidingMenu;

	private static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_START_TIMER:
				timer.start(tvTextView.getText().equals(INIT_TIME));
				mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
				break;
			case MSG_UPDATE_TIMER:
				int mins = (int) (timer.getElapsedTimeSecs() / 60);
				int secs = (int) (timer.getElapsedTimeSecs() % 60);
				tvTextView.setText(mins + ":" + secs);
				mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER, REFRESH_RATE);
				break;
			case MSG_STOP_TIMER:
				mHandler.removeMessages(MSG_UPDATE_TIMER);
				timer.stop();
				break;
			case MSG_RESET_TIMER:
				mHandler.removeMessages(MSG_UPDATE_TIMER);
				timer.stop();
				btnStart.setText("Start");
				tvTextView.setText(INIT_TIME);
				break;

			default:
				break;
			}
		}
	};

	private static TextView tvTextView;
	private static Button btnStart;
	Button btnReset;
	// LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
	ArrayList<String> listItems = new ArrayList<String>();

	// DEFINING STRING ADAPTER WHICH WILL HANDLE DATA OF LISTVIEW
	ArrayAdapter<String> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_main);
		if (slidingMenu == null) {
			slidingMenu = new MenuExtender(this, "");
			slidingMenu.initSlideMenu();
		}

		tvTextView = (TextView) findViewById(R.id.textViewTimer);

		btnStart = (Button) findViewById(R.id.buttonStart);
		btnReset = (Button) findViewById(R.id.buttonReset);
		btnStart.setOnClickListener(this);
		btnReset.setOnClickListener(this);

		ListView lstView = (ListView) findViewById(R.id.listView1);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listItems);
		lstView.setAdapter(adapter);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void onClick(View v) {
		if (btnStart == v) {
			if (btnStart.getText().equals("Start")) {
				mHandler.sendEmptyMessage(MSG_START_TIMER);
				btnStart.setText("Stop");
			} else {
				mHandler.sendEmptyMessage(MSG_STOP_TIMER);
				btnStart.setText("Start");
			}
		} else if (btnReset == v) {
			mHandler.sendEmptyMessage(MSG_RESET_TIMER);
		}

	}

	public static int stat_index = 0;
	final CharSequence[] tempList= { "Guy","Udi","Koko" };
	public void onAddItem(View v) {
		MultiSelectListDialog dlg = new MultiSelectListDialog(this, 0, 0, tempList) {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onOkClicked(String input) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		dlg.create();
		dlg.show();
		listItems.add("item " + stat_index++);
		adapter.notifyDataSetChanged();
	}

	public void goalClick(View view) {
		;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return slidingMenu.handleTouchEvent(event);
	}
}