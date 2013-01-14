package com.soccer.indoorstats.services.i;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;

import com.soccer.entities.impl.DAOGame;
import com.soccer.indoorstats.services.handlers.RequestHandler;

public interface IGameService {
	public ObjectInputStream getCurGameState();
	public void saveGameState(ByteArrayOutputStream bos);
	public void updateGame(DAOGame game, RequestHandler handler);
}
