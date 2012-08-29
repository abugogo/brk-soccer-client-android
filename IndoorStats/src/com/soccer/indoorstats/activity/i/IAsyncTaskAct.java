package com.soccer.indoorstats.activity.i;

public interface IAsyncTaskAct {
	public void onSuccess(String result);
	public void onFailure(int responseCode, String result);
	public void onProgress();
}
