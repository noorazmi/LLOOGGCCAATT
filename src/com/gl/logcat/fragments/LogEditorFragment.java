package com.gl.logcat.fragments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.catlogttest.R;
import com.gl.logcat.dialogs.DialogInfoOK;
import com.gl.logcat.provider.LogsProviderContract;
import com.gl.logcat.util.Utility;

public class LogEditorFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{

    private static final String TAG = LogEditorFragment.class.getCanonicalName();
    private static final int LOG_EDITOR_FRAGMENT_LOADER = 100;
    private static final String URI = "uri";
    private EditText editor;
    private static final String SUBJECT = "Log Details:";
    private ShareActionProvider shareActionProvider;
    private Resources res;
    private ExecutorService executorService;

    /************************************************************************************************************************************
     * Fragment Life cycle methods
     *************************************************************************************************************************************/
    @Override
    public void onAttach(Activity activity)
    {
	super.onAttach(activity);
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	res = activity.getResources();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setHasOptionsMenu(true);
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	View view = inflater.inflate(R.layout.log_editor, null);
	editor = (EditText) view.findViewById(R.id.log_editor);
	return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart()
    {
	super.onStart();
	LoaderManager.enableDebugLogging(true);
	Bundle data = new Bundle();
	data.putString(URI, getActivity().getIntent().getData().toString());
	getLoaderManager().initLoader(LOG_EDITOR_FRAGMENT_LOADER, data/*
								       * This
								       * will be
								       * received
								       * in
								       * onCreateLoader
								       * ()
								       * method
								       */, this);
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
    }

    @Override
    public void onResume()
    {
	super.onResume();
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
    }

    @Override
    public void onPause()
    {
	super.onPause();
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
    }

    @Override
    public void onStop()
    {
	super.onStop();
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
    }

    @Override
    public void onDestroy()
    {
	super.onDestroy();
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
    }

    @Override
    public void onDetach()
    {
	super.onDetach();
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
    }

    /************************************************************************************************************************************
     * Fragment's Callback Methods
     *************************************************************************************************************************************/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater)
    {

	// super.onCreateOptionsMenu(menu, menuInflater);
	menuInflater.inflate(R.menu.log_editor_fragment_menu, menu);
	MenuItem item = (MenuItem) menu.findItem(R.id.action_share);
	shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
	shareActionProvider.setShareIntent(getDefaultShareIntent());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

	switch (item.getItemId())
	{
	case android.R.id.home:

	    getActivity().finish();
	    break;
	case R.id.action_save:
	    saveTextToSDCard(editor.getText().toString());
	    break;

	default:
	    break;
	}
	return super.onOptionsItemSelected(item);
    }

    /************************************************************************************************************************************
     * Interface Implemented methods.
     *************************************************************************************************************************************/

    public Loader<Cursor> onCreateLoader(int id, Bundle data)
    {

	Uri uri = Uri.parse(data.getString(URI));

	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");

	switch (id)
	{
	case LOG_EDITOR_FRAGMENT_LOADER:

	    return new CursorLoader(getActivity(), uri, SavedLogsFragment.projections, null, null, SavedLogsFragment.sortOrder);

	default:
	    break;
	}

	return null;

    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	cursor.moveToFirst();// Must call otherwise it will throw :
			     // CursorIndexOutOfBoundsException: Index -1
			     // requested, with a size of 1.

	// Set the content of the log
	String title = cursor.getString(cursor.getColumnIndexOrThrow(LogsProviderContract.Logs.COLUMN_NAME_TITLE));
	getActivity().setTitle(title);

	// set title on the actionbar
	String logText = cursor.getString(cursor.getColumnIndexOrThrow(LogsProviderContract.Logs.COLUMN_NAME_LOG));
	editor.setText(logText);
    }

    public void onLoaderReset(Loader<Cursor> loader)
    {
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
    }

    /************************************************************************************************************************************
     * Helper methods.
     *************************************************************************************************************************************/

    /** Returns a share intent */
    private Intent getDefaultShareIntent()
    {
	Intent intent = new Intent(Intent.ACTION_SEND);
	intent.setType("text/plain");
	// intent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
	// intent.putExtra(Intent.EXTRA_TEXT, "Extra Text");
	intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
	intent.putExtra(Intent.EXTRA_TEXT, editor.getText().toString());
	return intent;
    }

    private void saveTextToSDCard(String logData)
    {
	if (!Utility.isExternalStorageWritable())
	{
	    new DialogInfoOK(getActivity(), "Error!", res.getString(R.string.sd_card_not_available_to_write), "OK").show();
	    return;
	}

	ExecutorService executorService = Executors.newSingleThreadExecutor();
	executorService.execute(new LogFileWriteTask(editor.getText().toString(),getActivity().getTitle().toString()));

    }

    private class LogFileWriteTask implements Runnable
    {
	private String logData;
	private String fileName;

	public LogFileWriteTask(String logData,String fileName)
	{
	    this.logData = logData;
	    this.fileName = fileName;
	}

	public void run()
	{
	    File dir = Environment.getExternalStorageDirectory();
	    File file = new File(dir, res.getString(R.string.app_name));
	    
           		
	    if (!file.mkdir() && !file.isDirectory())
	    {
		Utility.printLog(TAG, "Error in creating dir " + file.getAbsolutePath());
		//showFailMessage();
		return;
	    }
	    
	    Utility.printLog(TAG, "Saving file in :"+file.getAbsolutePath());
	    
	    FileOutputStream fileOutputStream = null;
	    OutputStreamWriter outputStreamWriter = null;
	    try
	    {
		fileOutputStream = new FileOutputStream(new File(file, fileName+".txt"));
		outputStreamWriter = new OutputStreamWriter(fileOutputStream);
		outputStreamWriter.write(logData);
		outputStreamWriter.flush();

	    }
	    catch (FileNotFoundException e)
	    {
		e.printStackTrace();
		//showFailMessage();
	    }
	    catch (IOException e)
	    {
		e.printStackTrace();
		//showFailMessage();
	    }
	    finally
	    {
		try
		{
		    if (outputStreamWriter != null)
		    {
			outputStreamWriter.close();
		    }
		    if (fileOutputStream != null)
		    {
			fileOutputStream.close();
		    }

		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}

	    }
	}
    }

    private void showFailMessage()
    {
	new DialogInfoOK(getActivity(), "Error!", res.getString(R.string.error_saving_file), "OK").show();
    }

}
