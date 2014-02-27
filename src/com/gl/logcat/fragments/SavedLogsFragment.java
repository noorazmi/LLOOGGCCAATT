package com.gl.logcat.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.catlogttest.R;
import com.gl.logcat.activities.LogcatViewActivity;
import com.gl.logcat.data.SavedLogsInfo;
import com.gl.logcat.listeners.MyMultiChoiceModeListener;
import com.gl.logcat.provider.LogsProviderContract;
import com.gl.logcat.util.Utility;

public class SavedLogsFragment extends android.support.v4.app.ListFragment implements LoaderManager.LoaderCallbacks<Cursor>
{

    private static final String TAG = SavedLogsFragment.class.getCanonicalName();
    public static final String[] projections = { LogsProviderContract.Logs._ID, LogsProviderContract.Logs.COLUMN_NAME_TITLE, LogsProviderContract.Logs.COLUMN_NAME_CREATE_DATE,
	    LogsProviderContract.Logs.COLUMN_NAME_LOG };
    private static final String[] fromColumns = { LogsProviderContract.Logs.COLUMN_NAME_TITLE, LogsProviderContract.Logs.COLUMN_NAME_CREATE_DATE /**
     * 
     * 
     * _ID is only being added for attaching checkbox checked listener in @link
     * #CustomViewBinder setViewValue method.
     **/
    };

    private static final String selection = null;
    private static final String[] selectionArgs = null;

    public static final String sortOrder = LogsProviderContract.Logs.COLUMN_NAME_CREATE_DATE;
    // private static final int[] toViews = { R.id.file_name, R.id.date_added,
    // R.id.checkb_box };
    private static final int[] toViews = { R.id.file_name, R.id.date_added };

    private static final int LAODER_ID = 0x01;
    private MyAdapter simpleCursorAdapter;
    private ProgressBar progressBar;
    private ArrayList<String> selectionOptions;
    private static final String DESELECT_ALL = "Deselect All";
    private static final String SELECT_ALL = "Select All";
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner markSpinner;
    private MyMultiChoiceModeListener choiceModeListener;

    private LogcatViewActivity logcatViewActivity;
    private boolean isUncheckedByActionModeDestroy = false;
    private List<SavedLogsInfo> logsList;
    // private ArrayList<String> words = null;
    private MyAdapter adapter;
    private boolean[] mCheckedState;

    public SavedLogsFragment(LogcatViewActivity logcatViewActivity)
    {
	this.logcatViewActivity = logcatViewActivity;
    }

    @Override
    public void onCreate(Bundle icicle)
    {
	super.onCreate(icicle);
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	getLoaderManager().initLoader(LAODER_ID, null, this);
	setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	View view = inflater.inflate(R.layout.logcatview_fragment, null);

	return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	menu.clear();
	super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
	{
	    l.setItemChecked(position, true);
	}
    }

    @Override
    public void onStart()
    {
	super.onStart();

	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
	{
	    getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
	    choiceModeListener = new MyMultiChoiceModeListener(logcatViewActivity, getListView(), this);
	    getListView().setMultiChoiceModeListener(choiceModeListener);
	}
	else
	{
	    getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    registerForContextMenu(getListView());
	}
    }

    // @Override
    // public boolean onCreateOptionsMenu(Menu menu)
    // {
    // getMenuInflater().inflate(R.menu.option, menu);
    //
    // EditText add = null;
    //
    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
    // {
    // View v = menu.findItem(R.id.add).getActionView();
    //
    // if (v != null)
    // {
    // add = (EditText) v.findViewById(R.id.title);
    // }
    // }
    //
    // if (add != null)
    // {
    // add.setOnEditorActionListener(onSearch);
    // }
    //
    // return (super.onCreateOptionsMenu(menu));
    // }

