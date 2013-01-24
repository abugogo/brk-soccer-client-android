package com.soccer.indoorstats.activity.impl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.markupartist.android.widget.ActionBar.Action;
import com.soccer.db.local.DB_CONSTS;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.services.LoginService;
import com.soccer.indoorstats.services.handlers.RequestHandler;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.preferences.Prefs;
import com.soccer.preferences.SoccerPrefsActivity;

public class LoginActivity extends Activity {
	Prefs sharedPrefs;
	private ProgressDialog mProgDialog;
	private ActionBar actionBar;
	
	private LoginService mBoundService;
	private boolean mIsBound;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		sharedPrefs = new Prefs(this);
		this.mProgDialog = new ProgressDialog(this);
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Soccer Time");

		final Action settingsAction = new SettingsAction();
		actionBar.setHomeAction(settingsAction);
	}
	
	private class SettingsAction extends AbstractAction {
		public SettingsAction() {
			super(R.drawable.settings);
		}

		@Override
		public void performAction(View view) {
			startActivity(new Intent(LoginActivity.this, SoccerPrefsActivity.class));
		}
	}

	@Override
	protected void onResume() {
		doBindService();
		String loggedInId = sharedPrefs.getPreference(DB_CONSTS.KEY_ID, "");
		if (loggedInId != null && !"".equals(loggedInId)) {
			loadApp(loggedInId);
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		doUnbindService();
		super.onPause();
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

	public void loginClick(View view) {
		EditText et = (EditText) findViewById(R.id.editIdNumber);
		final String id = et.getText().toString();
		et = (EditText) findViewById(R.id.editPassword);
		final String password = et.getText().toString();

		if (id != null && !id.equals("")) {
			String loggedInId = sharedPrefs.getPreference(DB_CONSTS.KEY_ID, "");
			if (!loggedInId.equals(id)) {
				perfromLogin(id, password);
			}
		}
	}

	private void perfromLogin(final String id, final String password) {
		if (mIsBound) {
			this.mProgDialog.setMessage("Logging in...");
			this.mProgDialog.show();
			mBoundService.login(id, password, "", new RequestHandler<String>() {
				@Override
				public void onSuccess(String content) {
					sharedPrefs.setPreference(DB_CONSTS.KEY_ID, id);
					sharedPrefs.setPreference(DB_CONSTS.KEY_PWRD, password);
					loadApp(id);
					mProgDialog.dismiss();
				}

				@Override
				public void onFailure(String reason, int errorCode) {
					mProgDialog.dismiss();
					sharedPrefs.setPreference(DB_CONSTS.KEY_ID, "");
					String error = "Failed login";
					if (reason != null)
						error = reason;
					DlgUtils.showAlertMessage(LoginActivity.this, "Login Error", error);
				}
			});
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("State", 1);
	}

	private void loadApp(String id) {
		if (id != "") {
			Intent appIntent = new Intent(this, HomeActivity.class);
			startActivity(appIntent);
		} else {
			showDialog(0, DlgUtils.prepareDlgBundle("Failed login"));
		}
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mBoundService = (LoginService) ((LoginService.LocalBinder) service)
					.getService();
		}

		public void onServiceDisconnected(ComponentName className) {
			mBoundService = null;
		}
	};

	private void doBindService() {
		bindService(new Intent(LoginActivity.this, LoginService.class),
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
