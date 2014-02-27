package com.gl.logcat.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import com.gl.logcat.util.DatePattern;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Filter;
import android.widget.Filter.FilterListener;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.catlogttest.R;
import com.gl.logcat.adapter.LogcatLinesAdapter;
import com.gl.logcat.adapter.LogcatLinesAdapter.LogLineAddedListener;
import com.gl.logcat.data.LogLine;
import com.gl.logcat.dialogs.DialogInfoOK;
import com.gl.logcat.dialogs.DialogOkButtonClickListener;
import com.gl.logcat.dialogs.SingleInputDismissOkDialog;
import com.gl.logcat.util.LogcatFeeder;
import com.gl.logcat.util.ModuleConstants;
import com.gl.logcat.util.Utility;

public class LogcatViewFragment extends ListFragment implements FilterListener, OnScrollListener, LogLineAddedListener
{
    private static final int MSG_NEWLINE = 1;
    private LogcatFeeder mLogcatFeeder;
    private static final int MAX_LINES = 250;
    private LogcatLinesAdapter logcatLinesAdapter;
    private SearchView mSearchView;
    private CharSequence searchString = "";
    private boolean autoscrollToBottom = true;
    private boolean isScrolling = true;
    private static final String TAG = LogcatViewFragment.class.getCanonicalName();
    private ActionsBroadcastReceiver actionsBroadcastReceiver;
    private AlertDialog alertDialog;
    private final Handler mHandler = new Handler()
    {
	@Override
	public void handleMessage(Message msg)
	{
	    
	    String aResponse = msg.getData().getString("message");
	    if ((aResponse != null))
	    {

		// processHandlerMessage(msg);
		LogLine logLine = LogLine.newLogLine(aResponse, !false);
		logcatLinesAdapter.addWithFilter(logLine, searchString);
	    }
	    else
	    {

		Toast.makeText(getActivity(), "Not Got Response From Server.", Toast.LENGTH_SHORT).show();
	    }
	}

    };
   

    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

	
	return inflater.inflate(Utility.resId_logcatview_fragment, null, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
	inflater.inflate(R.menu.logcat_view_activity_menu, menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }
    
    private void setUpAdapter()
    {

	logcatLinesAdapter = new LogcatLinesAdapter(getActivity(), Utility.resId_logcat_list_item, new ArrayList<LogLine>(), this);
	setListAdapter(logcatLinesAdapter);
	getListView().setFastScrollEnabled(true);
	getListView().setFastScrollAlwaysVisible(false);
	getListView().setOnScrollListener(this);
    }

    @Override
    public void onStart()
    {
	super.onStart();
	setUpAdapter();
	mLogcatFeeder = new LogcatFeeder()
	{

	    @Override
	    public void onNewLogLine(String line)
	    {

		Message msg = mHandler.obtainMessage();
		Bundle b = new Bundle();
		b.putString("message", line);
		msg.setData(b);
		mHandler.sendMessage(msg);
	    }

	    @Override
	    public void onError(String errorMsg, Throwable throwable)
	    {

	    }
	};

	mLogcatFeeder.start();

    };

    @Override
    public void onDestroy()
    {
	mLogcatFeeder.stopLogcatFeeder();
	super.onDestroy();
    }

    public void onSearchTextChanged(String newText)
    {
	// Toast.makeText(getActivity(), newText, 100).show();
	CharSequence filterText = newText;
	searchString = newText;
	Log.d("filtering: %s", (String) filterText);
	
	filter(filterText);

    }

    public void filter(CharSequence filterText)
    {

//	Filter filter = logcatLinesAdapter.getFilter();
//	if (filter == null)
//	{
//	    Toast.makeText(getActivity(), "logcatLinesAdapter in not initialized", Toast.LENGTH_SHORT).show();
//	    return;
//	}
//
//	filter.filter(filterText, this);

    }

    @Override
    public void onFilterComplete(int count)
    {
	Log.d("onFilterComplete", "onFilterComplete");

	// always scroll to the bottom when searching
	try
	{
	    // if (autoscrollToBottom)
	    // {
	    // getListView().setSelection(count);
	    //
	    // // if the list showing the bottom item of the list then let the
	    // // list auto scroll self and disable the auto scrolling
	    // getListView().setFastScrollEnabled(false);
	    // }
	    // else
	    // {
	    // // If user scrolled the list up then disable the auto scroll and
	    // // let the user scroll the list
	    // getListView().setFastScrollEnabled(true);
	    // }
	    getListView().setSelection(count);
	}
	catch (Throwable e)
	{
	    e.printStackTrace();
	}

    }

    public void pauseScrolling()
    {
	isScrolling = false;
    }

    public void startScrolling()
    {
	isScrolling = true;
	getListView().setSelection(getListView().getCount());
    }

    public boolean isScrolling(){
	return isScrolling;
    };
    
    public void clearLog(){
	logcatLinesAdapter.clearLogs();
    }
    
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
	// if the bottom of the list isn't visible anymore, then stop
	// autoscrolling

	autoscrollToBottom = (firstVisibleItem + visibleItemCount == totalItemCount);
	// getListView().setFastScrollEnabled(true);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {

    }

