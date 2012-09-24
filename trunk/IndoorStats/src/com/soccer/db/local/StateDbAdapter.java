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

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;

public class StateDbAdapter extends DbAdapterBase {

	public StateDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public StateDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createState(ByteArrayOutputStream bos) {
		SQLiteStatement p = mDb.compileStatement("insert into " + DATABASE_STATE_TABLE + "(" + KEY_ID + "," + 
				KEY_GAME_STATE + ") values(0, ?)");

		p.bindBlob(1, bos.toByteArray());
		long retVal =p.executeInsert(); 
		return retVal;
	}

	public boolean deleteAllStates() {

		return mDb.delete(DATABASE_STATE_TABLE, null, null) > 0;
	}

	public Cursor fetchState() throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_STATE_TABLE, new String[] { "_id", KEY_ID,
				KEY_GAME_STATE }, KEY_ID + "=0", null, null, null, null,
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public boolean insertOrUpdateState(ByteArrayOutputStream bos) {
		SQLiteStatement p = mDb.compileStatement("insert or replace into " + DATABASE_STATE_TABLE + " (_id, " + KEY_ID + "," + 
				KEY_GAME_STATE + ") values(0, 0, ?)");

		p.bindBlob(1, bos.toByteArray());
		long retVal =p.executeInsert(); 
		return (retVal != -1);
	}
}
