package com.soccer.connector;

import com.soccer.dal.entities.api.IDAOPlayer;
import flexjson.JSONDeserializer;

public class RemoteDBAdapter {
	
	public enum RequestMethod {
		GET, POST
	}

	public IDAOPlayer getPlayer(String sUrl, String id) throws Exception {
		IDAOPlayer p = null;
		RestClient c = new RestClient(sUrl + "/SoccerServer/rest/players/" + id);
		c.AddHeader("Accept", "application/json");
		c.AddHeader("Content-type", "application/json");

		c.ExecuteCall(RequestMethod.GET);
		if(c.getResponseCode() == 200)
			p = new JSONDeserializer<IDAOPlayer>().deserialize(c.getResponse()); 
		return p;

	}

	/*
	 * public List<IDAOPlayer> getAllPlayers(String sUrl) throws Exception {
	 * RestClient c = new RestClient(sUrl + "/SoccerServer/rest/players");
	 * c.AddHeader("Accept", "application/json"); c.AddHeader("Content-type",
	 * "application/json");
	 * 
	 * c.Execute(RequestMethod.GET);
	 * 
	 * List<IDAOPlayer> lst = new JSONDeserializer<List<IDAOPlayer>>()
	 * .deserialize(c.getResponse()); return lst;
	 * 
	 * }
	 */
}
