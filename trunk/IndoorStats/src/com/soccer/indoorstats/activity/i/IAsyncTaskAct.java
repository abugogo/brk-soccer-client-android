package com.soccer.indoorstats.activity.i;

import android.content.Context;

public interface IAsyncTaskAct {
	public void onSuccess(String result);
	public void onFailure(int responseCode, String result);
	public void onProgress();
	public Context getAppContext();
}
