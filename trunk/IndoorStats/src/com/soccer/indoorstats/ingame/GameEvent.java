package com.soccer.indoorstats.ingame;

import java.io.Serializable;

public class GameEvent implements IGameEvent, Serializable,
		Comparable<GameEvent> {

	private static final long serialVersionUID = 1L;
	private EventType _etype;
	private int _minute;

	public GameEvent(EventType t, int min) {
		_etype = t;
		_minute = min;
	}

	@Override
	public EventType getEventType() {
		return _etype;
	}

	@Override
	public int getMinute() {
		return _minute;
	}

	@Override
	public void setMinute(int min) {
		// TODO Auto-generated method stub

	}

	@Override
	public int compareTo(GameEvent other) {
		if (this == other)
			return 0;
		return (other != null && this._etype == other.getEventType() && this._minute == other
				.getMinute()) ? 0 : -1;
	}

}
