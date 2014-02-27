package com.gl.logcat.dialogs;


import com.example.catlogttest.R;

import android.app.AlertDialog;
import android.content.Context;

public class DialogInfoOK extends AlertDialog.Builder
{

    public DialogInfoOK(Context context, String dialogTitle, String message, String okButtomTitle)
    {
	super(context);
	setIcon(R.drawable.brand_icon);
	setTitle(dialogTitle);
	setMessage(message);
	setPositiveButton(okButtomTitle, null);
	
    }
//    
//    public static AlertDialog getInstance(Context context, String dialogTitle, String message, String okButtomTitle)
//    {
//	return new DialogInfoOK(context, dialogTitle, message ,okButtomTitle).create();
//    }
    
}
