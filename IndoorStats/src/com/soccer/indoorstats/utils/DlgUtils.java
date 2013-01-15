package com.soccer.indoorstats.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class DlgUtils {
	final private static String MSG_KEY = "MSG_KEY";
	
	public static Bundle prepareDlgBundle(String msg) {
		Bundle args = new Bundle();
		args.putString(MSG_KEY, msg);
		return args;
	}
	public static Dialog createAlertMessage(Activity act, Bundle args) {
		AlertDialog alertDialog = new AlertDialog.Builder(act).create();
		alertDialog.setMessage(args.getString(MSG_KEY));
		return alertDialog;
	}
	
	public static void showAlertMessage(Context ctxt, String title, String msg) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ctxt);
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(msg);
		alertDialogBuilder.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int id) {
						dialog.cancel();
					}
				});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
}
