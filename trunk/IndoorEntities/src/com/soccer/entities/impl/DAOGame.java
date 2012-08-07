package com.soccer.entities.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import com.soccer.entities.IDAOGame;


/**
 * The persistent class for the games_tbl database table.
 * 
 */
public class DAOGame implements Serializable, IDAOGame {
	private static final long serialVersionUID = 1L;
	private String gameId;
	private int bgoals;
	private String description;
	private Date gameDate;
	private String gameName;
	private byte hasDraft;
	private String misc;
	private String more;
	private int wgoals;
	private Object winner;

    public DAOGame() {
    	
    }

    public DAOGame(HashMap<String, Object> columns) {
    	this.setGameId("" + columns.get("game_id"));
    	this.setBgoals((Integer) columns.get("bgoals"));
    	this.setDescription((String) columns.get("description"));
    	this.setGameDate((Date)columns.get("game_date"));
    	this.setGameName((String) columns.get("game_name"));
    	this.setHasDraft((byte)((Boolean) columns.get("has_draft")?1:0));
    	this.setMisc((String) columns.get("misc"));
    	this.setMore((String) columns.get("more"));
    	this.setWgoals((Integer)columns.get("wgoals"));
    	this.setWinner((String) columns.get("winner"));
	}
    
	public String getGameId() {
		return this.gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}


	public int getBgoals() {
		return this.bgoals;
	}

	public void setBgoals(int bgoals) {
		this.bgoals = bgoals;
	}


	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Date getGameDate() {
		return this.gameDate;
	}

	public void setGameDate(Date gameDate) {
		this.gameDate = gameDate;
	}


	public String getGameName() {
		return this.gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}


	public byte getHasDraft() {
		return this.hasDraft;
	}

	public void setHasDraft(byte hasDraft) {
		this.hasDraft = hasDraft;
	}


	public String getMisc() {
		return this.misc;
	}

	public void setMisc(String misc) {
		this.misc = misc;
	}


	public String getMore() {
		return this.more;
	}

	public void setMore(String more) {
		this.more = more;
	}


	public int getWgoals() {
		return this.wgoals;
	}

	public void setWgoals(int wgoals) {
		this.wgoals = wgoals;
	}


	public Object getWinner() {
		return this.winner;
	}

	public void setWinner(Object winner) {
		this.winner = winner;
	}
}