package com.soccer.indoorstats.services.i;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.soccer.entities.IDAOGame.GameStatus;
import com.soccer.entities.impl.DAOGame;
import com.soccer.indoorstats.services.handlers.RequestHandler;

public interface IGameService {	
	public ObjectInputStream getCurGameState();
	public void saveGameState(ByteArrayOutputStream bos);
	public void updateGame(DAOGame game, RequestHandler<JSONObject> handler);
	public void getAllGames(int season, final RequestHandler<JSONArray> handler);
	public ArrayList<DAOGame> getGames(List<GameStatus> gs);
}
