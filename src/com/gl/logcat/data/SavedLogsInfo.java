package com.gl.logcat.data;

public class SavedLogsInfo
{
    private int id;
    private String fileName;
    private String dateSaved;
    private String log;
    
    public SavedLogsInfo(int id,String fileName,String dateSaved,String log)
    {
	this.id = id;
	this.fileName = fileName;
	this.dateSaved = dateSaved;
	this.log = log;
    }

    public int getId()
    {
        return id;
    }

    public String getFileName()
    {
        return fileName;
    }

    public String getDateSaved()
    {
        return dateSaved;
    }

    public String getLog()
    {
        return log;
    }
    
    

}
