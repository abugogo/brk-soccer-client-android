package com.soccer.indoorstats.services.i;

import com.soccer.entities.impl.DAOGame;
import com.soccer.indoorstats.activity.states.GameState;
import com.soccer.indoorstats.services.handlers.RequestHandler;

public interface IGameService {
	public GameState getCurGameState();
	public void saveGameState(GameState state);
	public void updateGame(DAOGame game, RequestHandler handler);
}
