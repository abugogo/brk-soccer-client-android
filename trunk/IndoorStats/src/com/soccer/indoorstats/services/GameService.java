package com.soccer.indoorstats.services;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;

import org.json.JSONObject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soccer.db.local.GameDbAdapter;
import com.soccer.db.remote.R_DB_CONSTS;
import com.soccer.entities.EntityManager;
import com.soccer.entities.impl.DAOGame;
import com.soccer.indoorstats.services.handlers.RequestHandler;
import com.soccer.indoorstats.services.i.IGameService;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.rest.LoopjRestClient;

public class GameService extends BaseService implements IGameService {

	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent arg0) {
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
		String sUrl = sharedPrefs.getPreference("server_port", "NULL");
		if (sUrl.equals("NULL")) {
			sUrl = R_DB_CONSTS.SERVER_DEFAULT;
		}

		try {
			RequestParams params = new RequestParams();
			params.put("JSON", EntityManager.writeGame(game));

			LoopjRestClient.put(this, sUrl.concat("/SoccerServer/rest/")
					.concat(sharedPrefs.getPreference("account_name", ""))
					.concat("/games"), params, new JsonHttpResponseHandler() {

				@Override
				public void onSuccess(JSONObject res) {
					handler.onSuccess();
				}

				@Override
				public void onFailure(Throwable tr, String res) {
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
}
