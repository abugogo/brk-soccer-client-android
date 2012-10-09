package com.soccer.db.remote;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.soccer.entities.EntityManager;
import com.soccer.entities.IDAOPlayer;
import com.soccer.indoorstats.activity.i.IAsyncTaskAct;
import com.soccer.rest.RestClient.RequestMethod;
import com.soccer.rest.asynctask.AsyncRestCallBasic;
import com.soccer.rest.asynctask.AsyncRestCallParams;

public class RemoteDBAdapter {
	/*
	 * public static IDAOPlayer getPlayer(IAsyncTaskAct caller, String sUrl,
	 * String id) throws Exception { IDAOPlayer p = null; RestClient c = new
	 * RestClient(sUrl + "/SoccerServer/rest/players/" + id);
	 * c.AddHeader("Accept", "application/json"); c.AddHeader("Content-type",
	 * "application/json");
	 * 
	 * c.ExecuteCall(RequestMethod.GET); if (c.getResponseCode() == 200) p =
	 * EntityManager.readPlayer(c.getResponse()); return p;
	 * 
	 * }
	 */

	public static void getPlayers(IAsyncTaskAct caller, String sUrl,
			String status) throws Exception {
		AsyncRestCallBasic asyncCall = new AsyncRestCallBasic(caller,
				status);
		ArrayList<NameValuePair> nva = new ArrayList<NameValuePair>();
		nva.add(new BasicNameValuePair("Accept", "application/json"));
		nva.add(new BasicNameValuePair("Content-type", "application/json"));

		AsyncRestCallParams params = new AsyncRestCallParams(sUrl
				+ "/SoccerServer/rest/players", RequestMethod.GET, nva, "");
		asyncCall.execute(params);
	}

	public static void updatePlayer(IAsyncTaskAct caller, String sUrl,
			String status, IDAOPlayer p) throws Exception {
		AsyncRestCallBasic asyncCall = new AsyncRestCallBasic(caller,
				status);
		ArrayList<NameValuePair> nva = new ArrayList<NameValuePair>();
		nva.add(new BasicNameValuePair("Accept", "application/json"));
		nva.add(new BasicNameValuePair("Content-type",
				"application/x-www-form-urlencoded"));

		AsyncRestCallParams params = new AsyncRestCallParams(sUrl
				+ "/SoccerServer/rest/players/" + p.getId(),
				RequestMethod.POST, nva, EntityManager.writePlayer(p));
		asyncCall.execute(params);

	}

	public static void getPlayerStats(IAsyncTaskAct caller, String sUrl, String pId,
			String status) throws Exception {
		AsyncRestCallBasic asyncCall = new AsyncRestCallBasic(caller,
				status);
		ArrayList<NameValuePair> nva = new ArrayList<NameValuePair>();
		nva.add(new BasicNameValuePair("Accept", "application/json"));
		nva.add(new BasicNameValuePair("Content-type", "application/json"));

		AsyncRestCallParams params = new AsyncRestCallParams(sUrl
				+ "/SoccerServer/rest/players/" + pId + "/stats", RequestMethod.GET, nva, "");
		asyncCall.execute(params);
	}
	
}
