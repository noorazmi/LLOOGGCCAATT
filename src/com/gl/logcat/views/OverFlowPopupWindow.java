package com.gl.logcat.views;

import java.util.ArrayList;

import com.example.catlogttest.R;
import com.gl.logcat.util.ModuleConstants;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class OverFlowPopupWindow extends PopupWindow implements OnItemClickListener
{

    private static final String TITLE_SAVE = "Save";
    private static final String TITLE_SHARE = "Share";
    public OverFlowPopupWindow(Context context)
    {
	// the drop down list is a list view
	super(context);
	ListView listViewOptions = new ListView(context);
	listViewOptions.setOnItemClickListener(this);

	// set our adapter and pass our pop up window contents
	ArrayList<Options> optionsList = new ArrayList<Options>();
	optionsList.add(new Options(R.drawable.ic_action_save, TITLE_SAVE));
	optionsList.add(new Options(R.drawable.ic_action_share, TITLE_SHARE));
	listViewOptions.setAdapter(new OptionsAdapter(context, R.layout.popup_list_item, optionsList));

	// some other visual settings
	setFocusable(true);
	setWidth(170);
	setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

	// set the list view as pop up window content
	setContentView(listViewOptions);
    }

    private class OptionsAdapter extends ArrayAdapter<Options>
    {

	private int layoutResourceId;
	private ArrayList<Options> optionsList = null;
	private LayoutInflater inflater;

	public OptionsAdapter(Context context, int layoutResourceId, ArrayList<Options> optionsList)
	{
	    super(context, layoutResourceId, optionsList);
	    this.layoutResourceId = layoutResourceId;
	    this.optionsList = optionsList;
	    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
	    Holder holder;
	    if (convertView == null)
	    {
		convertView = inflater.inflate(layoutResourceId, null);
		holder = new Holder((TextView) convertView.findViewById(R.id.txtTitle), (ImageView) convertView.findViewById(R.id.imgIcon));
		convertView.setTag(holder);
	    }
	    else
	    {
		holder = (Holder) convertView.getTag();
	    }

	    ImageView imgView = holder.getImageView();
	    imgView.setBackgroundResource(optionsList.get(position).getIcon());
	    TextView txtView = holder.getTextView();
	    txtView.setText(optionsList.get(position).getTitle());
	    return convertView;
	}

	public class Holder
	{
	    private TextView textView;
	    private ImageView imageView;

	    public Holder(TextView textView, ImageView imageView)
	    {
		this.textView = textView;
		this.imageView = imageView;

	    }

	    public TextView getTextView()
	    {
		return textView;
	    }

	    public ImageView getImageView()
	    {
		return imageView;
	    }

	}

    }

    private class Options
    {
	private int icon;
	private String title;

	public Options(int icon, String title)
	{
	    this.icon = icon;
	    this.title = title;
	}

	public int getIcon()
	{
	    return icon;
	}

	public String getTitle()
	{
	    return title;
	}
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long rowId)
    {
	Options selectedItem = (Options) parent.getAdapter().getItem(position);
	Intent intent = new Intent("com.gl.logcat.OVERFLOW_OPTION_CLICK_LISTENER");
	
	String title = selectedItem.getTitle();
	if(title.equals(TITLE_SAVE))
	{
	    intent.putExtra(ModuleConstants.MENU_ACTION, ModuleConstants.SAVE_LOGS);
	    view.getContext().sendBroadcast(intent);
	}
	else if(title.equals(TITLE_SHARE))
	{
	    intent.putExtra(ModuleConstants.MENU_ACTION, ModuleConstants.SHARE_LOGS);
	    view.getContext().sendBroadcast(intent);
	}
	
	dismiss();
    }

}
