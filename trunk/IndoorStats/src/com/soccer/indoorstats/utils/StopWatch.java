package com.soccer.indoorstats.utils;

public class StopWatch {

	private long startTime = 0;
	private long stopTime = 0;
	private boolean running = false;

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setStopTime(long stopTime) {
		this.stopTime = stopTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getStopTime() {
		return stopTime;
	}

	public boolean isRunning() {
		return running;
	}

	public void start(boolean reset) {
		long s = System.currentTimeMillis();
		if (!reset)
			s = this.startTime + s - this.stopTime;
		this.startTime = s;
		this.running = true;
	}

	public void stop() {
		this.stopTime = System.currentTimeMillis();
		this.running = false;
	}

	// elaspsed time in milliseconds
	public long getElapsedTime() {
		long elapsed = 0;
		if (running) {
			elapsed = (System.currentTimeMillis() - startTime);
		}
		// else {
		// elapsed = (stopTime - startTime);
		// }
		return elapsed;
	}

	// elaspsed time in seconds
	public long getElapsedTimeSecs() {
		long elapsed = 0;
		if (running) {
			elapsed = ((System.currentTimeMillis() - startTime) / 1000);
		}
		else {
			elapsed = ((stopTime - startTime) / 1000);
		}
		// else {
		// elapsed = ((stopTime - startTime) / 1000);
		// }
		return elapsed;
	}
}