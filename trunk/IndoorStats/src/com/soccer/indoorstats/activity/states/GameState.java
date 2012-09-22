package com.soccer.indoorstats.activity.states;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.dialog.lstItem;
import com.soccer.entities.impl.DAOPlayer;

public class GameState implements Serializable{

	private static final long serialVersionUID = -8375922051362602160L;
	final private ArrayList<lstItem> _team1List = new ArrayList<lstItem>();
	final private ArrayList<lstItem> _team2List = new ArrayList<lstItem>();
	private PlayersDbAdapter _mDbHelper = null;
	final private HashMap<String, DAOPlayer> _pList = new HashMap<String, DAOPlayer>();
	final private int FULL_TIME = 7 * 60;
	private boolean backwards = false;
	private boolean started = false;

	public PlayersDbAdapter get_mDbHelper() {
		return _mDbHelper;
	}

	public void set_mDbHelper(PlayersDbAdapter _mDbHelper) {
		this._mDbHelper = _mDbHelper;
	}

	public boolean isBackwards() {
		return backwards;
	}

	public void setBackwards(boolean back) {
		backwards = back;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean start) {
		started = start;
	}

	public ArrayList<lstItem> get_team1List() {
		return _team1List;
	}

	public ArrayList<lstItem> get_team2List() {
		return _team2List;
	}

	public int getFullTime() {
		return FULL_TIME;
	}

	public HashMap<String, DAOPlayer> get_pList() {
		return _pList;
	}

}
