package com.gl.logcat.views;

import com.gl.logcat.util.Utility;

import android.content.Context;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class OverFlowActionProvider extends ActionProvider
{

    private final Context context;
    private final int layout;
    private View view;
    private OverFlowPopupWindow popupWindow;
    private static final String TAG = OverFlowActionProvider.class.getCanonicalName(); 

    public OverFlowActionProvider(Context context, int layout)
    {
	super(context);
	this.layout = layout;
	this.context = context;
	popupWindow = new OverFlowPopupWindow(context); 
    }

    @Override
    public View onCreateActionView()
    {
	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	View view = inflater.inflate(this.layout, null);

	view.setOnClickListener(new View.OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		Utility.printLog( TAG, "onClick() calle.");
		popupWindow.showAsDropDown(OverFlowActionProvider.this.view, 50, 0);
	    }
	});
	this.view = view;
	return view;
    }
}
