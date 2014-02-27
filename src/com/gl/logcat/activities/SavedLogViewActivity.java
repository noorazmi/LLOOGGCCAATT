package com.gl.logcat.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.gl.logcat.fragments.LogEditorFragment;
import com.gl.logcat.util.Utility;


public class SavedLogViewActivity extends ActionBarActivity
{
    private static final String TAG = SavedLogViewActivity.class.getCanonicalName();
    
    
    /********************************************************************************************************
     * Activity Life Cycle Methods
     *********************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
//	if (android.os.Build.VERSION.SDK_INT >= 14)
//	{
//	    setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
//	}
//	else if (android.os.Build.VERSION.SDK_INT >= 11)
//	{
//	    setTheme(android.R.style.Theme_Holo_Light);
//	}
//	else if (android.os.Build.VERSION.SDK_INT <= 10)
//	{
//	    setTheme(android.R.style.Theme_Light);
//	}
	
        super.onCreate(savedInstanceState);
        
        
        Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName()+"() called.");
        
        //Enable home up button.
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        if(savedInstanceState != null)
        {
            return;
        }
        
       
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();
        Fragment fragment = new LogEditorFragment();
        fragmentTransaction.add(android.R.id.content, fragment);
        //fragmentTransaction.addToBackStack(SavedLogViewActivity.class.getSimpleName());
        fragmentTransaction.commit();
        
    }
    
    
    @Override
    protected void onStart()
    {
	super.onStart();
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName()+"() called.");
    }
    
    @Override
    protected void onRestart()
    {
        super.onRestart();
        Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName()+"() called.");
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName()+"() called.");
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName()+"() called.");
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName()+"() called.");
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName()+"() called.");
    }
    
    
    
}
