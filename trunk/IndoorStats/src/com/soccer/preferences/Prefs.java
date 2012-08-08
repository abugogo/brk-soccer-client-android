package com.soccer.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Prefs {

	Context myContext;

	public Prefs(Context ctx) {
		myContext = ctx;
	}

	/*
	 * Store a preference via key -> value
	 */
	public void setPreference(String key, String value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(myContext);

		SharedPreferences.Editor editor = prefs.edit();

		editor.putString(key, value);
		editor.commit(); // important! Don't forget!

	}

	public void setPreference(String key, int value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(myContext);

		SharedPreferences.Editor editor = prefs.edit();

		editor.putInt(key, value);
		editor.commit(); // important! Don't forget!

	}

	public void clearAllPreferences() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(myContext);

		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		editor.commit(); // important! Don't forget!

	}

	public String getPreference(String key, String def) {
		String val = "";
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(myContext);
		val = prefs.getString(key, def);

		return val;
	}

	public int getIntPreference(String key, int def) {
		int val = def;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(myContext);
		val = prefs.getInt(key, def);

		return val;
	}
}
