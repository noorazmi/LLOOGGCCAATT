package com.example.catlogttest;

import java.util.Arrays;

import com.gl.logcat.activities.LogcatViewActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
//http://www.techotopia.com/index.php/Creating_and_Managing_Overflow_Menus_on_Android
public class MainActivity extends Activity {
    //public static final String[] inAppPurcahseListenerCallbackNames  = {"onInitFinish","onQueryFinish","onBillingFinish","onBeforeDownload","onAfterDownload","onBeforeApply","onAfterApply"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logcat_view_activity);
		Intent intent = new Intent(this, LogcatViewActivity.class);
		startActivity(intent);
		//Log.e("CMCC_IAP_AndroidModule"," android Wrong callback method name(ss).The callback method names of listener must be "+ Arrays.toString(inAppPurcahseListenerCallbackNames));
			
		    
	}


}
