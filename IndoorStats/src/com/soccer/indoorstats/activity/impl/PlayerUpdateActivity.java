package com.soccer.indoorstats.activity.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.markupartist.android.widget.ActionBar.Action;
import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.imageListUtils.ImageLoader;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.preferences.Prefs;
import com.soccer.rest.LoopjRestClient;

public class PlayerUpdateActivity extends Activity {

	private PlayersDbAdapter mDbHelper;
	private DAOPlayer mPlayer = null;
	private String mPID = null;
	private ImageLoader imageLoader;
	private Prefs mPrefs;
	private ProgressDialog mProgDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.i("PlayerUpdateActivity onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_update_layout);

		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle(R.string.updatePlayer);

		final Action SaveAction = new SavePAction();
		actionBar.addAction(SaveAction);

		Intent i = getIntent();
		mPID = (String) i.getSerializableExtra("player_id");

		mPrefs = new Prefs(this);
		mPID = (String) mPrefs.getPreference(PlayersDbAdapter.KEY_ID, mPID);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

		imageLoader = new ImageLoader(this.getApplicationContext());

		mDbHelper = new PlayersDbAdapter(this);
		mDbHelper.open();
		LoadPlayerFromDB(mPID);

		this.mProgDialog = new ProgressDialog(this);
	}

	private void populateFields() {
		Logger.i("PlayerUpdateActivity populateFields");
		if (mPID != null && !mPID.equals("")) {
			LoadPlayerFromDB(mPID);

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
					|| mPlayer.getBday() == null || !d.equals(mPlayer.getBday());
			
			if (dirty) {
				mPlayer.setBday(d);
				mPlayer.setEmail(newEmail);
				mPlayer.setTel1(newPhone);
				String sUrl = mPrefs.getPreference("server_port", "NULL");

				RequestParams params;
				Gson gson = new Gson();
				params = new RequestParams();
				params.put("JSON", gson.toJson(mPlayer));

				mProgDialog.setMessage("Updating...");
				mProgDialog.show();

				LoopjRestClient.post(
						sUrl.concat("/SoccerServer/rest/")
								.concat(mPrefs
										.getPreference("account_name", ""))
								.concat("/players/").concat(mPlayer.getId()),
						params, new JsonHttpResponseHandler() {

							@Override
							public void onSuccess(JSONObject res) {
								onUpdateSuccess(res);
							}

							@Override
							public void onFailure(Throwable tr, String res) {
								onUpdateFailure(0, tr.getMessage());
							}

							@Override
							public void onFinish() {
								if (mProgDialog.isShowing())
									mProgDialog.dismiss();
								Logger.i("Update finished");
							}
						});
			} else {
				Toast toast = Toast.makeText(this.getApplicationContext(),
						"No changes detected", Toast.LENGTH_SHORT);
				toast.show();
			}

		}

	}

	private void LoadPlayerFromDB(String id) {
		DAOPlayer p = new DAOPlayer();
		Cursor cP = mDbHelper.fetchPlayer(Long.parseLong(id));
		startManagingCursor(cP);
		if (cP.getCount() > 0) {
			p.setFname(cP.getString(cP
					.getColumnIndexOrThrow(PlayersDbAdapter.KEY_FNAME)));
			p.setLname(cP.getString(cP
					.getColumnIndexOrThrow(PlayersDbAdapter.KEY_LNAME)));
			p.setEmail(cP.getString(cP
					.getColumnIndexOrThrow(PlayersDbAdapter.KEY_EMAIL)));
			p.setP_img(cP.getString(cP
					.getColumnIndexOrThrow(PlayersDbAdapter.KEY_IMG)));
			p.setDescription(cP.getString(cP
					.getColumnIndexOrThrow(PlayersDbAdapter.KEY_DESC)));
			Date d = null;
			try {
				String sBDate = cP.getString(cP
						.getColumnIndexOrThrow(PlayersDbAdapter.KEY_BDAY));
				if (sBDate != null && !sBDate.equals(""))
					d = (Date) new SimpleDateFormat(
							"EEE MMM d HH:mm:ss zzz yyyy", Locale.ENGLISH)
							.parse(sBDate);

			} catch (IllegalArgumentException e) {
				Logger.e(
						"player update activity loadplayersfromdb failed due to illegal state",
						e);
			} catch (ParseException e) {
				Logger.e(
						"player update activity loadplayersfromdb failed due to parse exception",
						e);
			}
			if (d != null)
				p.setBday(d);
			p.setId(cP.getString(cP
					.getColumnIndexOrThrow(PlayersDbAdapter.KEY_ID)));
			p.setTel1(cP.getString(cP
					.getColumnIndexOrThrow(PlayersDbAdapter.KEY_TEL1)));
		}

		mPlayer = p;
	}

	@Override
	protected void onPause() {
		Logger.i("PlayerUpdateActivity onPause");
		super.onPause();
		saveState();
	}

	@Override
	protected void onResume() {
		Logger.i("PlayerUpdateActivity onResume");
		super.onResume();
		mPID = mPrefs.getPreference(PlayersDbAdapter.KEY_ID, mPID);
		populateFields();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Logger.i("PlayerUpdateActivity onSaveInstanceState");
		super.onSaveInstanceState(outState);
		saveState();
	}

	private void saveState() {
		mPrefs.setPreference(PlayersDbAdapter.KEY_ID, mPID);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
	}

	public void onUpdateSuccess(JSONObject result) {
		mDbHelper.updatePlayer(mPlayer);
		Toast toast = Toast.makeText(this.getApplicationContext(),
				"Updated successfully", Toast.LENGTH_SHORT);
		toast.show();

		this.finish();
	}

	public void onUpdateFailure(int responseCode, String result) {
		showDialog(
				0,
				DlgUtils.prepareDlgBundle("Failed updating player info: "
						+ result));
		populateFields();
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		return DlgUtils.createAlertMessage(this, args);
	}

}
