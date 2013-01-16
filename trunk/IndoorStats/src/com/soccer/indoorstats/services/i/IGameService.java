package com.soccer.indoorstats.services.i;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import com.soccer.entities.impl.DAOGame;
import com.soccer.indoorstats.services.handlers.RequestHandler;

public interface IGameService {
	public static enum GameStatus {
		Pending,
		Failed
	}
	
	public ObjectInputStream getCurGameState();
	public void saveGameState(ByteArrayOutputStream bos);
	public void updateGame(DAOGame game, RequestHandler handler);
	public ArrayList<DAOGame> getAllGames(int season);
	public ArrayList<DAOGame> getGames(GameStatus gs);
}
