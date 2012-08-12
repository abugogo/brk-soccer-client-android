/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.soccer.db.local;

import com.soccer.entities.impl.DAOPlayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class PlayersDbAdapter {

	public static final String KEY_ID = "id";
	public static final String KEY_BDAY = "bday";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_FNAME = "fname";
	public static final String KEY_LNAME = "lname";
	public static final String KEY_TEL1 = "tel1";
	public static final String KEY_IMG = "P_img";
	
	private static final String TAG = "PlayersDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_PLAYERS_TABLE = "players";

	/**
	 * Database creation sql statement
	 */
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
			+ KEY_BDAY + " text);";

	private static final String DATABASE_NAME = "indoorsoccer";

	private static final int DATABASE_VERSION = 4;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(PLAYERS_TBL_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS players");
			onCreate(db);
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public PlayersDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the notes database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public PlayersDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	/**
	 * Create a new note using the title and body provided. If the note is
	 * successfully created return the new rowId for that note, otherwise return
	 * a -1 to indicate failure.
	 * 
	 * @param title
	 *            the title of the note
	 * @param body
	 *            the body of the note
	 * @return rowId or -1 if failed
	 */
	public long createPlayer(DAOPlayer p) {
		ContentValues initialValues = new ContentValues();
		if (p.getBday() != null)
			initialValues.put(KEY_BDAY, p.getBday().toString());
		initialValues.put(KEY_EMAIL, p.getEmail());
		initialValues.put(KEY_FNAME, p.getFname());
		initialValues.put(KEY_ID, p.getId());
		initialValues.put(KEY_LNAME, p.getLname());
		initialValues.put(KEY_TEL1, p.getTel1());
		initialValues.put(KEY_IMG, p.getP_img());

		return mDb.insert(DATABASE_PLAYERS_TABLE, null, initialValues);
	}

	/**
	 * Delete the note with the given rowId
	 * 
	 * @param rowId
	 *            id of note to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deletePlayer(long id) {

		return mDb.delete(DATABASE_PLAYERS_TABLE, KEY_ID + "=" + id, null) > 0;
	}

	public boolean deletePlayers() {

		return mDb.delete(DATABASE_PLAYERS_TABLE, null, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all players in the database
	 * 
	 * @return Cursor over all notes
	 */
	public Cursor fetchAllPlayers() {

		return mDb.query(DATABASE_PLAYERS_TABLE, new String[] { "_id", KEY_ID,
				KEY_BDAY, KEY_EMAIL, KEY_FNAME, KEY_LNAME, KEY_TEL1, KEY_IMG }, null,
				null, null, null, null);
	}

	/**
	 * Return a Cursor positioned at the note that matches the given rowId
	 * 
	 * @param rowId
	 *            id of note to retrieve
	 * @return Cursor positioned to matching note, if found
	 * @throws SQLException
	 *             if note could not be found/retrieved
	 */
	public Cursor fetchPlayer(long id) throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_PLAYERS_TABLE, new String[] { "_id", KEY_ID,
				KEY_BDAY, KEY_EMAIL, KEY_FNAME, KEY_LNAME, KEY_TEL1, KEY_IMG }, KEY_ID
				+ "=" + id, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	/**
	 * Update the note using the details provided. The note to be updated is
	 * specified using the rowId, and it is altered to use the title and body
	 * values passed in
	 * 
	 * @param rowId
	 *            id of note to update
	 * @param title
	 *            value to set note title to
	 * @param body
	 *            value to set note body to
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updatePlayer(DAOPlayer p) {
		ContentValues args = new ContentValues();

		args.put(KEY_BDAY, p.getBday().toString());
		args.put(KEY_EMAIL, p.getEmail());
		args.put(KEY_FNAME, p.getFname());
		args.put(KEY_ID, p.getId());
		args.put(KEY_LNAME, p.getLname());
		args.put(KEY_TEL1, p.getTel1());
		args.put(KEY_IMG, p.getP_img());
		
		return mDb.update(DATABASE_PLAYERS_TABLE, args,
				KEY_ID + "=" + p.getId(), null) > 0;
	}
}
