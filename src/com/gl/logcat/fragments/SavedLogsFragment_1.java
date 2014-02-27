package com.gl.logcat.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.example.catlogttest.R;
import com.gl.logcat.adapter.LogcatLinesAdapter;
import com.gl.logcat.listeners.MyMultiChoiceModeListener;
import com.gl.logcat.provider.LogsProviderContract;
import com.gl.logcat.util.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;

public class SavedLogsFragment_1 extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, OnItemClickListener
{
    private static final String TAG = SavedLogsFragment.class.getCanonicalName();
    public static final String[] projections = { LogsProviderContract.Logs._ID, LogsProviderContract.Logs.COLUMN_NAME_TITLE, LogsProviderContract.Logs.COLUMN_NAME_CREATE_DATE,
	    LogsProviderContract.Logs.COLUMN_NAME_LOG };
    private static final String[] fromColumns = { LogsProviderContract.Logs.COLUMN_NAME_TITLE, LogsProviderContract.Logs.COLUMN_NAME_CREATE_DATE /**
     * 
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
    //private MultiChoiceListener multiChoiceListener;
    // Selection Options spinner data
    private ArrayList<String> selectionOptions;
    private static final String DESELECT_ALL = "Deselect All";
    private static final String SELECT_ALL = "Select All";
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner markSpinner;
    private MyMultiChoiceModeListener choiceModeListener;
    
    
    private static final String[] items = { "lorem", "ipsum", "dolor", "sit", "amet", "consectetuer", "adipiscing", "elit", "morbi", "vel", "ligula", "vitae", "arcu", "aliquet", "mollis", "etiam",
	    "vel", "erat", "placerat", "ante", "porttitor", "sodales", "pellentesque", "augue", "purus" };
    private ArrayList<String> words = null;
    /************************************************************************************************************************************
     * Fragment Life cycle methods
     *************************************************************************************************************************************/
    @Override
    public void onAttach(Activity activity)
    {
	super.onAttach(activity);
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	getLoaderManager().initLoader(LAODER_ID, null, this);
	//simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.saved_log_list_item_1, null, fromColumns, toViews, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	//simpleCursorAdapter = new MyAdapter(getActivity(), R.layout.saved_log_list_item_1, null, fromColumns, toViews, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

	
	// simpleCursorAdapter.setViewBinder(new CustomViewBinder());
	//setListAdapter(simpleCursorAdapter);
	// Must be set in order to perform any options menu related
	// operation.For example onPrepareOptionsMenu() methods will not be
	// called if we don't call this method.
	setHasOptionsMenu(true);
	// getListView().setDivider(android.R.drawable.divider_horizontal_bright);
	
    }

   
    private void initAdapter()
    {
	words = new ArrayList<String>();

	for (String s : items)
	{
	    words.add(s);
	}
	MyAdapter adapter = new MyAdapter(getActivity(), R.layout.saved_log_list_item, R.id.file_name, words);
	setListAdapter(adapter);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	View view = inflater.inflate(R.layout.logcatview_fragment, null);

	// progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
	return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	

//	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
//	{
//	    getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//	}
//	else
//	{
//	    getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//	}
//	
//	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//	{
//	    getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//	    multiChoiceListener = new MultiChoiceListener();
//	    getListView().setMultiChoiceModeListener(multiChoiceListener);
//	}
//	else
//	{
//	    getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//	    registerForContextMenu(getListView());
//	}
	
	
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
	{
	    getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
	    //choiceModeListener = new HCMultiChoiceModeListener(this, getListView());
	    getListView().setMultiChoiceModeListener(choiceModeListener);
	}
	else
	{
	    getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    registerForContextMenu(getListView());
	}
	
	
	//getListView().setMultiChoiceModeListener(multiChoiceListener);

	super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart()
    {
	super.onStart();
	// getListView().setOnItemClickListener(this);
	// getListView().setOnItemLongClickListener(new
	// OnItemLongClickListener()
	// {
	//
	// @Override
	// public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int
	// position, long arg3)
	// {
	// Utility.printLog(TAG, new
	// Exception().getStackTrace()[0].getMethodName() + "() called.");
	// Utility.printLog(TAG, "onItemLongClick called.");
	// getListView().setItemChecked(position,
	// !multiChoiceListener.isPositionChecked(position));
	// return false;
	// }
	// });
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

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	menu.clear();
	super.onPrepareOptionsMenu(menu);
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
	//simpleCursorAdapter.swapCursor(cursor);
	initAdapter();
	getListView().setDivider(new ColorDrawable(getResources().getColor(R.color.background_error)));
	getListView().setDividerHeight(2);
	
	
	
	// progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursor)
    {
	Utility.printLog(TAG, "onLoaderReset() Called");
	// simpleCursorAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> listView, View view, int position, long id)
    {
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	Toast.makeText(getActivity(), "id=" + id, Toast.LENGTH_SHORT).show();
	Uri uri = ContentUris.withAppendedId(LogsProviderContract.Logs.CONTENT_ID_URI_BASE, id);
	Intent intent = new Intent(Intent.ACTION_VIEW);
	intent.setData(uri);
	// startActivity(intent);

    }

    /************************************************************************************************************************************
     * Helper methods and classes.
     *************************************************************************************************************************************/

    private final class MultiChoiceListener implements AbsListView.MultiChoiceModeListener
    {
	private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
	private ActionMode activeMode;

	/************************************************************************************************************************************
	 * Callback methods.
	 *************************************************************************************************************************************/
	@Override
	public boolean onActionItemClicked(ActionMode actionMove, MenuItem menuItem)
	{
	    Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	    return false;
	}

	@Override
	public boolean onCreateActionMode(ActionMode actionMode, Menu menu)
	{
	    Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");

	    MenuInflater inflater = getActivity().getMenuInflater();
	    inflater.inflate(R.menu.saved_logs_list_context_menu, menu);

	    selectionOptions = new ArrayList<String>();
	    selectionOptions.add(0, DESELECT_ALL);
	    selectionOptions.add(1, SELECT_ALL);
	    spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, selectionOptions);
	    markSpinner = new Spinner(getActivity(), Spinner.MODE_DROPDOWN);
	    markSpinner.setAdapter(spinnerAdapter);

	    MenuItem markItem = menu.findItem(R.id.action_mark);
	    // markItem.setActionView(Utility.resId_action_filter_layout);
	    markItem.setActionView(markSpinner);
	    markItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	    markSpinner = (Spinner) markItem.getActionView();
	    markSpinner.setOnItemSelectedListener(markSpinnerListener);

	    return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode)
	{
	    Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu)
	{
	    Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	    return false;
	}

	@Override
	public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked)
	{
	    Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	}

	/************************************************************************************************************************************
	 * Helper methods.
	 *************************************************************************************************************************************/

	public void checkBocChecked(int position, boolean isChecked)
	{
	    Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called." + isChecked);

	    // CheckBox c = (CheckBox)
	    // ((LinearLayout)lv.getChildAt(position)).getChildAt(0);
	    // c.setChecked(!c.isChecked());

	    // CheckedTextView cv = (CheckedTextView)
	    // ((LinearLayout)lv.getChildAt(position)).getChildAt(2);
	    // cv.setChecked(isChecked);
	    // lv.performItemClick((LinearLayout)lv.getChildAt(0), position, 0);
	    // cv.setChecked(true);
	    getListView().getChildAt(position).performLongClick();
	   // getListView().performItemClick((RelativeLayout) getListView().getChildAt(0), position, 0);
	    updateSubtitle(activeMode);
	    // updateSubtitle(mode);

	}

	private void updateSubtitle(ActionMode mode)
	{
	    Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	    try
	    {
		activeMode.setSubtitle("(" + getListView().getCheckedItemCount() + ")");
	    }
	    catch (NullPointerException exception)
	    {
		exception.printStackTrace();

	    }

	}

	public void setNewSelection(int position, boolean value)
	{
	    Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	    mSelection.put(position, value);
	}

	public boolean isPositionChecked(int position)
	{
	    Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	    Boolean result = mSelection.get(position);
	    return result == null ? false : result;
	}

	public Set<Integer> getCurrentCheckedPosition()
	{
	    Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	    return mSelection.keySet();
	}

	public void removeSelection(int position)
	{
	    Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	    mSelection.remove(position);
	    simpleCursorAdapter.notifyDataSetChanged();

	}

	public void clearSelection()
	{
	    Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	    mSelection = new HashMap<Integer, Boolean>();
	    simpleCursorAdapter.notifyDataSetChanged();
	}

    }



    OnItemSelectedListener markSpinnerListener = new OnItemSelectedListener()
    {

	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int position, long id)
	{
	    Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	    // LogcatLinesAdapter.setLogLevelLimit(position);
	    // logcatViewFragment.filter(mSearchView.getQuery().toString());
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{

	}
    };

    
    class MyAdapter extends ArrayAdapter<String> implements OnCheckedChangeListener
    {

	private final boolean[] mCheckedState;

	public MyAdapter(Context context, int resource, int textViewResourceId, List<String> objects)
	{
	    super(context, resource, textViewResourceId, objects);
	    mCheckedState = new boolean[objects.size()];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
	    Holder holder;
	    if (convertView == null)
	    {
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.saved_log_list_item, null);
		CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
		holder = new Holder(checkBox, (TextView) convertView.findViewById(R.id.file_name));
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

	    holder.getTextView().setText(getItem(position));

	    return convertView;

	}

	@Override
	public boolean hasStableIds()
	{
	    return true;
	}

	private final class Holder
	{
	    private CheckBox checkBox;
	    private TextView textView;

	    public Holder(CheckBox checkBox, TextView textView)
	    {
		this.checkBox = checkBox;
		this.textView = textView;
	    }

	    public CheckBox getCheckBox()
	    {
		return checkBox;
	    }

	    public TextView getTextView()
	    {
		return textView;
	    }
	}

	@Override
	public void onCheckedChanged(CompoundButton checkBox, boolean isChecked)
	{
	    Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called." + isChecked);
	    mCheckedState[Integer.parseInt((String) checkBox.getTag())] = isChecked;
	    choiceModeListener.checkBoxChecked(Integer.parseInt((String) checkBox.getTag()), isChecked);

	}

    }
    
    //class MyAdapter extends ArrayAdapter<String> implements OnCheckedChangeListener
//    class MyAdapter extends SimpleCursorAdapter implements OnCheckedChangeListener
//    {
//	
//	private boolean[] mCheckedState;
//	public MyAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags)
//	{
//	    super(context, layout, c, from, to, flags);
//	    
//	}
//
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent)
//	{
//	    Holder holder;
//	    if (convertView == null)
//	    {
//		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		convertView = inflater.inflate(R.layout.saved_log_list_item_1, null);
//		CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
//		holder = new Holder(checkBox, (TextView) convertView.findViewById(R.id.file_name));
//		convertView.setTag(holder);
//	    }
//	    else
//	    {
//		holder = (Holder) convertView.getTag();
//	    }
//
//	    holder.getCheckBox().setOnCheckedChangeListener(null);
//	    holder.getCheckBox().setTag(String.valueOf(position));
//	    holder.getCheckBox().setChecked(mCheckedState[position]);
//	    holder.getCheckBox().setOnCheckedChangeListener(this);
//            Cursor cursor = (Cursor) getItem(position);
//            String fileName = cursor.getString(cursor.getColumnIndex(LogsProviderContract.Logs.COLUMN_NAME_TITLE));
//	    holder.getTextView().setText(fileName);
//
//	    return convertView;
//
//	}
//
//	@Override
//	public int getCount()
//	{
//	    mCheckedState = new boolean[super.getCount()];
//	    return super.getCount();
//	}
//	
//	private final class Holder
//	{
//	    private CheckBox checkBox;
//	    private TextView textView;
//
//	    public Holder(CheckBox checkBox, TextView textView)
//	    {
//		this.checkBox = checkBox;
//		this.textView = textView;
//	    }
//
//	    public CheckBox getCheckBox()
//	    {
//		return checkBox;
//	    }
//
//	    public TextView getTextView()
//	    {
//		return textView;
//	    }
//	}
//
//	@Override
//	public void onCheckedChanged(CompoundButton checkBox, boolean isChecked)
//	{
//	    Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called." + isChecked);
//	    mCheckedState[Integer.parseInt((String) checkBox.getTag())] = isChecked;
//	    choiceModeListener.checkBocChecked(Integer.parseInt((String) checkBox.getTag()), isChecked);
//	    //multiChoiceListener.checkBocChecked(Integer.parseInt((String) checkBox.getTag()), isChecked);
//
//	}
//
//    }

}
