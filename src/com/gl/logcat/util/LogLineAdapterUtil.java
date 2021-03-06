package com.gl.logcat.util;

import android.content.Context;
import android.util.Log;

public class LogLineAdapterUtil {

	public static final int LOG_WTF = 100; // arbitrary int to signify 'wtf' log
	// level

	private static final int NUM_COLORS = 17;

	public static int getBackgroundColorForLogLevel(Context context,
			int logLevel) {
		int result = android.R.color.black;
		switch (logLevel) {
		case Log.DEBUG:
			result = Utility.resId_background_debug;
			break;
		case Log.ERROR:
			result = Utility.resId_background_error;
			break;
		case Log.INFO:
			result = Utility.resId_background_info;
			break;
		case Log.VERBOSE:
			result = Utility.resId_background_verbose;
			break;
		case Log.WARN:
			result = Utility.resId_background_warn;
			break;
		case LOG_WTF:
			result = Utility.resId_background_wtf;
			break;
		}

		return context.getResources().getColor(result);
	}

	public static int getForegroundColorForLogLevel(Context context,
			int logLevel) {
		int result = android.R.color.primary_text_dark;
		switch (logLevel) {
		case Log.DEBUG:
			result = Utility.resId_foreground_debug;// R.color.foreground_debug;
			break;
		case Log.ERROR:
			result = Utility.resId_foreground_error;// R.color.foreground_error;
			break;
		case Log.INFO:
			result = Utility.resId_foreground_info;// R.color.foreground_info;
			break;
		case Log.VERBOSE:
			result = Utility.resId_foreground_verbose;// R.color.foreground_verbose;
			break;
		case Log.WARN:
			result = Utility.resId_foreground_warn;// R.color.foreground_warn;
			break;
		case LOG_WTF:
			result = Utility.resId_foreground_wtf;// R.color.foreground_wtf;
			break;
		}
		return context.getResources().getColor(result);
	}

	// public static synchronized int getOrCreateTagColor(Context context,
	// String tag)
	// {
	//
	// int hashCode = (tag == null) ? 0 : tag.hashCode();
	//
	// int smear = Math.abs(hashCode) % NUM_COLORS;
	//
	// return getColorAt(smear, context);
	//
	// }

	// private static int getColorAt(int i, Context context)
	// {
	//
	// ColorScheme colorScheme = PreferenceHelper.getColorScheme(context);
	//
	// int[] colorArray = colorScheme.getTagColors(context);
	//
	// return colorArray[i];
	//
	// }

	public static boolean logLevelIsAcceptableGivenLogLevelLimit(int logLevel,
			int logLevelLimit) {

		int minVal = 0;
		switch (logLevel) {

		case Log.VERBOSE:
			minVal = 0;
			break;
		case Log.DEBUG:
			minVal = 1;
			break;
		case Log.INFO:
			minVal = 2;
			break;
		case Log.WARN:
			minVal = 3;
			break;
		case Log.ERROR:
			minVal = 4;
			break;
		case LOG_WTF:
			minVal = 5;
			break;
		default: // e.g. the starting line that says
			// "output of log such-and-such"
			return true;
		}

		return minVal >= logLevelLimit;

	}
}
