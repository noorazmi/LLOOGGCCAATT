package com.gl.logcat.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.catlogttest.R;
import com.gl.logcat.data.LogLine;
import com.gl.logcat.data.LogLineViewWrapper;
import com.gl.logcat.data.SearchCriteria;
import com.gl.logcat.fragments.LogcatViewFragment;
import com.gl.logcat.helper.PreferenceHelper;
import com.gl.logcat.services.SaveLogService;
import com.gl.logcat.util.LogLineAdapterUtil;
import com.gl.logcat.util.ModuleConstants;
import com.gl.logcat.util.StringUtil;

public class LogcatLinesAdapter extends BaseAdapter
{
    private LogcatViewFragment logcatViewFragment;
    private Comparator<? super LogLine> mComparator;

    private static int logLevelLimit = 0;

    /**
     * Indicates whether or not {@link #notifyDataSetChanged()} must be called
     * whenever {@link #mObjects} is modified.
     */
    private boolean mNotifyOnChange = true;

    /**
     * Lock used to modify the content of {@link #mObjects}. Any write operation
     * performed on the array should be synchronized on this lock. This lock is
     * also used by the filter (see {@link #getFilter()} to make a synchronized
     * copy of the original array of data.
     */
    private final Object mLock = new Object();
    /**
     * Contains the list of objects that represent the data of this
     * ArrayAdapter. The content of this list is referred to as "the array" in
     * the documentation.
     */
    private List<LogLine> mObjects;
    private Context mContext;
    private ArrayFilter mArrayFilter;

    private ArrayList<LogLine> mOriginalValues;

    private LayoutInflater mInflater;

    /**
     * The resource indicating what views to inflate to display the content of
     * this array adapter.
     */
    private int resIdLogcatListItem;

    /**
     * The resource indicating what views to inflate to display the content of
     * this array adapter in a drop down widget.
     */
    private int mDropDownResource;

    /**
     * Constructor
     * 
     * @param context
     *            The current context.
     * @param textViewResourceId
     *            The resource ID for a layout file containing a TextView to use
     *            when instantiating views.
     * @param objects
     *            The objects to represent in the ListView.
     */

    public LogcatLinesAdapter(Context context, int resId_logcat_list_item, List<LogLine> objects, LogcatViewFragment logcatViewFragment)
    {
	init(context, resId_logcat_list_item, 0, objects, logcatViewFragment);
    }

    private void init(Context context, int resId_logcat_list_item, int textViewResourceId, List<LogLine> objects, LogcatViewFragment logcatViewFragment)
    {
	mContext = context;
	mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	resIdLogcatListItem = mDropDownResource = resId_logcat_list_item;
	mObjects = objects;
	this.logcatViewFragment = logcatViewFragment;

    }

    @Override
    public int getCount()
    {
	synchronized (mLock)
	{
	    return mObjects.size();
	}
    }

    @Override
    public Object getItem(int position)
    {
	return mObjects.get(position);
    }

    @Override
    public long getItemId(int position)
    {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
	return createViewFromResource(position, convertView, parent, resIdLogcatListItem);
    }

