package com.gl.logcat.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.catlogttest.R;

public class SingleInputDismissOkDialog extends  AlertDialog.Builder
{
    
    private static EditText editText;
    
    public SingleInputDismissOkDialog(Context context,String dialogTitle, String initialInput, String okButtomTitle,String dismissButtonTitle,final DialogOkButtonClickListener dialogOkButtonClickListener)
    {
	super(context);
	setCancelable(false);
	LayoutInflater inflater = LayoutInflater.from(context);
	final View layoutView = inflater.inflate(R.layout.single_input_ok_cancel_dialog, null);
	setView(layoutView);
	setIcon(R.drawable.brand_icon);
	setTitle(dialogTitle);
	editText = (EditText) layoutView.findViewById(R.id.file_name_edit);
	editText.setText(initialInput);
	setPositiveButton(okButtomTitle, null);//pass null at place of listener so that the dialog could not be dismissed until we call alertdialog.dismiss().
	
	setNegativeButton(dismissButtonTitle, null);
	
	
	
    }

    public static AlertDialog getInstance(Context context, String dialogTitle, String initialInput, String okButtomTitle, String dismissButtonTitle, final DialogOkButtonClickListener dialogOkButtonClickListener)
    {
	final AlertDialog alertDialog = new SingleInputDismissOkDialog(context, dialogTitle, initialInput, okButtomTitle, dismissButtonTitle, dialogOkButtonClickListener).create();
	//you can add an onShowListener to the AlertDialog where you can then override the onClickListener of the positive button and prevent the dialog to be closed its own until you dismiss it.
	alertDialog.setOnShowListener(new OnShowListener()
	{

	    @Override
	    public void onShow(DialogInterface dialog)
	    {
		Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
		b.setOnClickListener(new View.OnClickListener()
		{
		    @Override
		    public void onClick(View view)
		    {
			dialogOkButtonClickListener.onDialogOkButtonClick(editText.getText().toString().trim());
		    }
		});
	    }
	});
	return alertDialog;
    }

}
