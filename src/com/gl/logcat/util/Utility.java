package com.gl.logcat.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

public final class Utility
{
    public static final String TAG = Utility.class.getCanonicalName();
    public static int resId_scrollView = -1;
    public static int resId_logs_container = -1;
    public static int resId_logcatview_fragment = -1;
    public static int resId_logcat_list_item = -1;
    public static int resId_logcat_view_activity = -1;
    public static int resId_drawer_layout = -1;
    public static int resId_drawer_list = -1;
    public static int resId_ic_drawer = -1;
    public static int resId_drawer_options = -1;
    public static int resId_drawer_list_item = -1;
    public static int resId_ic_menu_search = -1;

    // Resources used in LogLineViewWrapper.java class
    public static int resId_pid_text = -1;
    public static int resId_timestamp_text = -1;
    public static int resId_tag_text = -1;
    public static int resId_log_level_text = -1;
    public static int resId_log_output_text = -1;

    // Colors
    public static int resId_background_debug = -1;
    public static int resId_background_error = -1;
    public static int resId_background_info = -1;
    public static int resId_background_verbose = -1;
    public static int resId_background_warn = -1;
    public static int resId_background_wtf = -1;

    public static int resId_foreground_debug = -1;
    public static int resId_foreground_error = -1;
    public static int resId_foreground_info = -1;
    public static int resId_foreground_verbose = -1;
    public static int resId_foreground_warn = -1;
    public static int resId_foreground_wtf = -1;
    public static int resId_logcat_view_fragment_menu = -1;
    public static int resId_action_search = -1;
    public static int resId_action_filter = -1;
    public static int resId_action_filter_layout = -1;

    public static int resId_logcat_view_activity_menu = -1;
    public static int resId_content_frame = -1;
    public static int resId_drawer_open = -1;
    public static int resId_drawer_close = -1;
    public static int resId_drawer_shadow = -1;

    private Utility()
    {}

    public static void loadResourceIds(Context context)
    {
	String packageName = context.getPackageName();
	Resources resources = context.getResources();

	resId_scrollView = resources.getIdentifier("scrollView", "id", packageName);
	resId_logs_container = resources.getIdentifier("logs_container", "id", packageName);
	resId_logcatview_fragment = resources.getIdentifier("logcatview_fragment", "layout", packageName);
	resId_logcat_list_item = resources.getIdentifier("logcat_list_item", "layout", packageName);
	resId_logcat_view_activity = resources.getIdentifier("logcat_view_activity", "layout", packageName);
	// Resources used in LogLineViewWrapper.java class
	resId_pid_text = resources.getIdentifier("pid_text", "id", packageName);
	resId_timestamp_text = resources.getIdentifier("timestamp_text", "id", packageName);
	resId_tag_text = resources.getIdentifier("tag_text", "id", packageName);
	resId_log_level_text = resources.getIdentifier("log_level_text", "id", packageName);
	resId_log_output_text = resources.getIdentifier("log_output_text", "id", packageName);
	resId_drawer_layout = resources.getIdentifier("drawer_layout", "id", packageName);
	resId_drawer_list = resources.getIdentifier("drawer_list", "id", packageName);

	// Colors
	resId_background_debug = resources.getIdentifier("background_debug", "color", packageName);
	resId_background_error = resources.getIdentifier("background_error", "color", packageName);
	resId_background_info = resources.getIdentifier("background_info", "color", packageName);
	resId_background_verbose = resources.getIdentifier("background_verbose", "color", packageName);
	resId_background_warn = resources.getIdentifier("background_warn", "color", packageName);
	resId_background_wtf = resources.getIdentifier("background_wtf", "color", packageName);

	resId_foreground_debug = resources.getIdentifier("foreground_debug", "color", packageName);
	resId_foreground_error = resources.getIdentifier("foreground_error", "color", packageName);
	resId_foreground_info = resources.getIdentifier("foreground_info", "color", packageName);
	resId_foreground_verbose = resources.getIdentifier("foreground_verbose", "color", packageName);
	resId_foreground_warn = resources.getIdentifier("foreground_warn", "color", packageName);
	resId_foreground_wtf = resources.getIdentifier("foreground_wtf", "color", packageName);

	resId_logcat_view_activity_menu = resources.getIdentifier("logcat_view_activity_menu", "menu", packageName);
	resId_logcat_view_fragment_menu = resources.getIdentifier("logcat_view_fragment_menu", "menu", packageName);
	resId_action_search = resources.getIdentifier("action_search", "id", packageName);
	resId_content_frame = resources.getIdentifier("content_frame", "id", packageName);
	resId_action_filter = resources.getIdentifier("action_filter", "id", packageName);

	resId_drawer_open = resources.getIdentifier("drawer_open", "string", packageName);
	resId_drawer_close = resources.getIdentifier("drawer_close", "string", packageName);
	resId_ic_drawer = resources.getIdentifier("ic_drawer", "drawable", packageName);
	resId_drawer_shadow = resources.getIdentifier("drawer_shadow", "drawable", packageName);
	resId_drawer_options = resources.getIdentifier("drawer_options", "string-array", packageName);

	resId_drawer_list_item = resources.getIdentifier("drawer_list_item", "layout", packageName);
	resId_ic_menu_search = resources.getIdentifier("ic_menu_search", "drawable", packageName);
	resId_action_filter_layout = resources.getIdentifier("action_filter_layout", "layout", packageName);

	// resId_ = resources.getIdentifier("","color", packageName);
    }

    public static void printLog(String tag, String message)
    {
	Log.d(ModuleConstants.TAG, "[ " + tag + " ]:" + message);
    }

    public static void showToast(Context context, String message)
    {
	Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isValidFileName(String fileName)
    {

	if (TextUtils.isEmpty(fileName))
	{
	    return false;
	}

	for (int i = 0; i < ModuleConstants.INVALID_FILENAME_CHARACTERS.length; i++)
	{
	    if (fileName.indexOf(ModuleConstants.INVALID_FILENAME_CHARACTERS[i]) != -1)
	    {
		return false;
	    }
	}
	return true;
    }

    public static String getFormatedDate(DatePattern fileNameDateFormat)
    {
	return DateFormat.format(fileNameDateFormat.getFormat(), new Date()).toString();
    }

    public static final String getLogAsString(ArrayList<CharSequence> logsArrayList)
    {
	StringBuilder builder = new StringBuilder();

	for (CharSequence charSequence : logsArrayList)
	{
	    builder.append(charSequence);
	}

	return builder.toString();
    }
    
    /** Checks if external storage is available for both read and write */
    public static boolean isExternalStorageWritable()
    {
	String state = Environment.getExternalStorageState();
	return Environment.MEDIA_MOUNTED.equals(state);
    }
    
    
    /** Checks if external storage is available for at least read */
    public static boolean isExternalStorageReadable()
    {
	String state = Environment.getExternalStorageState();
	return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }
    

}
