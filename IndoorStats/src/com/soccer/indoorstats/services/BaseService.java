package com.soccer.indoorstats.services;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.soccer.db.local.DbAdapterBase;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.preferences.Prefs;

public abstract class BaseService extends Service {
	protected Prefs sharedPrefs;
	private static DbAdapterBase mDbHelper = null;
	private SQLiteDatabase mDb = null;

	public SQLiteDatabase openDB() {
		if (mDb == null || !mDb.isOpen())
			mDb = mDbHelper.getWritableDatabase();
		return mDb;
	}

	public void closeDB() {
		if (mDb != null && mDb.isOpen())
			mDb.close();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Logger.i("Received start id " + startId + ": " + intent);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		sharedPrefs = new Prefs(this);
		synchronized (DbAdapterBase.class) {
			if (mDbHelper == null)
				mDbHelper = new DbAdapterBase(this);
		}
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		if (mDbHelper != null)
			mDbHelper.close();
		super.onDestroy();
	}
}
