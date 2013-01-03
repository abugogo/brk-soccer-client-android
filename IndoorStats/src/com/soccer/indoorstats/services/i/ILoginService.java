package com.soccer.indoorstats.services.i;

import com.soccer.indoorstats.services.handlers.LoginHandler;

public interface ILoginService {
	public void login(String user, String password, String account, LoginHandler handler);
}
