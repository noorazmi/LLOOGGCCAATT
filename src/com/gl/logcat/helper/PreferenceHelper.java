package com.gl.logcat.helper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelper
{
    public static Map<Integer, Integer> ellipsisLengthsCache = Collections.synchronizedMap(new HashMap<Integer, Integer>());
    private int foregroundColor = -1;
    private int foregroundColorResource;
    public int getForegroundColor(Context context)
    {
	if (foregroundColor == -1)
	{
	    foregroundColor = context.getResources().getColor(foregroundColorResource);
	}
	return foregroundColor;
    }
    
   
}
