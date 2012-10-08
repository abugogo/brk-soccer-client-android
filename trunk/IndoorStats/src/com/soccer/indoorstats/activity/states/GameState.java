package com.soccer.indoorstats.activity.states;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.soccer.db.local.PlayersDbAdapter;
import com.soccer.dialog.lstItem;
import com.soccer.entities.impl.DAOPlayer;
import com.soccer.preferences.Prefs;

public class GameState implements Serializable{

	private static final long serialVersionUID = -8375922051362602160L;
	final private ArrayList<lstItem> _team1List = new ArrayList<lstItem>();
	final private ArrayList<lstItem> _team2List = new ArrayList<lstItem>();
	private PlayersDbAdapter _mDbHelper = null;
	final private HashMap<String, DAOPlayer> _pList = new HashMap<String, DAOPlayer>();
	private boolean _backwards = false;
	private boolean _started = false;
	private long _startTime = 0;
	private long _stopTime = 0;
	private boolean _running = false;
	
	public long get_startTime() {
		return _startTime;
	}

	public void set_startTime(long _startTime) {
		this._startTime = _startTime;
	}

	public long get_stopTime() {
		return _stopTime;
	}

	public void set_stopTime(long _stopTime) {
		this._stopTime = _stopTime;
	}

	public boolean is_running() {
		return _running;
	}

	public void set_running(boolean _running) {
		this._running = _running;
	}

	public PlayersDbAdapter get_mDbHelper() {
		return _mDbHelper;
	}

	public void set_mDbHelper(PlayersDbAdapter _mDbHelper) {
		this._mDbHelper = _mDbHelper;
	}

	public boolean isBackwards() {
		return _backwards;
	}

	public void setBackwards(boolean back) {
		_backwards = back;
	}

	public boolean isStarted() {
		return _started;
	}

	public void setStarted(boolean start) {
		_started = start;
	}

	public ArrayList<lstItem> get_team1List() {
		return _team1List;
	}

	public ArrayList<lstItem> get_team2List() {
		return _team2List;
	}

	public HashMap<String, DAOPlayer> get_pList() {
		return _pList;
	}

}
