package com.soccer.db.local;

import com.soccer.indoorstats.utils.log.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbAdapterBase extends SQLiteOpenHelper {
	/**
	 * Database creation sql statement
	 */
	private static final String STATES_TBL_CREATE = "create table "
			+ DB_CONSTS.DATABASE_STATE_TABLE
			+ " (_id integer primary key autoincrement, " + DB_CONSTS.KEY_ID
			+ " integer not null, " + DB_CONSTS.KEY_GAME_STATE + " text);";

	private static final String PLAYERS_TBL_CREATE = "create table "
			+ DB_CONSTS.DATABASE_PLAYERS_TABLE
			+ " (_id integer primary key autoincrement, " + DB_CONSTS.KEY_ID
			+ " integer not null, " + DB_CONSTS.KEY_EMAIL + " text, "
			+ DB_CONSTS.KEY_FNAME + " text not null, " + DB_CONSTS.KEY_LNAME
			+ " text, " + DB_CONSTS.KEY_TEL1 + " text, " + DB_CONSTS.KEY_IMG
			+ " text, " + DB_CONSTS.KEY_DESC + " text, " + DB_CONSTS.KEY_BDAY
			+ " text, " + DB_CONSTS.KEY_ADMIN + " integer, "
			+ DB_CONSTS.KEY_ACTIVE + " integer);";

	public DbAdapterBase(Context context) {
		super(context, DB_CONSTS.DB_NAME, null, DB_CONSTS.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(PLAYERS_TBL_CREATE);
		db.execSQL(STATES_TBL_CREATE);
		ContentValues initialValues = new ContentValues();
		initialValues.put(DB_CONSTS.KEY_GAME_STATE, "");
		initialValues.put(DB_CONSTS.KEY_ID, 0);

		db.insert(DB_CONSTS.DATABASE_STATE_TABLE, null, initialValues);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Logger.w("Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DB_CONSTS.DATABASE_PLAYERS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + DB_CONSTS.DATABASE_STATE_TABLE);
		onCreate(db);
	}

}
