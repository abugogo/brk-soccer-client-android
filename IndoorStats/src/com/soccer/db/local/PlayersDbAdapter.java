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

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.soccer.entities.impl.DAOPlayer;

public class PlayersDbAdapter extends DbAdapterBase {

	public PlayersDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public PlayersDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

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

	public boolean deletePlayer(long id) {

		return mDb.delete(DATABASE_PLAYERS_TABLE, KEY_ID + "=" + id, null) > 0;
	}

	public boolean deletePlayers() {

		return mDb.delete(DATABASE_PLAYERS_TABLE, null, null) > 0;
	}

	public Cursor fetchAllPlayers() {

		return mDb.query(DATABASE_PLAYERS_TABLE, new String[] { "_id", KEY_ID,
				KEY_BDAY, KEY_EMAIL, KEY_FNAME, KEY_LNAME, KEY_TEL1, KEY_IMG },
				null, null, null, null, null);
	}

	public ArrayList<DAOPlayer> fetchAllPlayersAsArray() {

		ArrayList<DAOPlayer> pList = new ArrayList<DAOPlayer>();
		// Get all of the rows from the database and create the item list
		Cursor PlayersCursor = fetchAllPlayers();
		if (PlayersCursor.moveToFirst()) {
			do {
				DAOPlayer p = new DAOPlayer();
				p.setId(PlayersCursor.getString(1));
				p.setFname(PlayersCursor.getString(4));
				p.setLname(PlayersCursor.getString(5));
				p.setTel1(PlayersCursor.getString(6));
				p.setP_img(PlayersCursor.getString(7));
				pList.add(p);
			} while (PlayersCursor.moveToNext());
		}
		if (PlayersCursor != null && !PlayersCursor.isClosed()) {
			PlayersCursor.close();
		}

		return pList;
	}

	public Cursor fetchPlayer(long id) throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_PLAYERS_TABLE, new String[] { "_id", KEY_ID,
				KEY_BDAY, KEY_EMAIL, KEY_FNAME, KEY_LNAME, KEY_TEL1, KEY_IMG },
				KEY_ID + "=" + id, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public boolean updatePlayer(DAOPlayer p) {
		ContentValues args = new ContentValues();

		args.put(KEY_BDAY, (null != p.getBday()) ? p.getBday().toString() : "");
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
