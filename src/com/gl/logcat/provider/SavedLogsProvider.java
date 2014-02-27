package com.gl.logcat.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.gl.logcat.database.LogsSQLDataBase;
import com.gl.logcat.util.DatePattern;
import com.gl.logcat.util.Utility;

public class SavedLogsProvider extends ContentProvider
{
    private LogsSQLDataBase logsSQLDataBase;
    private static final String TAG = SavedLogsProvider.class.getSimpleName();
    private static final int LOGS = 1;
    private static final int LOG_ID = 2;

    private static final UriMatcher uriMatcher;

    static
    {
	uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	uriMatcher.addURI(LogsProviderContract.AUTHORITY, "logs", LOGS);
	uriMatcher.addURI(LogsProviderContract.AUTHORITY, "logs/#", LOG_ID);
    }

    public static HashMap<String, String> userProjectionMap;
    static
    {
	userProjectionMap = new HashMap<String, String>();
	userProjectionMap.put(LogsProviderContract.Logs._ID, LogsProviderContract.Logs._ID);
	userProjectionMap.put(LogsProviderContract.Logs.COLUMN_NAME_TITLE, LogsProviderContract.Logs.COLUMN_NAME_TITLE);
	userProjectionMap.put(LogsProviderContract.Logs.COLUMN_NAME_LOG, LogsProviderContract.Logs.COLUMN_NAME_LOG);
	userProjectionMap.put(LogsProviderContract.Logs.COLUMN_NAME_CREATE_DATE, LogsProviderContract.Logs.COLUMN_NAME_CREATE_DATE);
    }

    @Override
    public boolean onCreate()
    {
	// Creates a new helper object. Note that the database itself isn't
	// opened until
	// something tries to access it, and it's only created if it doesn't
	// already exist.
	logsSQLDataBase = new LogsSQLDataBase(getContext());

	Utility.printLog(TAG, "onCreate() called");

	// Assumes that any failures will be reported by a thrown exception.
	return logsSQLDataBase == null ? false : true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
	return 0;
    }

    /*
     * This is called when a client calls {@link
     * android.content.ContentResolver#getType(Uri)}. Returns the MIME data type
     * of the URI given as a parameter.
     * 
     * @param uri The URI whose MIME type is desired.
     * 
     * @return The MIME type of the URI.
     * 
     * @throws IllegalArgumentException if the incoming URI pattern is invalid.
     */

    @Override
    public String getType(Uri uri)
    {
	/**
	 * Chooses the MIME type based on the incoming URI pattern
	 */
	switch (uriMatcher.match(uri))
	{

	// If the pattern is for logs or live folders, returns the general
	// content type.
	case LOGS:
	    return LogsProviderContract.Logs.CONTENT_TYPE;

	    // If the pattern is for log IDs, returns the log ID content type.
	case LOG_ID:
	    return LogsProviderContract.Logs.CONTENT_ITEM_TYPE;

	default:
	    throw new IllegalArgumentException("Unknown URI: " + uri);
	}
    }

    @Override
    public Uri insert(Uri uri, ContentValues suppliedValues)
    {
	// Validates the incoming URI. Only the full provider URI is allowed for
	// inserts.
	if (uriMatcher.match(uri) != LOGS)
	{
	    throw new IllegalArgumentException("Unknown URI " + uri);
	}

	// A map to hold the new record's values.
	ContentValues values;

	// If the incoming values map is not null, uses it for the new values.
	if (suppliedValues != null)
	{
	    values = new ContentValues(suppliedValues);
	}
	else
	{
	    // If the incoming values map is not null, uses it for the new
	    // values.
	    values = new ContentValues();
	}

	// If the values map does not contain the creation date, sets the value
	// to the current time.
	if (values.containsKey(LogsProviderContract.Logs.COLUMN_NAME_CREATE_DATE) == false)
	{
	    values.put(LogsProviderContract.Logs.COLUMN_NAME_CREATE_DATE, Utility.getFormatedDate(DatePattern.DEFAULT_DATE_PATTERN));
	}

	// Opens the database object in "write" mode.
	SQLiteDatabase db = logsSQLDataBase.getWritableDatabase();

	// Perform the insert and returns the ID of the new log.
	long rowId = db.insert(LogsProviderContract.Logs.TABLE_NAME, LogsProviderContract.Logs.COLUMN_NAME_LOG, values);

	if (rowId > 0)
	{
	    // Creates a URI with the log ID pattern and the new row ID appended
	    // to it.
	    Uri logUri = ContentUris.withAppendedId(LogsProviderContract.Logs.CONTENT_ID_URI_BASE, rowId);

	    // Notifies observers registered against this provider that the data
	    // changed.
	    getContext().getContentResolver().notifyChange(logUri, null);
	    return logUri;
	}

	// If the insert didn't succeed, then the rowID is <= 0 ie -1. Throws an
	// exception.
	throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
	SQLiteDatabase db = logsSQLDataBase.getReadableDatabase();
	SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	queryBuilder.setTables(LogsProviderContract.Logs.TABLE_NAME);

	switch (uriMatcher.match(uri))
	{
	case LOGS:
	    queryBuilder.setProjectionMap(userProjectionMap);
	    break;
	case LOG_ID:
	    queryBuilder.appendWhere(LogsProviderContract.Logs._ID + "==" + uri.getPathSegments().get(1));
	    break;

	default:
	    throw new IllegalArgumentException("Unknown Uri : " + uri);
	}

	if (sortOrder == null || sortOrder == "")
	{
	    sortOrder = LogsProviderContract.Logs.COLUMN_NAME_TITLE;
	}

	Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

	return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
	return 0;
    }

}
