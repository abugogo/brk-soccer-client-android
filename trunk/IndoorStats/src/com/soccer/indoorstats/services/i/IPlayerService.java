package com.soccer.indoorstats.services.i;

import java.util.LinkedList;

import org.json.JSONObject;

import com.soccer.entities.impl.DAOPlayer;
import com.soccer.indoorstats.services.handlers.RequestHandler;

public interface IPlayerService {
	public DAOPlayer getPlayer(String id);
	public LinkedList<DAOPlayer> getAllPlayers();
	public void updatePlayer(DAOPlayer player, RequestHandler<JSONObject> handler);
}
