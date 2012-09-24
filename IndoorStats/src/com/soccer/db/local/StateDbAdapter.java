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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

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

	public long createState(String s) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_GAME_STATE, s);

		return mDb.insert(DATABASE_STATE_TABLE, null, initialValues);
	}

	public boolean deleteState() {

		return mDb.delete(DATABASE_STATE_TABLE, KEY_ID + "=" + 0, null) > 0;
	}

	public Cursor fetchState() throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_STATE_TABLE, new String[] { "_id", KEY_ID,
				KEY_GAME_STATE }, KEY_ID + "=" + 0, null, null, null, null,
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public boolean updateState(String s) {
		ContentValues args = new ContentValues();

		args.put(KEY_GAME_STATE, s);

		return mDb.update(DATABASE_STATE_TABLE, args, KEY_ID + "=" + 0, null) > 0;
	}
}
