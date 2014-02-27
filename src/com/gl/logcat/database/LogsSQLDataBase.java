package com.gl.logcat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gl.logcat.provider.LogsProviderContract;
import com.gl.logcat.util.Utility;

public class LogsSQLDataBase extends SQLiteOpenHelper
{
    private static final String TAG = LogsSQLDataBase.class.getCanonicalName();
    
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "logs_database.db";

    public LogsSQLDataBase(Context context)
    {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	Utility.printLog(TAG, "Constructor Called");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
	db.execSQL("CREATE TABLE "
		+ LogsProviderContract.Logs.TABLE_NAME
		+ " ("
		+ LogsProviderContract.Logs._ID+ " INTEGER PRIMARY KEY, "
		+ LogsProviderContract.Logs.COLUMN_NAME_TITLE+" TEXT, "
		+ LogsProviderContract.Logs.COLUMN_NAME_LOG+" TEXT, "
		+ LogsProviderContract.Logs.COLUMN_NAME_CREATE_DATE+" TEXT"
		+ " );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {	
	 // Logs that the database is being upgraded
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
	
	// Kills the table along with the existing data.
	db.execSQL("DROP TABLE IF EXIST "+LogsProviderContract.Logs.TABLE_NAME);
	
	// Recreate the table with a new version.
	onCreate(db);
    }
    
    @Override
    public void onOpen(SQLiteDatabase db)
    {
        Utility.printLog(TAG, "oponOpen() called");
    }

}