    // @Override
    // public void onCreateContextMenu(ContextMenu menu, View v,
    // ContextMenu.ContextMenuInfo menuInfo)
    // {
    // getMenuInflater().inflate(R.menu.context, menu);
    // }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	switch (item.getItemId())
	{
	case R.id.add:
	    return (true);

	case R.id.reset:
	    // initAdapter();
	    return (true);

	case R.id.about:
	case android.R.id.home:
	    // Toast.makeText(this, "Action Bar Sample App",
	    // Toast.LENGTH_LONG).show();
	    return (true);
	}

	return (super.onOptionsItemSelected(item));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
	boolean result = performActions(item);

	if (!result)
	{
	    result = super.onContextItemSelected(item);
	}

	return (result);
    }

    public void uncheckItems()
    {
	isUncheckedByActionModeDestroy = true;
	Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called uncheckItemsuncheckItems.");
	adapter.clearCheckedStates();
	final int itemCount = getListView().getCount();
	for (int i = 0; i < itemCount; ++i)
	{
	    Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called not null." + i);
	    View v = getListView().getChildAt(i);
	    if (v == null)
	    {
		Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called vvvv null." + i);
		continue;
	    }
	    CheckBox cb = (CheckBox) v.findViewById(R.id.check_box);
	    if (cb == null)
	    {
		Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called null." + i);

	    }
	    if (cb.isChecked())
	    {
		cb.setChecked(false);
	    }
	}
	isUncheckedByActionModeDestroy = false;

    }

    private void checkAllItems()
    {
	final int itemCount = getListView().getCount();
	for (int i = 0; i < itemCount; ++i)
	{
	    Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called not null." + i);
	    View v = getListView().getChildAt(i);
	    mCheckedState[i] = true;
	    if (v == null)
	    {
		Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called vvvv null." + i);
		continue;
	    }
	    CheckBox cb = (CheckBox) v.findViewById(R.id.check_box);
	    if (cb == null)
	    {
		Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called null." + i);

	    }
	    cb.setChecked(true);
	}

    }

    private final void deleteSelectedItem()
    {
	int deletedNoOfItems = 0;
	for (int j = mCheckedState.length - 1; j >= 0; j--)
	{
	    if (mCheckedState[j])
	    {
		logsList.remove(j);
		deletedNoOfItems++;
	    }
	}
	mCheckedState = new boolean[logsList.size()];
	adapter.notifyDataSetChanged();
	choiceModeListener.closeActionMode();
	Toast.makeText(getActivity(), deletedNoOfItems + (deletedNoOfItems > 1 ? " Items" : "Item") + " deleted.", Toast.LENGTH_SHORT).show();

    }

    @SuppressWarnings("unchecked")
    public boolean performActions(MenuItem item)
    {
	switch (item.getItemId())
	{
	case R.id.select_all:
	    checkAllItems();

	    return (true);

	case R.id.delete:

	    deleteSelectedItem();

	    return (true);
	}

	return (false);
    }

    private void initAdapter(List<SavedLogsInfo> logsList)
    {
	adapter = new MyAdapter(logcatViewActivity, R.layout.saved_log_list_item, R.id.file_name, logsList);
	setListAdapter(adapter);
	getListView().setDivider(new ColorDrawable(getResources().getColor(R.color.background_error)));
	getListView().setDividerHeight(2);
    }

    class MyAdapter extends ArrayAdapter<SavedLogsInfo> implements OnCheckedChangeListener
    {

	public MyAdapter(Context context, int resource, int textViewResourceId, List<SavedLogsInfo> logsList)
	{
	    super(context, resource, textViewResourceId, logsList);
	    mCheckedState = new boolean[logsList.size()];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
	    Holder holder;
	    if (convertView == null)
	    {
		LayoutInflater inflater = (LayoutInflater) logcatViewActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.saved_log_list_item, null);
		CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
		holder = new Holder(checkBox, (TextView) convertView.findViewById(R.id.file_name), (TextView) convertView.findViewById(R.id.date_added), position, getItem(position).getId());
		convertView.setTag(holder);
	    }
	    else
	    {
		holder = (Holder) convertView.getTag();
	    }

	    holder.getCheckBox().setOnCheckedChangeListener(null);
	    holder.getCheckBox().setTag(String.valueOf(position));
	    holder.getCheckBox().setChecked(mCheckedState[position]);
	    holder.getCheckBox().setOnCheckedChangeListener(this);

	    holder.getTextViewFileName().setText(getItem(position).getFileName());
	    holder.getTextViewDateAdded().setText(getItem(position).getDateSaved());

	    return convertView;

	}

	public void clearCheckedStates()
	{
	    mCheckedState = new boolean[getCount()];
	}

	@Override
	public void onCheckedChanged(CompoundButton checkBox, boolean isChecked)
	{
	    if (isUncheckedByActionModeDestroy == false)
	    {
		Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called." + isChecked);
		mCheckedState[Integer.parseInt((String) checkBox.getTag())] = isChecked;
		choiceModeListener.checkBoxChecked(Integer.parseInt((String) checkBox.getTag()), isChecked);
	    }

	}

    }

    private static final class Holder
    {
	private CheckBox checkBox;
	private TextView textViewFileName;
	private TextView textViewDateAdded;
	private int position;
	private int contentProviderId;

	public Holder(CheckBox checkBox, TextView textViewFileName, TextView textViewDateAdded, int position, int contentProviderId)
	{
	    this.checkBox = checkBox;
	    this.textViewFileName = textViewFileName;
	    this.textViewDateAdded = textViewDateAdded;
	    this.position = position;
	    this.contentProviderId = contentProviderId;
	}

	public CheckBox getCheckBox()
	{
	    return checkBox;
	}

	public TextView getTextViewFileName()
	{
	    return textViewFileName;
	}

	public TextView getTextViewDateAdded()
	{
	    return textViewDateAdded;
	}

	public int getPosition()
	{
	    return position;
	}

	public int getContentProviderId()
	{
	    return contentProviderId;
	}

    }

    // This method will be called from LogcatViewActivity.
    public void openLogDetails(View v)
    {
	View parent = (View) v.getParent();
	Holder holder = (Holder) parent.getTag();
	int position = holder.getPosition();

	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	Toast.makeText(getActivity(), "ContentProviderId:" + holder.getContentProviderId() + " index in list : " + position, Toast.LENGTH_SHORT).show();
	Uri uri = ContentUris.withAppendedId(LogsProviderContract.Logs.CONTENT_ID_URI_BASE, holder.getContentProviderId());
	Intent intent = new Intent(Intent.ACTION_VIEW);
	intent.setData(uri);
	startActivity(intent);

    }

    /************************************************************************************************************************************
     * Interface Implemented methods.
     *************************************************************************************************************************************/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle)
    {
	Utility.printLog(TAG, "onCreateLoader() Called");
	CursorLoader cursorLoader = new CursorLoader(getActivity(), LogsProviderContract.Logs.CONTENT_URI, projections, selection, selectionArgs, sortOrder);
	return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> laoder, Cursor cursor)
    {
	Utility.printLog(TAG, "onLoadFinished() Called");
	// simpleCursorAdapter.swapCursor(cursor);

	cursor.moveToFirst();
	logsList = new ArrayList<SavedLogsInfo>();
	while (cursor.moveToNext())
	{
	    logsList.add(new SavedLogsInfo(cursor.getInt(cursor.getColumnIndex(LogsProviderContract.Logs._ID)), cursor.getString(cursor.getColumnIndex(LogsProviderContract.Logs.COLUMN_NAME_TITLE)),
		    cursor.getString(cursor.getColumnIndex(LogsProviderContract.Logs.COLUMN_NAME_CREATE_DATE)), cursor.getString(cursor.getColumnIndex(LogsProviderContract.Logs.COLUMN_NAME_LOG))));
	}
	initAdapter(logsList);
	getListView().setDivider(new ColorDrawable(getResources().getColor(R.color.background_error)));
	getListView().setDividerHeight(2);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursor)
    {
	Utility.printLog(TAG, "onLoaderReset() Called");
	// simpleCursorAdapter.swapCursor(null);
    }

}
