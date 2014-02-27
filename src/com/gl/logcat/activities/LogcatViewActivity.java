package com.gl.logcat.activities;

/*
 * Copyright 2013 The Android Open Source Project;
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ShareActionProvider;
import android.widget.Spinner;

import com.example.catlogttest.R;
import com.gl.logcat.adapter.DrawerListAdapter;
import com.gl.logcat.adapter.LogcatLinesAdapter;
import com.gl.logcat.fragments.FragmentName;
import com.gl.logcat.fragments.LogcatViewFragment;
import com.gl.logcat.fragments.SavedLogsFragment;
import com.gl.logcat.util.Utility;
import com.gl.logcat.views.OverFlowActionProvider;

/**
 * This example illustrates a common usage of the DrawerLayout widget in the
 * Android support library.
 * <p/>
 * <p>
 * When a navigation (left) drawer is present, the host activity should detect
 * presses of the action bar's Up affordance as a signal to open and close the
 * navigation drawer. The ActionBarDrawerToggle facilitates this behavior. Items
 * within the drawer should fall into one of two categories:
 * </p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic
 * policies as list or tab navigation in that a view switch does not create
 * navigation history. This pattern should only be used at the root activity of
 * a task, leaving some form of Up navigation active for activities further down
 * the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an
 * alternate parent for Up navigation. This allows a user to jump across an
 * app's navigation hierarchy at will. The application should treat this as it
 * treats Up navigation from a different task, replacing the current task stack
 * using TaskStackBuilder or similar. This is the only form of navigation drawer
 * that should be used outside of the root activity of a task.</li>
 * </ul>
 * <p/>
 * <p>
 * Right side drawers should be used for actions, not navigation. This follows
 * the pattern established by the Action Bar that navigation should be to the
 * left and actions to the right. An action should be an operation performed on
 * the current contents of the window, for example enabling or disabling a data
 * overlay on top of the current content.
 * </p>
 */
