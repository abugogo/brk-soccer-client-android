package com.soccer.indoorstats.services.handlers;

public abstract class LoginHandler {
	public abstract void onSuccess();
	public abstract void onFailure(String reason, int errorCode); 	
}
