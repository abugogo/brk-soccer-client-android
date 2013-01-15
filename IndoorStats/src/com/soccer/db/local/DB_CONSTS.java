package com.soccer.db.local;

public abstract class DB_CONSTS {
	public static final String DB_NAME = "indoorsoccer";
	public static final int DB_VERSION = 12;
	
	public static final String DATABASE_PLAYERS_TABLE = "players";
	public static final String DATABASE_STATE_TABLE = "state_table";
	public static final String DATABASE_GAMES_TABLE = "games_table";
	
	// players fields
	public static final String KEY_ID = "id";
	public static final String KEY_PWRD = "pwrd";
	public static final String KEY_BDAY = "bday";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_FNAME = "fname";
	public static final String KEY_LNAME = "lname";
	public static final String KEY_TEL1 = "tel1";
	public static final String KEY_IMG = "P_img";
	public static final String KEY_DESC = "description";
	public static final String KEY_ADMIN = "isAdmin";
	public static final String KEY_ACTIVE = "isActive";
	
	// state fields
	public static final String KEY_GAME_STATE = "gstate";

	// games fields
	public static final String KEY_GAME_BLOB = "gblob";
	public static final String KEY_GAME_STATUS = "gstatus";
	
	
	public static final String TAG_PLAYERS = "PlayersDbAdapter";
	public static final String TAG_STATES = "StateDbAdapter";
}
