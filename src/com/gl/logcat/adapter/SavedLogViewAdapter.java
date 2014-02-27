package com.gl.logcat.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.catlogttest.R;
import com.gl.logcat.data.LogLine;
import com.gl.logcat.data.LogLineViewWrapper;
import com.gl.logcat.util.LogLineAdapterUtil;
import com.gl.logcat.util.StringUtil;

public class SavedLogViewAdapter extends ArrayAdapter<LogLine>
{

    private LayoutInflater inflator;

    public SavedLogViewAdapter(Context context, int layoutId, ArrayList<LogLine> logLines)
    {
	super(context, layoutId, logLines);
	inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
	LogLineViewWrapper wrapper;
	Context context = getContext();

	if (convertView == null)
	{
	    convertView = inflator.inflate(R.layout.logcat_list_item, null);
	    wrapper = new LogLineViewWrapper(convertView);
	    convertView.setTag(wrapper);
	}
	else
	{
	    wrapper = (LogLineViewWrapper) convertView.getTag();
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

	return convertView;
    }

}