public class LogcatViewActivity extends android.support.v4.app.FragmentActivity implements SearchView.OnQueryTextListener
{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] drawerOptions;
    private SearchView mSearchView;
    private Spinner mSpinner;
    private ShareActionProvider mShareActionProvider;
    // http://www.edumobile.org/android/android-development/action-bar-search-view/
    private LogcatViewFragment logcatViewFragment;
    public static final int id_action_search = Utility.resId_action_search;
    private static final String TAG = LogcatViewActivity.class.getCanonicalName();
    private Resources res;
    public static final String CURRENT_LOGS = "Current Logs";
    public static final String SAVED_LOGS = "Saved Logs";

    private Fragment backStackFragment;

    /********************************************************************************************************
     * Activity Life Cycle Methods. onCreate() Already Defined.
     *********************************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	if (android.os.Build.VERSION.SDK_INT >= 14)
	{
	    setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
	}
	else if (android.os.Build.VERSION.SDK_INT >= 11)
	{
	    setTheme(android.R.style.Theme_Holo_Light);
	}
	else if (android.os.Build.VERSION.SDK_INT <= 10)
	{
	    setTheme(android.R.style.Theme_Light);
	}

	super.onCreate(savedInstanceState);
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	Utility.loadResourceIds(getApplicationContext());
	res = getResources();
	drawerOptions = res.getStringArray(R.array.drawer_options);
	setContentView(Utility.resId_logcat_view_activity);

	mTitle = mDrawerTitle = getTitle();
	mDrawerLayout = (DrawerLayout) findViewById(Utility.resId_drawer_layout);
	mDrawerList = (ListView) findViewById(Utility.resId_drawer_list);

	// set a custom shadow that overlays the main content when the drawer
	// opens
	mDrawerLayout.setDrawerShadow(Utility.resId_drawer_shadow, GravityCompat.START);

	// set up the drawer's list view with items and click listener
	// mDrawerList.setAdapter(new ArrayAdapter<String>(this,
	// Utility.resId_drawer_list_item, drawerOptions));
	mDrawerList.setAdapter(new DrawerListAdapter(this, Utility.resId_drawer_list_item, drawerOptions));
	mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

	// enable ActionBar app icon to behave as action to toggle nav drawer
	getActionBar().setDisplayHomeAsUpEnabled(true);
	getActionBar().setHomeButtonEnabled(true);

	// ActionBarDrawerToggle ties together the the proper interactions
	// between the sliding drawer and the action bar app icon
	mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
	mDrawerLayout, /* DrawerLayout object */
	Utility.resId_ic_drawer, /* nav drawer image to replace 'Up' caret */
	Utility.resId_drawer_open, /*
				    * "open drawer" description for
				    * accessibility
				    */
	Utility.resId_drawer_close /*
				    * "close drawer" description for
				    * accessibility
				    */
	)
	{
	    public void onDrawerClosed(View view)
	    {
		getActionBar().setTitle(mTitle);
		invalidateOptionsMenu(); // creates call to
					 // onPrepareOptionsMenu()
	    }

	    public void onDrawerOpened(View drawerView)
	    {
		getActionBar().setTitle(mDrawerTitle);
		invalidateOptionsMenu(); // creates call to
					 // onPrepareOptionsMenu()
	    }
	};
	mDrawerLayout.setDrawerListener(mDrawerToggle);

	if (savedInstanceState == null)
	{
	    selectItem(0);
	}

	logcatViewFragment = (LogcatViewFragment) getSupportFragmentManager().findFragmentByTag("LogcatViewFragment");
	if (logcatViewFragment == null)
	{
	    logcatViewFragment = new LogcatViewFragment();
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	    fragmentTransaction.add(Utility.resId_content_frame, logcatViewFragment, "LogcatViewFragment");
	    fragmentTransaction.commit();
	    invalidateOptionsMenu();

	}

    }

    @Override
    protected void onStart()
    {
	super.onStart();
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
    }

    @Override
    protected void onRestart()
    {
	super.onRestart();
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
    }

    @Override
    protected void onResume()
    {
	super.onResume();
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
    }

    @Override
    protected void onPause()
    {
	super.onPause();
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
    }

    @Override
    protected void onStop()
    {
	super.onStop();
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
    }

    @Override
    protected void onDestroy()
    {
	super.onDestroy();
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	getMenuInflater().inflate(Utility.resId_logcat_view_activity_menu, menu);

	MenuItem searchItem = menu.findItem(Utility.resId_action_search);
	SearchView searchView = new android.widget.SearchView(getActionBar().getThemedContext());
	searchItem.setActionView(searchView);

	searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	mSearchView = (SearchView) searchItem.getActionView();

	MenuItem filterItem = menu.findItem(Utility.resId_action_filter);
	filterItem.setActionView(Utility.resId_action_filter_layout);
	filterItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	mSpinner = (Spinner) filterItem.getActionView();
	mSpinner.setOnItemSelectedListener(logFilterSpinnerListener);
	mSearchView.setOnQueryTextListener(this);

	MenuItem shareMenuItem = menu.findItem(R.id.action_overflow);
	shareMenuItem.setActionProvider(new OverFlowActionProvider(this, R.layout.overflow_action_item_layout));

	return super.onCreateOptionsMenu(menu);
    }

    /************************************************************************************************************************************
     * Helper methods.
     *************************************************************************************************************************************/

    /** Returns a share intent */
    private Intent getShareIntent()
    {
	Intent intent = new Intent(Intent.ACTION_SEND);
	intent.setType("text/plain");
	intent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
	intent.putExtra(Intent.EXTRA_TEXT, "Extra Text");
	return Intent.createChooser(intent, "Choose");
    }

    OnItemSelectedListener logFilterSpinnerListener = new OnItemSelectedListener()
    {

	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int position, long id)
	{
	    LogcatLinesAdapter.setLogLevelLimit(position);
	    logcatViewFragment.filter(mSearchView.getQuery().toString());
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{

	}
    };

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
	// If the nav drawer is open, hide action items related to the content
	// view
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
	// menu.findItem(Utility.resId_action_search).setVisible(!drawerOpen);
	return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	Utility.printLog(TAG, new Exception().getStackTrace()[0].getMethodName() + "() called.");
	// The action bar home/up action should open or close the drawer.
	// ActionBarDrawerToggle will take care of this.
	if (mDrawerToggle.onOptionsItemSelected(item))
	{
	    return true;
	}
	// final int id_action_search = Utility.resId_action_search;
	// Handle action buttons
	int itemId = item.getItemId();
	if (itemId == id_action_search)
	{
	    // create intent to perform web search for this planet
	    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
	    intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
	    // catch event that there's no activity to handle intent
	    if (intent.resolveActivity(getPackageManager()) != null)
	    {
		startActivity(intent);
	    }
	    else
	    {
		// Toast.makeText(this,
		// R.string.app_not_available,Toast.LENGTH_LONG).show();
	    }
	    return true;

	}
	else if (itemId == R.id.action_pause_play)
	{
	    if (logcatViewFragment.isScrolling())
	    {
		logcatViewFragment.pauseScrolling();
		item.setIcon(R.drawable.ic_action_play);
	    }
	    else
	    {
		logcatViewFragment.startScrolling();
		item.setIcon(R.drawable.ic_action_pause);
	    }

	    return true;
	}
	else if (itemId == R.id.action_clear)
	{
	    logcatViewFragment.clearLog();
	    return true;
	}
	else
	{
	    return super.onOptionsItemSelected(item);
	}

    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
	    selectItem(position);
	    createFragment(drawerOptions[position]);

	}
    }

    private void createFragment(String selectedOption)
    {
	// Fragment fragment = null;
	FragmentManager fragmentManager = getSupportFragmentManager();
	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

	if (selectedOption.equals(CURRENT_LOGS))
	{
	    fragmentTransaction.remove(backStackFragment);
	    fragmentTransaction.commit();
	    fragmentManager.popBackStack();
	    invalidateOptionsMenu();

	}
	else if (selectedOption.equals(SAVED_LOGS))
	{
	     backStackFragment = new SavedLogsFragment(this);
	     fragmentTransaction.add(Utility.resId_content_frame,
	     backStackFragment,
	     FragmentName.SAVEDLOGS_FRAGMENT.getFragmentName());
	     fragmentTransaction.addToBackStack(FragmentName.SAVEDLOGS_FRAGMENT.getFragmentName());
	     fragmentTransaction.commit();
	     invalidateOptionsMenu();

	}

    }

    private void selectItem(int position)
    {

	// update selected item and title, then close the drawer
	mDrawerList.setItemChecked(position, true);
	setTitle(drawerOptions[position]);
	mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title)
    {
	mTitle = title;
	getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
	super.onPostCreate(savedInstanceState);
	// Sync the toggle state after onRestoreInstanceState has occurred.
	mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
	super.onConfigurationChanged(newConfig);
	// Pass any configuration change to the drawer toggls
	mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
	logcatViewFragment.onSearchTextChanged(newText);
	return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
	return false;
    }

    @Override
    public void onBackPressed()
    {
	finish();
	super.onBackPressed();
    }
    
    // This method is defined in saved_log_list_item.xml file. 
    public void openLogDetails(View v)
    {
	((SavedLogsFragment)backStackFragment).openLogDetails(v);
    }

}
