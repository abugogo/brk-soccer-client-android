package com.soccer.indoorstats.services;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.db.local.GameDbAdapter;
import com.soccer.db.remote.R_DB_CONSTS;
import com.soccer.entities.EntityManager;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.indoorstats.services.handlers.RequestHandler;
import com.soccer.indoorstats.services.i.ILoginService;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.rest.LoopjRestClient;

public class LoginService extends BaseService implements ILoginService {
	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public class LocalBinder extends Binder {
		public LoginService getService() {
			return LoginService.this;
		}
	}

	@Override
	public void login(String user, String password, final String account,
			final RequestHandler<JSONArray> handler) {
		String sUrl = sharedPrefs.getPreference("server_port", "NULL");
		if (sUrl.equals("NULL")) {
			sUrl = R_DB_CONSTS.SERVER_DEFAULT;
		}
		RequestParams params = new RequestParams();
		params.put("u", user);
		params.put("p", password);

		LoopjRestClient.get(this, sUrl.concat("/SoccerServer/login"), params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject res) {
						onLoginSuccess(res, account, handler);
					}

					@Override
					public void onFailure(Throwable tr, String res) {
						String err = tr.getMessage();
						if (err == null) {
							if (tr.getCause() != null)
								err = tr.getCause().getMessage();
							else
								err = "Unknown failure";
						}
						handler.onFailure(err, 0);
					}

					@Override
					public void onFinish() {
						Logger.i("login finished");
					}
				});
	}

	private void onLoginSuccess(JSONObject res, String account,
			RequestHandler<JSONArray> handler) {
		try {
			JSONArray acc_arr = res.getJSONArray("accounts");
			int s = acc_arr.length();
			// check for multiple accounts
			if (s == 1) {
				// only one account is supported which is the common case
				LoginToAccount(acc_arr.getString(0), account, handler);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void LoginToAccount(CharSequence accountName, String account,
			final RequestHandler<JSONArray> handler) {

		sharedPrefs.setPreference("account_name", (String) accountName);
		String sUrl = sharedPrefs.getPreference("server_port", "NULL");
		if (sUrl.equals("NULL")) {
			sUrl = R_DB_CONSTS.SERVER_DEFAULT;
		}

		try {
			LoopjRestClient.get(this, sUrl.concat("/SoccerServer/rest/")
					.concat((String) accountName).concat("/players"), null,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONArray res) {
							onAccountLoginSuccess(res);
							handler.onSuccess(res);
						}

						@Override
						public void onFailure(Throwable tr, String res) {
							handler.onFailure(res, 0);
						}

						@Override
						public void onFinish() {
							Logger.i("account login finished");
						}
					});

		} catch (Exception e) {
			Logger.e("account login failed", e);
			handler.onFailure(e.getMessage(), 0);
		}
	}

	private void onAccountLoginSuccess(JSONArray res) {
		// refresh players list
		ArrayList<DAOPlayer> pArr = EntityManager.readPlayers(res.toString());
		SQLiteDatabase db = openDB();
		LoadPlayers(pArr, db);
		// remove app state
		GameDbAdapter sdba = new GameDbAdapter(db);
		sdba.deleteAllStates();
		closeDB();
	}

	private void LoadPlayers(ArrayList<DAOPlayer> pArr, SQLiteDatabase db) {
		try {
			if (pArr != null) {
				PlayersDbAdapter pda = new PlayersDbAdapter(db);
				pda.deletePlayers();
				Iterator<DAOPlayer> itr = pArr.iterator();
				while (itr.hasNext()) {
					DAOPlayer p = itr.next();
					pda.createPlayer(p);
				}
			}
		} catch (Exception e) {
			Logger.e("login activity failed due players creation failure", e);
		}
	}
}
