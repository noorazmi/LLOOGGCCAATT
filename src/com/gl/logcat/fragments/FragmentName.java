package com.gl.logcat.fragments;

public enum FragmentName {
  
    
    CURRENTLOGS_FRAGMENT(LogcatViewFragment.class.getSimpleName()), SAVEDLOGS_FRAGMENT(SavedLogsFragment.class.getSimpleName());
    
    private String framentName;
    
    private FragmentName(String fragmentName)
    {
	this.framentName = fragmentName;
    }
    
    
    public String getFragmentName()
    {
	return framentName;
    }
    
}
