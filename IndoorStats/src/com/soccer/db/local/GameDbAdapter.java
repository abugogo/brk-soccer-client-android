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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.soccer.indoorstats.services.GameService.GameStatus;
import com.soccer.indoorstats.utils.log.Logger;

public class GameDbAdapter {

	private SQLiteDatabase mDB = null;

	public GameDbAdapter(SQLiteDatabase db) {
		this.mDB = db;
	}

	public long createState(ByteArrayOutputStream bos) {
		SQLiteStatement p = mDB.compileStatement("insert into "
				+ DB_CONSTS.DATABASE_STATE_TABLE + "(" + DB_CONSTS.KEY_ID + ","
				+ DB_CONSTS.KEY_GAME_STATE + ") values(0, ?)");

		p.bindBlob(1, bos.toByteArray());
		long retVal = p.executeInsert();
		return retVal;
	}

	public boolean deleteAllStates() {

		return mDB.delete(DB_CONSTS.DATABASE_STATE_TABLE, null, null) > 0;
	}

	public ObjectInputStream fetchState() throws SQLException {
		ObjectInputStream objectIn = null;
		Cursor mCursor =

		mDB.query(true, DB_CONSTS.DATABASE_STATE_TABLE, new String[] { "_id",
				DB_CONSTS.KEY_ID, DB_CONSTS.KEY_GAME_STATE }, DB_CONSTS.KEY_ID
				+ "=0", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
			if (mCursor.getCount() > 0) {
				byte[] bs = (mCursor.getBlob(mCursor
						.getColumnIndexOrThrow(DB_CONSTS.KEY_GAME_STATE)));
				if (bs != null && !bs.equals("")) {
					try {
						objectIn = new ObjectInputStream(
								new ByteArrayInputStream(bs));
					} catch (StreamCorruptedException e) {
						Logger.e(
								"game activity restore state failed due to corrupted stream",
								e);
					} catch (IOException e) {
						Logger.e(
								"game activity restore state failed due to io issue",
								e);
					}
				}
			}
			mCursor.close();
		}
		return objectIn;
	}

	public boolean insertOrUpdateState(ByteArrayOutputStream bos) {
		SQLiteStatement p = mDB.compileStatement("insert or replace into "
				+ DB_CONSTS.DATABASE_STATE_TABLE + " (_id, " + DB_CONSTS.KEY_ID
				+ "," + DB_CONSTS.KEY_GAME_STATE + ") values(0, 0, ?)");

		p.bindBlob(1, bos.toByteArray());
		long retVal = p.executeInsert();
		return (retVal != -1);
	}

	public long storeGame(String g_json) {
		SQLiteStatement p = mDB.compileStatement("insert into "
				+ DB_CONSTS.DATABASE_GAMES_TABLE + "("
				+ DB_CONSTS.KEY_GAME_STATUS + "," + DB_CONSTS.KEY_GAME_BLOB
				+ ") values(?, ?)");

		p.bindLong(1, GameStatus.Pending.ordinal());
		p.bindString(2, g_json);
		long retVal = p.executeInsert();
		return retVal;
	}

	public long updateGame(long rowid, GameStatus gs) {
		SQLiteStatement p = mDB.compileStatement("update "
				+ DB_CONSTS.DATABASE_GAMES_TABLE + " set "
				+ DB_CONSTS.KEY_GAME_STATUS + "=? WHERE _id=?");

		p.bindLong(1, gs.ordinal());
		p.bindLong(2, rowid);
		long retVal = p.executeInsert();
		return retVal;
	}

	public int removeGame(long rowid) {
		return mDB.delete(DB_CONSTS.DATABASE_GAMES_TABLE, "_id=?",
				new String[] { String.valueOf(rowid) });
	}

	public ArrayList<String> getGames(GameStatus gs) {
		ArrayList<String> retArray = new ArrayList<String>();
		Cursor mCursor = mDB.query(true, DB_CONSTS.DATABASE_GAMES_TABLE,
				new String[] { "_id", DB_CONSTS.KEY_GAME_BLOB,
						DB_CONSTS.KEY_GAME_STATUS }, DB_CONSTS.KEY_GAME_STATUS
						+ "=?", new String[] { String.valueOf(gs.ordinal()) },
				null, null, null, null);
		if (mCursor != null && mCursor.getCount() > 0) {
			mCursor.moveToFirst();
			while (!mCursor.isAfterLast()) {
				String gstr = (mCursor.getString(mCursor
						.getColumnIndexOrThrow(DB_CONSTS.KEY_GAME_BLOB)));
				if (gstr != null && !"".equals(gstr)) {
					retArray.add(gstr);
				}
			}
			mCursor.close();

		}
		return retArray;
	}
}
