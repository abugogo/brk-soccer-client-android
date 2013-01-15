package com.soccer.indoorstats.activity.states;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.LinkedHashMap;

import com.soccer.entities.impl.PrintableLineup;
import com.soccer.indoorstats.utils.log.Logger;

public class GameState implements Serializable {

	private static final long serialVersionUID = -8375922051362602160L;
	private LinkedHashMap<String, PrintableLineup> _lpList = new LinkedHashMap<String, PrintableLineup>();
	private boolean _backwards;
	private boolean _started;
	private long _startTime;
	private long _stopTime;
	private boolean _running;

	public GameState() {
		_lpList = new LinkedHashMap<String, PrintableLineup>();
		_backwards = false;
		_started = false;
		_startTime = 0;
		_stopTime = 0;
		_running = false;
	}

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

	public LinkedHashMap<String, PrintableLineup> get_lpList() {
		return _lpList;
	}

	public void set_lpList(LinkedHashMap<String, PrintableLineup> lpMap) {
		if (lpMap != null) {
			if (this._lpList == null)
				this._lpList = new LinkedHashMap<String, PrintableLineup>();
			this._lpList.clear();
			this._lpList.putAll(lpMap);
		}
	}

	public ByteArrayOutputStream serialize() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(this);
			out.flush();
			out.close();
		} catch (IOException e) {
			Logger.e("game activity save state failed due to io", e);
		}
		return bos;
	}

	public void deserialize(ObjectInputStream objectIn) {
		if (objectIn != null) {
			try {
				GameState state = (GameState) objectIn.readObject();
				if (state != null) {
					this._backwards = state._backwards;
					this._running = state._running;
					this._started = state._started;
					this._startTime = state._startTime;
					this._stopTime = state._stopTime;
					set_lpList(state.get_lpList());
				}
			} catch (OptionalDataException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
