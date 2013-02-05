package com.soccer.indoorstats.services.i;

import java.util.List;

import com.soccer.entities.IDAOLEvent.EventType;
import com.soccer.entities.impl.DAOAggrLEvents;
import com.soccer.indoorstats.services.handlers.RequestHandler;

public interface IStatsTableService {	
	public void getPrimeTable(int season, final RequestHandler<String> handler);
	public void getTable(int season, final RequestHandler<List<DAOAggrLEvents>> handler, EventType t);
}
