package com.soccer.indoorstats.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.soccer.entities.EntityManager;
import com.soccer.entities.IDAOLEvent.EventType;
import com.soccer.entities.impl.DAOAggrLEvents;
import com.soccer.indoorstats.services.handlers.RequestHandler;
import com.soccer.indoorstats.services.i.IStatsTableService;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.lib.SoccerException;
import com.soccer.rest.LoopjRestClient;

public class StatsTableService extends BaseService implements
		IStatsTableService {

	private final IBinder mBinder = new LocalBinder();
	private String sUrl = "";

	@Override
	public IBinder onBind(Intent arg0) {
		sUrl = sharedPrefs.getPreference("server_port", "NULL");
		return mBinder;
	}

	public class LocalBinder extends Binder {
		public StatsTableService getService() {
			return StatsTableService.this;
		}
	}

	@Override
	public void getPrimeTable(int season, final RequestHandler<String> handler) {
		LoopjRestClient.get(
				this,
				sUrl.concat("/SoccerServer/rest/")
						.concat(sharedPrefs.getPreference("account_name", ""))
						.concat("/table/"), null,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray res) {
						handler.onSuccess(res.toString());
					}

					@Override
					public void onFailure(Throwable tr, String res) {
						handler.onFailure(res, 0);
					}

					@Override
					public void onFinish() {
						Logger.i("Get prime table finished");
					}
				});
	}

	@Override
	public void getTable(int season, final RequestHandler<List<DAOAggrLEvents>> handler,
			EventType t) {
		LoopjRestClient.get(
				this,
				sUrl.concat("/SoccerServer/rest/")
						.concat(sharedPrefs.getPreference("account_name", ""))
						.concat("/table/").concat(String.valueOf(t.ordinal())), null,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray res) {
						onReadTableSuccess(handler, res);
					}

					@Override
					public void onFailure(Throwable tr, String res) {
						handler.onFailure(res, 0);
					}

					@Override
					public void onFinish() {
						Logger.i("Get table finished");
					}
				});
	}
	
	private void onReadTableSuccess(final RequestHandler<List<DAOAggrLEvents>> handler, JSONArray res) {
		try {
			List<DAOAggrLEvents> lst = (ArrayList<DAOAggrLEvents>) EntityManager.readAggrEvtTable(res.toString());
			handler.onSuccess(lst);
		}
		catch (SoccerException e) {
			Logger.e("onSuccess of stats service failed", e);
		}
		
		
	}
}