    private View createViewFromResource(int position, View view, ViewGroup parent, int resource)
    {
	Context context = parent.getContext();
	LogLineViewWrapper wrapper;
	if (view == null)
	{
	    view = mInflater.inflate(resIdLogcatListItem, parent, false);
	    wrapper = new LogLineViewWrapper(view);
	    view.setTag(wrapper);
	}
	else
	{
	    wrapper = (LogLineViewWrapper) view.getTag();
	}

	TextView levelTextView = wrapper.getLevelTextView();
	TextView outputTextView = wrapper.getOutputTextView();
	TextView tagTextView = wrapper.getTagTextView();
	TextView pidTextView = wrapper.getPidTextView();
	TextView timestampTextView = wrapper.getTimestampTextView();

	LogLine logLine;
	try
	{
	    logLine = (LogLine) getItem(position);
	}
	catch (IndexOutOfBoundsException e)
	{
	    // XXX hack - I sometimes get array index out of bounds exceptions
	    // here
	    // no idea how to solve it, so this is the best I can do
	    // logLine = LogLine.newLogLine("",
	    // PreferenceHelper.getExpandedByDefaultPreference(context));
	    logLine = LogLine.newLogLine("", false);
	}

	levelTextView.setText(Character.toString(LogLine.convertLogLevelToChar(logLine.getLogLevel())));
	levelTextView.setBackgroundColor(LogLineAdapterUtil.getBackgroundColorForLogLevel(context, logLine.getLogLevel()));
	levelTextView.setTextColor(LogLineAdapterUtil.getForegroundColorForLogLevel(context, logLine.getLogLevel()));
	levelTextView.setVisibility(logLine.getLogLevel() == -1 ? View.GONE : View.VISIBLE);

	// empty tag indicates this is the line like "beginning of dev/log..."
	CharSequence output = (logLine.isExpanded() || TextUtils.isEmpty(logLine.getTag())) ? logLine.getLogOutput() : StringUtil.ellipsizeString(logLine.getLogOutput(), outputTextView);

	outputTextView.setSingleLine(!logLine.isExpanded());
	outputTextView.setText(output);
	outputTextView.setTextColor(Color.WHITE);

	CharSequence tag = logLine.isExpanded() ? logLine.getTag() : StringUtil.ellipsizeString(logLine.getTag(), tagTextView);

	tagTextView.setSingleLine(!logLine.isExpanded());
	tagTextView.setText(tag);
	tagTextView.setVisibility(logLine.getLogLevel() == -1 ? View.GONE : View.VISIBLE);
	tagTextView.setTextColor(Color.WHITE);
	// set the text size based on the preferences

	// float textSize = PreferenceHelper.getTextSizePreference(context);
	float textSize = 15;

	tagTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
	outputTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
	levelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

	pidTextView.setText(logLine.getProcessId() != -1 ? Integer.toString(logLine.getProcessId()) : null);
	pidTextView.setTextColor(Color.WHITE);
	timestampTextView.setText("Time: " + logLine.getTimestamp());
	timestampTextView.setTextColor(Color.WHITE);

	return view;

    }


    public void addWithFilter(LogLine object, CharSequence text)
    {

	if (mOriginalValues != null)
	{

	    List<LogLine> inputList = Arrays.asList(object);

	    if (mArrayFilter == null)
	    {
		mArrayFilter = new ArrayFilter();
	    }

	    List<LogLine> filteredObjects = mArrayFilter.performFilteringOnList(inputList, text);

	    synchronized (mLock)
	    {
		mOriginalValues.add(object);

		mObjects.addAll(filteredObjects);

		if (mNotifyOnChange)
		{
		    notifyDataSetChanged();
		}
	    }
	}
	else
	{
	    synchronized (mLock)
	    {
		mObjects.add(object);
	    }
	    if (mNotifyOnChange)
	    {
		notifyDataSetChanged();
	    }
	}

    }

    public ArrayList<LogLine> performFilteringOnList(List<LogLine> inputList, CharSequence query)
    {

	SearchCriteria searchCriteria = new SearchCriteria(query);

	// search by log level
	ArrayList<LogLine> allValues = new ArrayList<LogLine>();

	ArrayList<LogLine> logLines;
	synchronized (mLock)
	{
	    logLines = new ArrayList<LogLine>(inputList);
	}

	for (LogLine logLine : logLines)
	{
	    if (logLine != null && LogLineAdapterUtil.logLevelIsAcceptableGivenLogLevelLimit(logLine.getLogLevel(), logLevelLimit))
	    {
		allValues.add(logLine);
	    }
	}
	ArrayList<LogLine> finalValues = allValues;

	// search by criteria
	if (!searchCriteria.isEmpty())
	{

	    final ArrayList<LogLine> values = allValues;
	    final int count = values.size();

	    final ArrayList<LogLine> newValues = new ArrayList<LogLine>(count);

	    for (int i = 0; i < count; i++)
	    {
		final LogLine value = values.get(i);
		// search the logline based on the criteria
		if (searchCriteria.matches(value))
		{
		    newValues.add(value);
		}
	    }

	    finalValues = newValues;
	}

	// sort here to ensure that filtering the list doesn't mess up the
	// sorting
	if (mComparator != null)
	{
	    Collections.sort((List<LogLine>) finalValues, mComparator);
	}

	return finalValues;
    };

