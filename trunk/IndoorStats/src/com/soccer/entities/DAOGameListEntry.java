package com.soccer.entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.soccer.entities.impl.DAOGame;

public class DAOGameListEntry extends DAOGame {

	private static final long serialVersionUID = 1L;
	private boolean _isSection;

	public DAOGameListEntry(boolean isSection, Date d, GameStatus gs) {
		super();
		setGameDate(d);
		setStatus((gs==null)?GameStatus.Success:gs);
		_isSection = isSection;
	}
	
	public DAOGameListEntry(DAOGame game, boolean isSection) {
		super(game);
		_isSection = isSection;
	}

	public String getTitle() {
		String ttl = "";
		if (getStatus() != null) {
			switch (getStatus()) {
			case Failed:
			case Pending:
				ttl = "Failed Save";
				break;
			default:
				ttl = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
						.format(getGameDate());
				break;
			}
		}
		return ttl;
	}

	public boolean isSection() {
		return _isSection;
	}
}
