package com.gl.logcat.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gl.logcat.util.Utility;

public class DrawerListAdapter extends ArrayAdapter<String>
{
    private final String[] drawerOptions;
    private final Context context;

    public DrawerListAdapter(Context context, int resId_drawer_list_item, String[] drawerOptions)
    {
	super(context, resId_drawer_list_item, drawerOptions);
	this.drawerOptions = drawerOptions;
	this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
	LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	TextView drawerListItem = (TextView) layoutInflater.inflate(Utility.resId_drawer_list_item, parent, false);
	drawerListItem.setText(drawerOptions[position]);
	applyStylesAttributeToListItem(drawerListItem);
	drawerListItem.setTextColor(Color.WHITE);
	drawerListItem.setPadding(16, 0, 0, 0);
	return drawerListItem;
    }

    
    private void applyStylesAttributeToListItem(TextView drawerListItem)
    {
	// Create an array of the attributes we want to resolve
	// using values from a theme
	int[] attributeNamesArray = new int[] { android.R.attr.activatedBackgroundIndicator, android.R.attr.textAppearanceListItemSmall, android.R.attr.listPreferredItemHeightSmall /*
																				        * index
																				        * 0
																				        */};

	// Obtain the styled attributes. 'themedContext' is a context with a
	// theme, typically the current Activity (i.e. 'this')
	TypedArray typedArray = context.obtainStyledAttributes(attributeNamesArray);

	// Now get the value of the 'listItemBackground' attribute that was
	// set in the theme used in 'themedContext'. The parameter is the index
	// of the attribute in the 'attributeNamesList' array. The returned Drawable
	// is what you are after
	 Drawable drawableFromTheme = typedArray.getDrawable(0 /* index of android.R.attr.activatedBackgroundIndicator in attributeNamesArray*/);
	 drawerListItem.setBackground(drawableFromTheme);
	 //
	 drawerListItem.setTextAppearance(context, typedArray.getResourceId(1 /* index of android.R.attr.textAppearanceListItemSmall in attributeNamesArray*/, android.R.style.TextAppearance_Small));
	 
	 drawerListItem.setMinHeight(typedArray.getDimensionPixelSize(2 /* index of android.R.attr.listPreferredItemHeightSmall in attributeNamesArray*/, 20));
	
	 
	 // Finally free resources used by TypedArray
	 typedArray.recycle();

    }

   

}