    /**
     * Sorts the content of this adapter using the specified comparator.
     * 
     * @param comparator
     *            The comparator used to sort the objects contained in this
     *            adapter.
     */
    public void sort(Comparator<? super LogLine> comparator)
    {
	this.mComparator = comparator;
	Collections.sort(mObjects, comparator);
	if (mNotifyOnChange)
	{
	    notifyDataSetChanged();
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDataSetChanged()
    {
	super.notifyDataSetChanged();
	mNotifyOnChange = true;
	logcatViewFragment.onLoglineAdded(mObjects.size());

	// logcatViewFragment.onFilterComplete(mObjects.size());

    }

    public interface LogLineAddedListener
    {
	public void onLoglineAdded(int count);
    }

    public Filter getFilter()
    {
	if (mArrayFilter == null)
	{
	    mArrayFilter = new ArrayFilter();
	}
	return mArrayFilter;
    }

    private class ArrayFilter extends Filter
    {
	@Override
	protected FilterResults performFiltering(CharSequence prefix)
	{

	    FilterResults results = new FilterResults();

	    if (mOriginalValues == null)
	    {
		synchronized (mLock)
		{
		    mOriginalValues = new ArrayList<LogLine>(mObjects);
		}
	    }

	    ArrayList<LogLine> allValues = performFilteringOnList(mOriginalValues, prefix);

	    results.values = allValues;
	    results.count = allValues.size();

	    return results;

	}

	public ArrayList<LogLine> performFilteringOnList(List<LogLine> inputList, CharSequence query)
	{

	    SearchCriteria searchCriteria = new SearchCriteria(query);

	    // search by log level
	    ArrayList<LogLine> allValues = new ArrayList<LogLine>();

	    ArrayList<LogLine> logLines;
	    synchronized (mLock)
	    {
		logLines = new ArrayList<LogLine>(inputList);
	    }

	    for (LogLine logLine : logLines)
	    {
		if (logLine != null && LogLineAdapterUtil.logLevelIsAcceptableGivenLogLevelLimit(logLine.getLogLevel(), logLevelLimit))
		{
		    allValues.add(logLine);
		}
	    }
	    ArrayList<LogLine> finalValues = allValues;

	    // search by criteria
	    if (!searchCriteria.isEmpty())
	    {

		final ArrayList<LogLine> values = allValues;
		final int count = values.size();

		final ArrayList<LogLine> newValues = new ArrayList<LogLine>(count);

		for (int i = 0; i < count; i++)
		{
		    final LogLine value = values.get(i);
		    // search the logline based on the criteria
		    if (searchCriteria.matches(value))
		    {
			newValues.add(value);
		    }
		}

		finalValues = newValues;
	    }

	    // sort here to ensure that filtering the list doesn't mess up the
	    // sorting
	    if (mComparator != null)
	    {
		Collections.sort((List<LogLine>) finalValues, mComparator);
	    }

	    return finalValues;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void publishResults(CharSequence constraint, FilterResults results)
	{

	    mObjects = (List<LogLine>) results.values;
	    if (results.count > 0)
	    {
		notifyDataSetChanged();
	    }
	    else
	    {
		notifyDataSetInvalidated();
	    }
	}
    }

    public static int getLogLevelLimit()
    {
	return logLevelLimit;
    }

    public static void setLogLevelLimit(int logLevelLimit)
    {
	LogcatLinesAdapter.logLevelLimit = logLevelLimit;
    }

    public void clearLogs()
    {
	mOriginalValues.clear();
	mObjects.clear();
    }

    public void saveLogs(String fileName)
    {
	final ArrayList<CharSequence> logs = getLogAsListOfStrings();
	Intent serviceIntent = new Intent(mContext, SaveLogService.class);
	Bundle bundle = new Bundle();
	bundle.putCharSequenceArrayList(ModuleConstants.LOG_DATA, logs);
	bundle.putString(ModuleConstants.FILE_NAME, fileName);
	serviceIntent.putExtras(bundle);
	mContext.startService(serviceIntent);
    }

    private ArrayList<CharSequence> getLogAsListOfStrings()
    {

	ArrayList<CharSequence> result = new ArrayList<CharSequence>(getCount());

	for (int i = 0; i < getCount(); i++)
	{
	    result.add(((LogLine) getItem(i)).getOriginalLine());
	}

	return result;
    }

}
