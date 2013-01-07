package com.soccer.indoorstats.services.i;

import java.util.ArrayList;

import com.soccer.entities.impl.DAOPlayer;
import com.soccer.indoorstats.services.handlers.RequestHandler;

public interface IPlayerService {
	public DAOPlayer getPlayer(String id);
	public ArrayList<DAOPlayer> getAllPlayers();
	public void updatePlayer(DAOPlayer player, RequestHandler handler);
}
