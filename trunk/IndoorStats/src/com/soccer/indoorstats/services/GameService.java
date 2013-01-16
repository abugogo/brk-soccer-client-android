package com.soccer.indoorstats.services;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soccer.db.local.GameDbAdapter;
import com.soccer.entities.EntityManager;
import com.soccer.entities.impl.DAOGame;
import com.soccer.indoorstats.services.handlers.RequestHandler;
import com.soccer.indoorstats.services.i.IGameService;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.rest.LoopjRestClient;

public class GameService extends BaseService implements IGameService {

	private final IBinder mBinder = new LocalBinder();
	private String sUrl = "";

	@Override
	public IBinder onBind(Intent arg0) {
		sUrl = sharedPrefs.getPreference("server_port", "NULL");
		return mBinder;
	}

	public class LocalBinder extends Binder {
		public GameService getService() {
			return GameService.this;
		}
	}

	@Override
	public ObjectInputStream getCurGameState() {
		SQLiteDatabase db = openDB();
		GameDbAdapter gda = new GameDbAdapter(db);
		ObjectInputStream objectIn = gda.fetchState();
		closeDB();

		return objectIn;
	}

	@Override
	public void saveGameState(ByteArrayOutputStream bos) {
		SQLiteDatabase db = openDB();
		GameDbAdapter gda = new GameDbAdapter(db);
		gda.insertOrUpdateState(bos);
		closeDB();
	}

	@Override
	public void updateGame(DAOGame game, final RequestHandler handler) {

		try {
			RequestParams params = new RequestParams();
			String g_json = EntityManager.writeGame(game);
			// save game in status updating (remove on success)
			final long growid = storeGame(g_json);
			params.put("JSON", g_json);

			LoopjRestClient.put(this, sUrl.concat("/SoccerServer/rest/")
					.concat(sharedPrefs.getPreference("account_name", ""))
					.concat("/games"), params, new JsonHttpResponseHandler() {

				@Override
				public void onSuccess(JSONObject res) {
					// remove game from "updating" status
					deleteGame(growid);
					handler.onSuccess();
				}

				@Override
				public void onFailure(Throwable tr, String res) {
					updateGame(growid, GameStatus.Failed);
					handler.onFailure(res, 0);
				}

				@Override
				public void onFinish() {
					Logger.i("Create game finished");
				}
			});

		} catch (Exception e) {
			Logger.e("create game failed", e);
		}

	}

	@Override
	public ArrayList<DAOGame> getAllGames(int season) {
		return null;
	}

	@Override
	public ArrayList<DAOGame> getGames(GameStatus gs) {
		ArrayList<DAOGame> retList = new ArrayList<DAOGame>();
		try {
			ArrayList<String> gamesBlobs = getGamesBlobs(gs);
			if (gamesBlobs != null) {
				for (String s : gamesBlobs) {
					retList.add(EntityManager.readGame(s));
				}
			}
		} catch (RuntimeException rte) {
			Logger.e(rte.getMessage(), rte);
		}
		return retList;
	}

	private long storeGame(String g_json) {
		SQLiteDatabase db = openDB();
		GameDbAdapter gda = new GameDbAdapter(db);
		long ret = gda.storeGame(g_json);
		closeDB();
		return ret;
	}

	private long updateGame(long rowid, GameStatus gs) {
		SQLiteDatabase db = openDB();
		GameDbAdapter gda = new GameDbAdapter(db);
		long ret = gda.updateGame(rowid, gs);
		closeDB();
		return ret;
	}

	private int deleteGame(long rowid) {
		SQLiteDatabase db = openDB();
		GameDbAdapter gda = new GameDbAdapter(db);
		int ret = gda.removeGame(rowid);
		closeDB();
		return ret;
	}

	private ArrayList<String> getGamesBlobs(GameStatus gs) {
		SQLiteDatabase db = openDB();
		GameDbAdapter gda = new GameDbAdapter(db);
		ArrayList<String> ret = gda.getGames(gs);
		closeDB();
		return ret;
	}

}
