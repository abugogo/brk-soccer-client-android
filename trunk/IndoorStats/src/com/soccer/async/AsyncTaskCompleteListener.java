package com.soccer.async;

public interface AsyncTaskCompleteListener<T> {
	public void onTaskComplete(T result);

}
