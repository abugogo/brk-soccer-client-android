package com.soccer.indoorstats.services.i;

import com.soccer.indoorstats.services.handlers.RequestHandler;

public interface ILoginService {
	public void login(String user, String password, String account, RequestHandler<String> handler);
}
