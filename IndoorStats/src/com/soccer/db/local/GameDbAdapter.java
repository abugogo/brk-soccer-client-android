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
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import com.soccer.indoorstats.activity.states.GameState;
import com.soccer.indoorstats.utils.log.Logger;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

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

	public GameState fetchState() throws SQLException {
		GameState state = null;
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
					ObjectInputStream objectIn;
					Object obj;
					try {
						objectIn = new ObjectInputStream(
								new ByteArrayInputStream(bs));
						obj = objectIn.readObject();
						state = (GameState) obj;
					} catch (StreamCorruptedException e) {
						Logger.e(
								"game activity restore state failed due to corrupted stream",
								e);
					} catch (IOException e) {
						Logger.e(
								"game activity restore state failed due to io issue",
								e);
					} catch (ClassNotFoundException e) {
						Logger.e(
								"game activity restore state failed due to class not found",
								e);
					}
				}
			}
			mCursor.close();
		}
		return state;
	}

	public boolean insertOrUpdateState(GameState state) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(state);
			out.flush();
			out.close();
		} catch (IOException e) {
			Logger.e("game activity save state failed due to io", e);
		}
		SQLiteStatement p = mDB.compileStatement("insert or replace into "
				+ DB_CONSTS.DATABASE_STATE_TABLE + " (_id, " + DB_CONSTS.KEY_ID
				+ "," + DB_CONSTS.KEY_GAME_STATE + ") values(0, 0, ?)");

		p.bindBlob(1, bos.toByteArray());
		long retVal = p.executeInsert();
		return (retVal != -1);
	}
}
