package com.soccer.dialog;

import java.io.Serializable;
import java.util.ArrayList;

import com.soccer.indoorstats.ingame.IGameEvent;

public class lstItem implements Serializable {
	private static final long serialVersionUID = 6983509531169847290L;
	public String mText;
	public String mId;
	public boolean mChecked;
	public ArrayList<IGameEvent> mEvents = new ArrayList<IGameEvent>();

	public lstItem(String text, String id, boolean checked) {
		mText = text;
		mId = id;
		mChecked = checked;
	}

	public void addEvent(IGameEvent evt) {
		mEvents.add(evt);
	}

	public void removeEvent(IGameEvent evt) {
		mEvents.remove(evt);
	}

	@Override
	public String toString() {
		return this.mText;
	}

	public String toStringEvents() {
		String evts = "";
		int g = 0, og = 0, r = 0, y = 0, c = 0;
		for (IGameEvent ge : mEvents) {
			switch (ge.getEventType()) {
			case Goal:
				g++;
				break;
			case O_Goal:
				og++;
				break;
			case Y_Card:
				y++;
				break;
			case R_Card:
				r++;
				break;
			case Cook:
				c++;
				break;
			}
		}
		evts = this.mText
				+ (((g + og + r + y + c) > 0) ? "(" + g + "," + og + "," + r
						+ "," + y + "," + c + ")" : "");
		return evts;
	}
}