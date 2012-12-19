package com.soccer.db.local;

import com.soccer.indoorstats.utils.log.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbAdapterBase {
	protected static final String DATABASE_PLAYERS_TABLE = "players";
	protected static final String DATABASE_STATE_TABLE = "state_table";
	public static final String KEY_ID = "id";
	public static final String KEY_BDAY = "bday";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_FNAME = "fname";
	public static final String KEY_LNAME = "lname";
	public static final String KEY_TEL1 = "tel1";
	public static final String KEY_IMG = "P_img";
	public static final String KEY_DESC = "description";
	
	public static final String KEY_GAME_STATE = "gstate";

	protected static final String TAG_PLAYERS = "PlayersDbAdapter";
	protected static final String TAG_STATES = "StateDbAdapter";
	protected DatabaseHelper mDbHelper;
	protected SQLiteDatabase mDb;
	
	

	/**
	 * Database creation sql statement
	 */
	private static final String STATES_TBL_CREATE = "create table "
			+ DATABASE_STATE_TABLE
			+ " (_id integer primary key autoincrement, " + KEY_ID
			+ " integer not null, " + KEY_GAME_STATE + " text);";

	private static final String PLAYERS_TBL_CREATE = "create table " + DATABASE_PLAYERS_TABLE + " (_id integer primary key autoincrement, "
			+ KEY_ID
			+ " integer not null, "
			+ KEY_EMAIL
			+ " text, "
			+ KEY_FNAME
			+ " text not null, "
			+ KEY_LNAME
			+ " text, "
			+ KEY_TEL1
			+ " text, "
			+ KEY_IMG
			+ " text, "
			+ KEY_DESC
			+ " text, "
			+ KEY_BDAY + " text);";

	protected Context mCtx = null;

	public static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DB_CONSTS.DB_NAME, null, DB_CONSTS.DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(PLAYERS_TBL_CREATE);
			db.execSQL(STATES_TBL_CREATE);
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_GAME_STATE, "");
			initialValues.put(KEY_ID, 0);

			db.insert(DATABASE_STATE_TABLE, null, initialValues);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Logger.w("Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_PLAYERS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_STATE_TABLE);
			onCreate(db);
		}
	}
}
