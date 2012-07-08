package com.soccer.dal.entities.api;


/**
 * The primary key class for the lineup database table.
 * 
 */
public interface ILineupPK {
	public String getGameId();
	public void setGameId(String gameId);
	public String getPlayerId();
	public void setPlayerId(String playerId);
	public boolean equals(Object other);    
	public int hashCode();
}