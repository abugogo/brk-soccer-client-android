package com.soccer.indoorstats.activity.impl;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.markupartist.android.widget.ActionBar.Action;
import com.soccer.db.local.DB_CONSTS;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.imageListUtils.ImageLoader;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.services.PlayerService;
import com.soccer.indoorstats.services.handlers.RequestHandler;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.preferences.Prefs;

public class PlayerUpdateActivity extends Activity {

	private DAOPlayer mPlayer = null;
	private String mPID = null;
	private ImageLoader imageLoader;
	private Prefs mPrefs;
	private ProgressDialog mProgDialog;
	private PlayerService mBoundService;
	private boolean mIsBound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.i("PlayerUpdateActivity onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_update_layout);

		mPrefs = new Prefs(this);
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		String title = mPrefs.getPreference("account_name", getString(R.string.updatePlayer));
		actionBar.setTitle(title);

		final Action SaveAction = new SavePAction();
		actionBar.addAction(SaveAction);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		imageLoader = new ImageLoader(this.getApplicationContext());

		this.mProgDialog = new ProgressDialog(this);
	}

	private void populateFields() {
		Logger.i("PlayerUpdateActivity populateFields");
		if (mPID != null && !mPID.equals("")) {
			if (mPlayer != null && mPlayer.getId() != null
					&& mPlayer.getId() != "") {
				((TextView) findViewById(R.id.pfname)).setText(mPlayer
						.getFname());
				((TextView) findViewById(R.id.plname)).setText(mPlayer
						.getLname());
				((TextView) findViewById(R.id.email)).setText(mPlayer
						.getEmail());
				((TextView) findViewById(R.id.ptel1))
						.setText(mPlayer.getTel1());
				Date bDay = mPlayer.getBday();
				if (bDay != null) {
					Calendar c = Calendar.getInstance();
					c.setTime(bDay);
					((DatePicker) findViewById(R.id.bday)).init(
							c.get(Calendar.YEAR), c.get(Calendar.MONTH),
							c.get(Calendar.DAY_OF_MONTH), null);
				}
				ImageView thumb_image = (ImageView) findViewById(R.id.pimage); // thumb
																				// image
				imageLoader.DisplayImage(mPlayer.getP_img(), thumb_image);
			} else {
				Prefs sharedPrefs = new Prefs(this);
				sharedPrefs.setPreference("LoggedIn", -1);
				showDialog(0,
						DlgUtils.prepareDlgBundle("Failed to load player"));

			}

		}

	}

	private class SavePAction extends AbstractAction {

		public SavePAction() {
			super(R.drawable.save);
		}

		@Override
		public void performAction(View view) {
			updatePlayer();
		}

	}

	private void updatePlayer() {
		if (mPlayer != null) {
			String newEmail = ((TextView) findViewById(R.id.email)).getText()
					.toString();
			String newPhone = ((TextView) findViewById(R.id.ptel1)).getText()
					.toString();
			DatePicker datePicker = ((DatePicker) findViewById(R.id.bday));
			Date d = new Date(datePicker.getYear() - 1900,
					datePicker.getMonth(), datePicker.getDayOfMonth());

			boolean dirty = !newEmail.equals(mPlayer.getEmail())
					|| !newPhone.equals(mPlayer.getTel1())
					|| mPlayer.getBday() == null
					|| !d.equals(mPlayer.getBday());

			if (dirty) {
				mPlayer.setBday(d);
				mPlayer.setEmail(newEmail);
				mPlayer.setTel1(newPhone);
				if (mIsBound) {
					this.mProgDialog.setMessage("Updating Player Data");
					this.mProgDialog.show();
					mBoundService.updatePlayer(mPlayer, new RequestHandler() {

						@Override
						public void onSuccess() {
							Logger.i("PlayerUpdateActivity success");
							mProgDialog.dismiss();
							onUpdateSuccess();
						}

						@Override
						public void onFailure(String reason, int errorCode) {
							Logger.i("PlayerUpdateActivity failure");
							mProgDialog.dismiss();
							onUpdateFailure(errorCode, reason);
						}
					});
				}
			} else {
				Toast toast = Toast.makeText(this.getApplicationContext(),
						"No changes detected", Toast.LENGTH_SHORT);
				toast.show();
			}

		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		Logger.i("PlayerUpdateActivity onPause");
		doUnbindService();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Logger.i("PlayerUpdateActivity onResume");
		mPID = mPrefs.getPreference(DB_CONSTS.KEY_ID, mPID);
		doBindService();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void onUpdateSuccess() {
		Toast toast = Toast.makeText(this.getApplicationContext(),
				"Updated successfully", Toast.LENGTH_SHORT);
		toast.show();

		this.finish();
	}

	private void onUpdateFailure(int responseCode, String result) {
		showDialog(
				0,
				DlgUtils.prepareDlgBundle("Failed updating player info: "
						+ result));
		if(mIsBound)
			mPlayer = mBoundService.getPlayer(mPID);
		populateFields();
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		return DlgUtils.createAlertMessage(this, args);
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mBoundService = (PlayerService) ((PlayerService.LocalBinder) service)
					.getService();
			mPlayer = mBoundService.getPlayer(mPID);
			populateFields();
		}

		public void onServiceDisconnected(ComponentName className) {
			mBoundService = null;
		}
	};

	private void doBindService() {
		bindService(new Intent(PlayerUpdateActivity.this, PlayerService.class),
				mConnection, Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	private void doUnbindService() {
		if (mIsBound) {
			unbindService(mConnection);
			mIsBound = false;
		}
	}

}
