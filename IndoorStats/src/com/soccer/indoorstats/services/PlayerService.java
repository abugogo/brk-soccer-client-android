package com.soccer.indoorstats.services;

import java.util.LinkedList;

import org.json.JSONObject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.db.remote.R_DB_CONSTS;
import com.soccer.entities.EntityManager;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.indoorstats.services.handlers.RequestHandler;
import com.soccer.indoorstats.services.i.IPlayerService;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.rest.LoopjRestClient;

public class PlayerService extends BaseService implements IPlayerService {

	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public class LocalBinder extends Binder {
		public PlayerService getService() {
			return PlayerService.this;
		}
	}

	@Override
	public DAOPlayer getPlayer(String id) {
		DAOPlayer p = new DAOPlayer();
		SQLiteDatabase db = openDB();
		PlayersDbAdapter pda = new PlayersDbAdapter(db);

		p = pda.fetchPlayer(Long.parseLong(id));

		return p;
	}

	@Override
	public void updatePlayer(final DAOPlayer player, final RequestHandler<JSONObject> handler) {
		String sUrl = sharedPrefs.getPreference("server_port", "NULL");
		if (sUrl.equals("NULL")) {
			sUrl = R_DB_CONSTS.SERVER_DEFAULT;
		}

		RequestParams params = new RequestParams();
		params.put("JSON", EntityManager.writePlayer(player));
		String url = sUrl.concat("/SoccerServer/rest/")
				.concat(sharedPrefs.getPreference("account_name", ""))
				.concat("/players/").concat(player.getId());
		Logger.i("Player service updating player: url- ".concat(url));
		LoopjRestClient.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject res) {
				SQLiteDatabase db = openDB();
				PlayersDbAdapter pda = new PlayersDbAdapter(db);
				pda.updatePlayer(player);
				handler.onSuccess(res);
			}

			@Override
			public void onFailure(Throwable tr, String res) {
				handler.onFailure(tr.getMessage(), 0);
			}

			@Override
			public void onFinish() {
				Logger.i("update player finished");
			}
		});

	}

	@Override
	public LinkedList<DAOPlayer> getAllPlayers() {
		LinkedList<DAOPlayer> arr = new LinkedList<DAOPlayer>();
		SQLiteDatabase db = openDB();
		PlayersDbAdapter pda = new PlayersDbAdapter(db);
		arr = pda.fetchAllPlayersAsList();
		
		return arr;
	}
}
