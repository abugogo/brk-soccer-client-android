package com.soccer.async;

public interface AsyncGetPlayerListener<IDAOPlayer> {
	public void onTaskComplete(IDAOPlayer result);

}
