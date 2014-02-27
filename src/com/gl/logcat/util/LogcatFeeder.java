package com.gl.logcat.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.Log;

public abstract class LogcatFeeder extends Thread {

	public static final String[] LOGCAT_CMD = new String[] { "logcat", "-v", "time" };
	private static final int BUFFER_SIZE = 1024;
	private int lines = 0;
	protected Process process = null;

	@Override
	public void run() {
		Log.d(ModuleConstants.TAG, "run called");
		try {
			process = Runtime.getRuntime().exec(LOGCAT_CMD);
		} catch (IOException e) {
			onError("Can't start " + LOGCAT_CMD[0], e);
			Log.d(ModuleConstants.TAG, "Can't start" + e.getMessage());
			return;

		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()), BUFFER_SIZE);
			String line;
			while ((line = reader.readLine()) != null) {
				onNewLogLine(line);
			}
		} catch (Exception e) {
			onError("Error reading from process " + LOGCAT_CMD[0], e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void stopLogcatFeeder() {
		if (process != null) {
			process.destroy();
			process = null;
		}
	}

	public abstract void onError(String errorMsg, Throwable throwable);

	public abstract void onNewLogLine(String line);

}
