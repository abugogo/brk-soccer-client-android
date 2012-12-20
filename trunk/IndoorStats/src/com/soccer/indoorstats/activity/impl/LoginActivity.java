package com.soccer.indoorstats.activity.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.db.local.StateDbAdapter;
import com.soccer.db.remote.R_DB_CONSTS;
import com.soccer.dialog.ListDialog;
import com.soccer.entities.EntityManager;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.indoorstats.R;
import com.soccer.indoorstats.activity.i.StrListDialogAct;
import com.soccer.indoorstats.utils.DlgUtils;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.preferences.Prefs;
import com.soccer.preferences.SoccerPrefsActivity;
import com.soccer.rest.LoopjRestClient;

public class LoginActivity extends Activity implements StrListDialogAct {
	private PlayersDbAdapter mDbHelper;
	Prefs sharedPrefs;
	private ProgressDialog mProgDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mDbHelper = new PlayersDbAdapter(this);
		mDbHelper.open();
		sharedPrefs = new Prefs(this);
		this.mProgDialog = new ProgressDialog(this);
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
		String id = et.getText().toString();
		et = (EditText) findViewById(R.id.editPassword);
		String password = et.getText().toString();

		if (id != null && !id.equals("")) {
			String loggedInId = sharedPrefs.getPreference(
					PlayersDbAdapter.KEY_ID, "");
			if (!loggedInId.equals(id)) {
				String sUrl = sharedPrefs.getPreference("server_port", "NULL");
				if (sUrl.equals("NULL")) {
					sUrl = R_DB_CONSTS.SERVER_DEFAULT;
				}

				try {
					RequestParams params = new RequestParams();
					params.put("u", id);
					params.put("p", password);
					this.mProgDialog.setMessage("Logging in...");
					this.mProgDialog.show();
					LoopjRestClient.get(this,sUrl.concat("/SoccerServer/login"),
							params, new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(JSONObject res) {
									onLoginSuccess(res);
								}

								@Override
								public void onFailure(Throwable tr, String res) {
									onLoginFailure(0, tr.getMessage());
								}

								@Override
								public void onFinish() {
									if (mProgDialog.isShowing())
										mProgDialog.dismiss();
									Logger.i("login finished");
								}
							});

					// RemoteDBAdapter.loginPlayer(this, sUrl, id, password,
					// "Loggin In");
				} catch (Exception e) {
					Logger.e("login failed", e);
					showDialog(0, DlgUtils.prepareDlgBundle(e.getMessage()));
				}
			} else {
				loadApp(id);
			}
		} else {
			showDialog(0,
					DlgUtils.prepareDlgBundle("Please provide a valid ID"));
		}

		// old code starts
		/*
		 * if (id != null && !id.equals("")) { String loggedInId =
		 * sharedPrefs.getPreference( PlayersDbAdapter.KEY_ID, ""); if
		 * (!loggedInId.equals(id)) { String sUrl =
		 * sharedPrefs.getPreference("server_port", "NULL"); if
		 * (sUrl.equals("NULL")) { sUrl = R_DB_CONSTS.SERVER_DEFAULT; }
		 * 
		 * try { RemoteDBAdapter.getPlayers(this, sUrl, "Downloading data"); }
		 * catch (Exception e) {
		 * Logger.e("login failed due get players failure", e); showDialog(0,
		 * DlgUtils.prepareDlgBundle(e.getMessage())); } } else { loadApp(id); }
		 * } else { showDialog(0,
		 * DlgUtils.prepareDlgBundle("Please provide a valid ID")); }
		 */
		// end old code
	}

	private void LoadPlayers(ArrayList<DAOPlayer> pArr) {
		try {
			if (pArr != null) {
				mDbHelper.deletePlayers();
				Iterator<DAOPlayer> itr = pArr.iterator();
				while (itr.hasNext()) {
					DAOPlayer p = itr.next();
					mDbHelper.createPlayer(p);
				}
			}
		} catch (Exception e) {
			Logger.e("login activity failed due players creation failure", e);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("State", 1);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
	}

	private void loadApp(String id) {
		sharedPrefs.setPreference(PlayersDbAdapter.KEY_ID, id);
		if (id != "") {
			Intent appIntent = new Intent(this, HomeActivity.class);
			startActivity(appIntent);
		} else {
			showDialog(0, DlgUtils.prepareDlgBundle("Failed login"));
		}
	}

	public void onLoginSuccess(JSONObject result) {
		try {
			JSONArray acc_arr = result.getJSONArray("accounts");
			Set<CharSequence> ss = new HashSet<CharSequence>();
			int s = acc_arr.length();
			// check for multiple accounts
			if (s > 1) {
				for (int i = 0; i < s; i++) {
					ss.add(acc_arr.getString(i));
				}
				ListDialog ldlg = new ListDialog(this, "Select Account", ss);
				ldlg.show();
			} else if (s == 1) {
				// only one account - common case
				LoginToAccount(acc_arr.getString(0));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onLoginFailure(int responseCode, String result) {
		showDialog(0, DlgUtils.prepareDlgBundle("Failed login: " + result));
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		return DlgUtils.createAlertMessage(this, args);
	}

	@Override
	public void StringSelected(CharSequence selection) {
		// account selected
		LoginToAccount(selection);
	}

	private void LoginToAccount(CharSequence accountName) {

		sharedPrefs.setPreference("account_name", (String) accountName);
		String sUrl = sharedPrefs.getPreference("server_port", "NULL");
		if (sUrl.equals("NULL")) {
			sUrl = R_DB_CONSTS.SERVER_DEFAULT;
		}

		try {
			this.mProgDialog.setMessage("Downloading account info...");
			this.mProgDialog.show();
			LoopjRestClient.get(this,
					sUrl.concat("/SoccerServer/rest/")
							.concat((String) accountName).concat("/players"),
					null, new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONArray res) {
							onAccountLoginSuccess(res);
						}

						@Override
						public void onFailure(Throwable tr, String res) {
							onLoginFailure(0, tr.getMessage());
						}

						@Override
						public void onFinish() {
							if (mProgDialog.isShowing())
								mProgDialog.dismiss();
							Logger.i("account login finished");
						}
					});

			// RemoteDBAdapter.loginPlayer(this, sUrl, id, password,
			// "Loggin In");
		} catch (Exception e) {
			Logger.e("account login failed", e);
			showDialog(0, DlgUtils.prepareDlgBundle(e.getMessage()));
		}

		/*
		 * try { ArrayList<IDAOPlayer> pArr = EntityManager.readPlayers(result);
		 * LoadPlayers(pArr); } catch (SoccerException e) {
		 * Logger.e("login activity onSuccess failed", e); }
		 * 
		 * EditText et = (EditText) findViewById(R.id.editIdNumber); String id =
		 * et.getText().toString(); StateDbAdapter sdba = new
		 * StateDbAdapter(this); sdba.open(); sdba.deleteAllStates();
		 * sdba.close(); loadApp(id);
		 */
	}

	protected void onAccountLoginSuccess(JSONArray res) {
		/*Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		Type collectionType = new TypeToken<ArrayList<DAOPlayer>>() {
		}.getType();
		ArrayList<IDAOPlayer> pArr = gson.fromJson(res.toString(),
				collectionType);*/
		ArrayList<DAOPlayer> pArr = EntityManager.readPlayers(res.toString());
		
		LoadPlayers(pArr);

		EditText et = (EditText) findViewById(R.id.editIdNumber);
		String id = et.getText().toString();
		StateDbAdapter sdba = new StateDbAdapter(this);
		sdba.open();
		sdba.deleteAllStates();
		sdba.close();
		loadApp(id);
	}

}
