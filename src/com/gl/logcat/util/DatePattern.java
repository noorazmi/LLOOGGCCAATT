package com.gl.logcat.util;

public enum DatePattern {
    
    DEFAULT_DATE_PATTERN("MMMM dd, yyyy h:mm:ssaa"),
    FILE_NAME_DATE_PATTERN("MMMddyyyyhhmmssaa");
    
    private CharSequence dateFormat;

    private DatePattern(String dateFromat)
    {
	this.dateFormat = dateFromat;
    }
    
    public CharSequence getFormat()
    {
	return dateFormat;
    }

}
