package com.gl.logcat.listeners;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.catlogttest.R;
import com.gl.logcat.activities.LogcatViewActivity;
import com.gl.logcat.fragments.SavedLogsFragment;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MyMultiChoiceModeListener implements AbsListView.MultiChoiceModeListener
{
    private  List<String> list;
    LogcatViewActivity host;
    private SavedLogsFragment savedLogsFragment;
    ActionMode activeMode;
    ListView lv;
    String TAG = "AcrionModeTest";
    private View selectView;

    public MyMultiChoiceModeListener(LogcatViewActivity host, ListView lv, SavedLogsFragment savedLogsFragment)
    {
	this.host = host;
	this.lv = lv;
	this.savedLogsFragment = savedLogsFragment;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu)
    {
	MenuInflater inflater = host.getMenuInflater();
	inflater.inflate(R.menu.context, menu);
	LayoutInflater layoutInflater = (LayoutInflater) host.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	selectView = (View) layoutInflater.inflate(R.layout.action_mode_custom_view, null);
	mode.setCustomView(selectView);
	activeMode = mode;
	Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu)
    {
	Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	return (false);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item)
    {
	Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	updateSubtitle(mode);
	savedLogsFragment.performActions(item);
	if(item.getItemId() == R.id.select_all)
	{
	    ((TextView)activeMode.getCustomView().findViewById(R.id.selected_text)).setText(lv.getCount() +" Selected");
	}

	return (true);
    }

    @Override
    public void onDestroyActionMode(ActionMode mode)
    {
	Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");

	savedLogsFragment.uncheckItems();
	activeMode = null;

    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked)
    {
	Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	 updateSubtitle(activeMode);

    }

    private void updateSubtitle(ActionMode mode)
    {
	Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	try
	{
	    activeMode.setSubtitle("(" + lv.getCheckedItemCount() + ")");
	    ((TextView)activeMode.getCustomView().findViewById(R.id.selected_text)).setText(lv.getCheckedItemCount() +" Selected");
	}
	catch (NullPointerException exception)
	{
	    exception.printStackTrace();

	}

    }

    public void checkBoxChecked(int position, boolean isChecked)
    {
	Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called." + isChecked);
	lv.performItemClick((RelativeLayout) lv.getChildAt(0), position, 0);
	// updateSubtitle(mode);

    }
    
    public void closeActionMode()
    {
	activeMode.finish();
    }
}
