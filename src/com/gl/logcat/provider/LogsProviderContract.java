package com.gl.logcat.provider;


import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines a contract between the SavedLogsProvider content provider and its
 * clients. A contract defines the information that a client needs to access the
 * provider as one or more data tables. A contract is a public, non-extendable
 * (final) class that contains constants defining column names and URIs. A
 * well-written client depends only on the constants in the contract.
 */

public final class LogsProviderContract
{
    public static final String AUTHORITY = "com.gl.provider.SavedLogsProvider";
    
    
    // This class can not be instantiated.
    private LogsProviderContract()
    {}

    public static final class Logs implements BaseColumns
    {
	// This class can not be instantiated.
	private Logs()
	{}
	
	/** Table name offered by this provider. */
	public static final String TABLE_NAME = "logs";
	
	/*
	 * URI definitions 
	 */
	
	/**
	 * The scheme part of this provider.
	 */
	private static final String SCHEME = "content://";
	
	
	/*
	 * Path parts of the URIs
	 */
	
	/**
	 * Path part for the Logs URI 
	 */
	
	private static final String PATH_LOGS = "/logs";
	
	 /**
         * Path part for the Note ID URI
         */
        private static final String PATH_LOG_ID = "/logs/";
	
        /**
         * The content URI base for a single log. Callers must
         * append a numeric log id to this Uri to retrieve a log.
         */
        public static final Uri CONTENT_ID_URI_BASE
            = Uri.parse(SCHEME + AUTHORITY + PATH_LOG_ID);
        
	
	/**
	 * The content:// style URL for this table
	 */
	
	public static final Uri CONTENT_URI = Uri.parse(SCHEME+AUTHORITY+PATH_LOGS); 
	
	
	 /*
         * MIME type definitions
         */
	

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of logs.
         */
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.logcat.log";
	
	 /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
         * log.
         */
	
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.logcat.log";
	
	
	 /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "modified DESC";
        
        
        /*
         * Column Definitions
         */
        
        /**
         * Column name for the title of the log <P>Type: Text</P>
         */
        
	public static final String COLUMN_NAME_TITLE = "title";
	
	/**
         * Column name for the log content <P>Type: Text</P>
         */
        
	public static final String COLUMN_NAME_LOG = "log";
	
	
	/**
	 * Column name for the log creation date.
	 */
	
	public static final String COLUMN_NAME_CREATE_DATE = "created";
	
	
    }
}
