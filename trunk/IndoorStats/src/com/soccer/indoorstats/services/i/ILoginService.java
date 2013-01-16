package com.soccer.indoorstats.services.i;

import org.json.JSONArray;

import com.soccer.indoorstats.services.handlers.RequestHandler;

public interface ILoginService {
	public void login(String user, String password, String account, RequestHandler<JSONArray> handler);
}
