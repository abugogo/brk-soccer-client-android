package com.soccer.connector;

import java.util.List;

import com.soccer.connector.RestClient.RequestMethod;
import com.soccer.dal.entities.api.IDAOPlayer;
import flexjson.JSONDeserializer;

public class RemoteDBAdapter {

	public static IDAOPlayer getPlayer(String sUrl, String id) throws Exception {
		RestClient c = new RestClient(sUrl + "/SoccerServer/rest/players/" + id);
		c.AddHeader("Accept", "application/json");
		c.AddHeader("Content-type", "application/json");

		c.Execute(RequestMethod.GET);

		IDAOPlayer p = new JSONDeserializer<IDAOPlayer>().deserialize(c
				.getResponse());

		return p;

	}

	public static List<IDAOPlayer> getAllPlayers(String sUrl) throws Exception {
		RestClient c = new RestClient(sUrl + "/SoccerServer/rest/players");
		c.AddHeader("Accept", "application/json");
		c.AddHeader("Content-type", "application/json");

		c.Execute(RequestMethod.GET);

		List<IDAOPlayer> lst = new JSONDeserializer<List<IDAOPlayer>>()
				.deserialize(c.getResponse());
		return lst;

	}

}
