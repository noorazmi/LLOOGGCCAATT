package com.gl.logcat.data;

import com.gl.logcat.util.Utility;

import android.view.View;
import android.widget.TextView;

/**
 * Improves performance of the ListView. Watch Romain Guy's video about ListView
 * to learn more.
 * 
 * @author nlawson
 * 
 */
public class LogLineViewWrapper {

	private View view;
	private TextView levelTextView;
	private TextView outputTextView;
	private TextView tagTextView;
	private TextView pidTextView;
	private TextView timestampTextView;

	public LogLineViewWrapper(View view) {
		this.view = view;
	}

	public TextView getPidTextView() {
		if (pidTextView == null) {
			pidTextView = (TextView) view.findViewById(Utility.resId_pid_text);
		}
		return pidTextView;
	}

	public TextView getTimestampTextView() {
		if (timestampTextView == null) {
			timestampTextView = (TextView) view
					.findViewById(Utility.resId_timestamp_text);
		}
		return timestampTextView;
	}

	public TextView getTagTextView() {
		if (tagTextView == null) {
			tagTextView = (TextView) view.findViewById(Utility.resId_tag_text);
		}
		return tagTextView;
	}

	public TextView getLevelTextView() {
		if (levelTextView == null) {
			levelTextView = (TextView) view
					.findViewById(Utility.resId_log_level_text);
		}
		return levelTextView;
	}

	public TextView getOutputTextView() {
		if (outputTextView == null) {
			outputTextView = (TextView) view
					.findViewById(Utility.resId_log_output_text);
		}
		return outputTextView;
	}
}
