package com.soccer.indoorstats.services.handlers;


public abstract class RequestHandler<T> {
	public abstract void onSuccess(T t);
	public abstract void onFailure(String reason, int errorCode); 	
}
