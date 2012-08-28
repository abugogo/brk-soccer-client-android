package com.soccer.db.remote;

import java.util.ArrayList;

import com.soccer.connector.RestClient;
import com.soccer.entities.EntityManager;
import com.soccer.entities.IDAOPlayer;

public class RemoteDBAdapter {
	
	public enum RequestMethod {
		GET, POST, PUT
	}

	public IDAOPlayer getPlayer(String sUrl, String id) throws Exception {
		IDAOPlayer p = null;
		RestClient c = new RestClient(sUrl + "/SoccerServer/rest/players/" + id);
		c.AddHeader("Accept", "application/json");
		c.AddHeader("Content-type", "application/json");

		c.ExecuteCall(RequestMethod.GET);
		if(c.getResponseCode() == 200)
			p = EntityManager.readPlayer(c.getResponse()); 
		return p;

	}
	
	public ArrayList<IDAOPlayer> getPlayers(String sUrl) throws Exception {
		ArrayList<IDAOPlayer> pArr = null;
		RestClient c = new RestClient(sUrl + "/SoccerServer/rest/players");
		c.AddHeader("Accept", "application/json");
		c.AddHeader("Content-type", "application/json");

		c.ExecuteCall(RequestMethod.GET);
		if(c.getResponseCode() == 200)
			pArr = EntityManager.readPlayers(c.getResponse()); 
		return pArr;

	}
	
	public IDAOPlayer updatePlayer(String sUrl, IDAOPlayer p) throws Exception {
		RestClient c = new RestClient(sUrl + "/SoccerServer/rest/players/" + p.getId());
		c.AddHeader("Accept", "application/json");
		c.AddHeader("Content-type", "application/json");
		c.setJSonBody(EntityManager.writePlayer(p));
		
		c.ExecuteCall(RequestMethod.POST);
		if(c.getResponseCode() != 200)
			throw new Exception("Error code:" + c.getResponseCode() + ", Error:" + c.getErrorMessage()); 
		return p;

	}

}
