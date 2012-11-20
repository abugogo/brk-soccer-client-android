package com.soccer.indoorstats.utils.log;

import android.util.Log;

public class Logger {
	private static String key = "soccer_stats_key";
	
	public static void w(String msg) {
		Log.w(key, msg);
	}
	
	public static void w(Throwable tr) {
		Log.w(key, tr);
	}
	
	public static void i(String msg) {
		Log.i(key, msg);
	}
	
	public static void e(String msg, Throwable tr) {
		Log.e(key, msg, tr);
	}
}