    @Override
    public void onLoglineAdded(int count)
    {
	if (autoscrollToBottom && isScrolling)
	{
	    getListView().setSelection(count);
	}

    }
    
    @Override
    public void onDestroyView()
    {
	super.onDestroyView();
	autoscrollToBottom = false;
    }

    public class ActionsBroadcastReceiver extends BroadcastReceiver
    {

	@Override
	public void onReceive(Context context, Intent intent)
	{
	    Utility.printLog(TAG, "ActionsBroadcastReceiver's on recieve called");
	    int action = intent.getIntExtra(ModuleConstants.MENU_ACTION, -99);
	    switch (action)
	    {
	    case ModuleConstants.SAVE_LOGS:
		openFileNameInputDialog();
		break;
	    case ModuleConstants.SHARE_LOGS:
		break;
	    
	    default:
		Utility.printLog(TAG, "No action specified.");
		break;
	    }
	    new Thread(){
		public void run() {
		    try
		    {
			Utility.printLog(TAG, "Start Time:"+Utility.getFormatedDate(DatePattern.DEFAULT_DATE_PATTERN));
			Thread.sleep(20000);
			Utility.printLog(TAG, "End Time:"+Utility.getFormatedDate(DatePattern.DEFAULT_DATE_PATTERN));
			
		    }
		    catch (InterruptedException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		};
	    }.start();
	    Utility.showToast(context, "ActionsBroadcastReceiver's on recieve called");
	}

    }
    
    
    private void openFileNameInputDialog()
    {
	
	final Resources res = getResources();
	//String initialFileName = Utility.getFormatedDate("MMMddyyyyhhmmssaa");
	String initialFileName = Utility.getFormatedDate(DatePattern.FILE_NAME_DATE_PATTERN); 
	alertDialog = SingleInputDismissOkDialog.getInstance(getActivity(), res.getString(R.string.enter_file_name), initialFileName, res.getString(R.string.ok), res.getString(R.string.cancel), new DialogOkListener());
	alertDialog.show();

    }
    
    private class DialogOkListener implements DialogOkButtonClickListener
    {
	final Resources res = getResources();
	@Override
	public void onDialogOkButtonClick(String fileName)
	{

		if(!Utility.isValidFileName(fileName))
		{
		    String message = "The characters \n"+Arrays.toString(ModuleConstants.INVALID_FILENAME_CHARACTERS).replace("[", "").replace("]", "").replace(",", " ")+"\ncan not be used in naming the file.";
		    if(TextUtils.isEmpty(fileName))
		    {
			message = res.getString(R.string.file_name_empty_message);
		    }
		    
		    new DialogInfoOK(getActivity(), res.getString(R.string.invalid_file_name), message ,res.getString(R.string.ok)).show();
		    return;
		}
	    alertDialog.dismiss();
	    logcatLinesAdapter.saveLogs(fileName);
	}
	
    }
    
    
    
    
    @Override
    public void onResume()
    {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
	intentFilter.addAction("com.gl.logcat.OVERFLOW_OPTION_CLICK_LISTENER");
	actionsBroadcastReceiver = new ActionsBroadcastReceiver();
	getActivity().registerReceiver(actionsBroadcastReceiver, intentFilter);
	Utility.printLog(TAG, "in onResume(): registerReceiver(actionsBroadcastReceiver, intentFilter) called.");
    }
    @Override
    public void onStop()
    {
	Utility.printLog(TAG, "in onStop(): unregisterReceiver(actionsBroadcastReceiver) called.");
	getActivity().unregisterReceiver(actionsBroadcastReceiver);
        super.onStop();
    }
    
}
