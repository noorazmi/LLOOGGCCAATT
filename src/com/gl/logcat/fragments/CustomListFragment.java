//package com.gl.logcat.fragments;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Locale;
//
//import android.annotation.TargetApi;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.util.SparseBooleanArray;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.ArrayAdapter;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.example.catlogttest.R;
//import com.gl.logcat.activities.LogcatViewActivity;
//import com.gl.logcat.listeners.MyMultiChoiceModeListener;
//
//public class CustomListFragment extends android.support.v4.app.ListFragment
//{
//    private LogcatViewActivity logcatViewActivity;
//
//    public CustomListFragment(LogcatViewActivity logcatViewActivity)
//    {
//	this.logcatViewActivity = logcatViewActivity;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//    {
//
//	return super.onCreateView(inflater, container, savedInstanceState);
//    }
//
//    private static final String TAG = "ActionModeDemo";
//    private MyMultiChoiceModeListener choiceModeListener;
//
//    @TargetApi(11)
//    @Override
//    public void onCreate(Bundle icicle)
//    {
//	super.onCreate(icicle);
//
//    }
//
//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id)
//    {
//	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//	{
//	    l.setItemChecked(position, true);
//	}
//    }
//
//    @Override
//    public void onStart()
//    {
//	super.onStart();
//	initAdapter();
//
//	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//	{
//	    // getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//	    // choiceModeListener = new
//	    // HCMultiChoiceModeListener(logcatViewActivity,
//	    // getListView(),this);
//	    // getListView().setMultiChoiceModeListener(choiceModeListener);
//	}
//	else
//	{
//	    getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//	    registerForContextMenu(getListView());
//	}
//    }
//
//    // @Override
//    // public boolean onCreateOptionsMenu(Menu menu)
//    // {
//    // getMenuInflater().inflate(R.menu.option, menu);
//    //
//    // EditText add = null;
//    //
//    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//    // {
//    // View v = menu.findItem(R.id.add).getActionView();
//    //
//    // if (v != null)
//    // {
//    // add = (EditText) v.findViewById(R.id.title);
//    // }
//    // }
//    //
//    // if (add != null)
//    // {
//    // add.setOnEditorActionListener(onSearch);
//    // }
//    //
//    // return (super.onCreateOptionsMenu(menu));
//    // }
//
//    // @Override
//    // public void onCreateContextMenu(ContextMenu menu, View v,
//    // ContextMenu.ContextMenuInfo menuInfo)
//    // {
//    // getMenuInflater().inflate(R.menu.context, menu);
//    // }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//	switch (item.getItemId())
//	{
//	case R.id.add:
//	    add();
//	    return (true);
//
//	case R.id.reset:
//	    initAdapter();
//	    return (true);
//
//	case R.id.about:
//	case android.R.id.home:
//	    // Toast.makeText(this, "Action Bar Sample App",
//	    // Toast.LENGTH_LONG).show();
//	    return (true);
//	}
//
//	return (super.onOptionsItemSelected(item));
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item)
//    {
//	boolean result = performActions(item);
//
//	if (!result)
//	{
//	    result = super.onContextItemSelected(item);
//	}
//
//	return (result);
//    }
//
//    @SuppressWarnings("unchecked")
//    public boolean performActions(MenuItem item)
//    {
//	ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();
//	SparseBooleanArray checked = getListView().getCheckedItemPositions();
//
//	switch (item.getItemId())
//	{
//	case R.id.cap:
//	    for (int i = 0; i < checked.size(); i++)
//	    {
//		if (checked.valueAt(i))
//		{
//		    int position = checked.keyAt(i);
//		    String word = words.get(position);
//
//		    word = word.toUpperCase(Locale.ENGLISH);
//
//		    adapter.remove(words.get(position));
//		    adapter.insert(word, position);
//		}
//	    }
//
//	    return (true);
//
//	case R.id.delete:
//	    ArrayList<Integer> positions = new ArrayList<Integer>();
//
//	    for (int i = 0; i < checked.size(); i++)
//	    {
//		if (checked.valueAt(i))
//		{
//		    positions.add(checked.keyAt(i));
//		}
//	    }
//
//	    Collections.sort(positions, Collections.reverseOrder());
//
//	    for (int position : positions)
//	    {
//		adapter.remove(words.get(position));
//	    }
//
//	    getListView().clearChoices();
//
//	    return (true);
//	}
//
//	return (false);
//    }
//
//    private void initAdapter()
//    {
//
//	MyAdapter adapter = new MyAdapter(logcatViewActivity, R.layout.saved_log_list_item, R.id.file_name, words);
//	setListAdapter(adapter);
//    }
//
//    private void add()
//    {
//	final View addView = logcatViewActivity.getLayoutInflater().inflate(R.layout.add, null);
//
//	new AlertDialog.Builder(logcatViewActivity).setTitle("Add a Word").setView(addView).setPositiveButton("OK", new DialogInterface.OnClickListener()
//	{
//	    public void onClick(DialogInterface dialog, int whichButton)
//	    {
//		addWord((TextView) addView.findViewById(R.id.title));
//	    }
//	}).setNegativeButton("Cancel", null).show();
//    }
//
//    @SuppressWarnings("unchecked")
//    private void addWord(TextView title)
//    {
//	ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();
//
//	adapter.add(title.getText().toString());
//    }
//
//    private TextView.OnEditorActionListener onSearch = new TextView.OnEditorActionListener()
//    {
//	public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
//	{
//	    if (event == null || event.getAction() == KeyEvent.ACTION_UP)
//	    {
//		addWord(v);
//
//		InputMethodManager imm = (InputMethodManager) logcatViewActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//
//		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//	    }
//
//	    return (true);
//	}
//    };
//
//    // public void onListItemClick(View v)
//    // {
//    // Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() +
//    // "() called.");
//    // }
//
//    class MyAdapter extends ArrayAdapter<String> implements OnCheckedChangeListener
//    {
//
//	private final boolean[] mCheckedState;
//
//	public MyAdapter(Context context, int resource, int textViewResourceId, List<String> objects)
//	{
//	    super(context, resource, textViewResourceId, objects);
//	    mCheckedState = new boolean[objects.size()];
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent)
//	{
//	    Holder holder;
//	    if (convertView == null)
//	    {
//		LayoutInflater inflater = (LayoutInflater) logcatViewActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		convertView = inflater.inflate(R.layout.saved_log_list_item, null);
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
//
//	    holder.getTextView().setText(getItem(position));
//
//	    return convertView;
//
//	}
//
//	@Override
//	public boolean hasStableIds()
//	{
//	    return true;
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
//
//	}
//
//    }
//
//}
