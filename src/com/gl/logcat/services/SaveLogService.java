package com.gl.logcat.services;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;

import com.gl.logcat.provider.LogsProviderContract;
import com.gl.logcat.util.ModuleConstants;
import com.gl.logcat.util.Utility;

public class SaveLogService extends IntentService
{
    public static final String SAVE_LOG_SERVICE = SaveLogService.class.getSimpleName();
    
    public SaveLogService()
    {
	super(SAVE_LOG_SERVICE);
	Utility.printLog(SAVE_LOG_SERVICE, "SaveLogService constructor called");
    }


    @Override
    protected void onHandleIntent(Intent intent)
    {
	Utility.printLog(SAVE_LOG_SERVICE, "SaveLogService called");
	
	Bundle bundle = intent.getExtras();
	ArrayList<CharSequence> logsArrayList = bundle.getCharSequenceArrayList(ModuleConstants.LOG_DATA);
	
	ContentValues contentValues = new ContentValues();
	contentValues.put(LogsProviderContract.Logs.COLUMN_NAME_TITLE, bundle.getString(ModuleConstants.FILE_NAME));
	contentValues.put(LogsProviderContract.Logs.COLUMN_NAME_LOG, Utility.getLogAsString(logsArrayList));
	
	ContentResolver contentResolver = getContentResolver();
	contentResolver.insert(LogsProviderContract.Logs.CONTENT_URI, contentValues);
    }


    @Override
    public void onCreate()
    {
	super.onCreate();
	Utility.printLog(SAVE_LOG_SERVICE, "onCreate() called");
    }


    @Override
    public void onDestroy()
    {
	super.onDestroy();
	Utility.printLog(SAVE_LOG_SERVICE, "onDestroy() called");
    }
    
    

}
