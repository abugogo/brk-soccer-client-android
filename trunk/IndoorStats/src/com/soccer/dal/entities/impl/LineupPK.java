package com.soccer.dal.entities.impl;

import java.io.Serializable;

import com.soccer.dal.entities.api.ILineupPK;

/**
 * The primary key class for the lineup database table.
 * 
 */
public class LineupPK implements Serializable, ILineupPK {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String gameId;
	private String playerId;

    public LineupPK() {
    }

	public String getGameId() {
		return this.gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getPlayerId() {
		return this.playerId;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof LineupPK)) {
			return false;
		}
		LineupPK castOther = (LineupPK)other;
		return 
			this.gameId.equals(castOther.gameId)
			&& this.playerId.equals(castOther.playerId);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.gameId.hashCode();
		hash = hash * prime + this.playerId.hashCode();
		
		return hash;
    }
}