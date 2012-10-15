package com.soccer.indoorstats.ingame;

public interface IGameEvent {
	enum EventType {
		Goal,
		Cook,
		O_Goal,
		Y_Card,
		R_Card
	}
	
	public EventType getEventType();
	public int getMinute();
	public void setMinute(int min);
}
