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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.soccer.entities.impl.DAOPlayer;
import com.soccer.indoorstats.utils.log.Logger;

public class PlayersDbAdapter {

	private SQLiteDatabase mDB = null;

	public PlayersDbAdapter(SQLiteDatabase db) {
		mDB = db;
	}

	public long createPlayer(DAOPlayer p) {
		ContentValues initialValues = new ContentValues();
		if (p.getBday() != null)
			initialValues.put(DB_CONSTS.KEY_BDAY, p.getBday().toString());
		initialValues.put(DB_CONSTS.KEY_EMAIL, p.getEmail());
		initialValues.put(DB_CONSTS.KEY_FNAME, p.getFname());
		initialValues.put(DB_CONSTS.KEY_ID, p.getId());
		initialValues.put(DB_CONSTS.KEY_LNAME, p.getLname());
		initialValues.put(DB_CONSTS.KEY_TEL1, p.getTel1());
		initialValues.put(DB_CONSTS.KEY_IMG, p.getP_img());
		initialValues.put(DB_CONSTS.KEY_DESC, p.getDescription());

		return mDB
				.insert(DB_CONSTS.DATABASE_PLAYERS_TABLE, null, initialValues);
	}

	public boolean deletePlayer(long id) {

		return mDB.delete(DB_CONSTS.DATABASE_PLAYERS_TABLE, DB_CONSTS.KEY_ID
				+ "=" + id, null) > 0;
	}

	public boolean deletePlayers() {

		return mDB.delete(DB_CONSTS.DATABASE_PLAYERS_TABLE, null, null) > 0;
	}

	public Cursor fetchAllPlayers() {

		return mDB.query(DB_CONSTS.DATABASE_PLAYERS_TABLE, new String[] {
				"_id", DB_CONSTS.KEY_ID, DB_CONSTS.KEY_BDAY,
				DB_CONSTS.KEY_EMAIL, DB_CONSTS.KEY_FNAME, DB_CONSTS.KEY_LNAME,
				DB_CONSTS.KEY_TEL1, DB_CONSTS.KEY_IMG, DB_CONSTS.KEY_DESC },
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
				p.setDescription(PlayersCursor.getString(8));
				pList.add(p);
			} while (PlayersCursor.moveToNext());
		}
		if (PlayersCursor != null && !PlayersCursor.isClosed()) {
			PlayersCursor.close();
		}

		return pList;
	}

	public Cursor fetchPlayerFromDB(long id) throws SQLException {

		Cursor mCursor =

		mDB.query(true, DB_CONSTS.DATABASE_PLAYERS_TABLE, new String[] { "_id",
				DB_CONSTS.KEY_ID, DB_CONSTS.KEY_BDAY, DB_CONSTS.KEY_EMAIL,
				DB_CONSTS.KEY_FNAME, DB_CONSTS.KEY_LNAME, DB_CONSTS.KEY_TEL1,
				DB_CONSTS.KEY_IMG, DB_CONSTS.KEY_DESC }, DB_CONSTS.KEY_ID + "="
				+ id, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}
	
	public DAOPlayer fetchPlayer(long id) {
		Cursor cP = fetchPlayerFromDB(id);
		DAOPlayer p = new DAOPlayer();
		if (cP.getCount() > 0) {
			p.setFname(cP.getString(cP
					.getColumnIndexOrThrow(DB_CONSTS.KEY_FNAME)));
			p.setLname(cP.getString(cP
					.getColumnIndexOrThrow(DB_CONSTS.KEY_LNAME)));
			p.setEmail(cP.getString(cP
					.getColumnIndexOrThrow(DB_CONSTS.KEY_EMAIL)));
			p.setP_img(cP.getString(cP.getColumnIndexOrThrow(DB_CONSTS.KEY_IMG)));
			p.setDescription(cP.getString(cP
					.getColumnIndexOrThrow(DB_CONSTS.KEY_DESC)));
			Date d = null;
			try {
				String sBDate = cP.getString(cP
						.getColumnIndexOrThrow(DB_CONSTS.KEY_BDAY));
				if (sBDate != null && !sBDate.equals(""))
					d = (Date) new SimpleDateFormat(
							"EEE MMM d HH:mm:ss zzz yyyy", Locale.ENGLISH)
							.parse(sBDate);

			} catch (IllegalArgumentException e) {
				Logger.e(
						"loadplayersfromdb failed due to illegal state",
						e);
			} catch (ParseException e) {
				Logger.e(
						"loadplayersfromdb failed due to parse exception",
						e);
			}
			if (d != null)
				p.setBday(d);
			p.setId(cP.getString(cP.getColumnIndexOrThrow(DB_CONSTS.KEY_ID)));
			p.setTel1(cP.getString(cP.getColumnIndexOrThrow(DB_CONSTS.KEY_TEL1)));
		}
		if (null != cP)
			cP.close();
		
		return p;
	}

	public boolean updatePlayer(DAOPlayer p) {
		ContentValues args = new ContentValues();

		args.put(DB_CONSTS.KEY_BDAY, (null != p.getBday()) ? p.getBday()
				.toString() : "");
		args.put(DB_CONSTS.KEY_EMAIL, p.getEmail());
		args.put(DB_CONSTS.KEY_FNAME, p.getFname());
		args.put(DB_CONSTS.KEY_ID, p.getId());
		args.put(DB_CONSTS.KEY_LNAME, p.getLname());
		args.put(DB_CONSTS.KEY_TEL1, p.getTel1());
		args.put(DB_CONSTS.KEY_IMG, p.getP_img());
		args.put(DB_CONSTS.KEY_DESC, p.getDescription());

		return mDB.update(DB_CONSTS.DATABASE_PLAYERS_TABLE, args,
				DB_CONSTS.KEY_ID + "=" + p.getId(), null) > 0;
	}

	public boolean upsertPlayer(DAOPlayer p) {
		final String fields = DB_CONSTS.KEY_ID.concat(",")
				.concat(DB_CONSTS.KEY_BDAY).concat(",")
				.concat(DB_CONSTS.KEY_EMAIL).concat(",")
				.concat(DB_CONSTS.KEY_FNAME).concat(",")
				.concat(DB_CONSTS.KEY_LNAME).concat(",")
				.concat(DB_CONSTS.KEY_TEL1).concat(",")
				.concat(DB_CONSTS.KEY_IMG).concat(",")
				.concat(DB_CONSTS.KEY_DESC);
		final String query = "INSERT OR REPLACE INTO "
				.concat(DB_CONSTS.DATABASE_PLAYERS_TABLE).concat("  (")
				.concat(fields).concat(") VALUES (?,?,?,?,?,?,?,?);");
		try {
			mDB.execSQL(
					query,
					new String[] {
							p.getId(),
							(null != p.getBday()) ? p.getBday().toString() : "",
							p.getEmail(), p.getFname(), p.getLname(),
							p.getTel1(), p.getP_img(), p.getDescription() });
		} catch (SQLException se) {
			return false;
		}
		return true;
	}
}
