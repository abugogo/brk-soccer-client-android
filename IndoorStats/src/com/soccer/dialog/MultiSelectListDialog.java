package com.soccer.dialog;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.soccer.indoorstats.R;

/**
 * helper for Prompt-Dialog creation
 */
public abstract class MultiSelectListDialog extends AlertDialog.Builder implements
		OnClickListener {
	/**
	 * @param context
	 * @param title
	 *            resource id
	 * @param message
	 *            resource id
	 */
	public MultiSelectListDialog(Context context, int title, int message, CharSequence data[]) {
		super(context);
		//setTitle(title);

		setMultiChoiceItems(data, new boolean[] { false, true,
				false }, new DialogInterface.OnMultiChoiceClickListener() {
			public void onClick(DialogInterface dialog, int whichButton,
					boolean isChecked) {

			}
		});
		
		setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				ListView list = ((AlertDialog) dialog).getListView();

				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < list.getCount(); i++) {
					boolean checked = list.isItemChecked(i);

					if (checked) {
						if (sb.length() > 0)
							sb.append(", ");
						sb.append(list.getItemAtPosition(i));
					}
				}
			}
		});
		
		setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});

		
	}

	/**
	 * will be called when "cancel" pressed. closes the dialog. can be
	 * overridden.
	 * 
	 * @param dialog
	 */
	public void onCancelClicked(DialogInterface dialog) {
		dialog.dismiss();
	}


	/**
	 * called when "ok" pressed.
	 * 
	 * @param input
	 * @return true, if the dialog should be closed. false, if not.
	 */
	abstract public boolean onOkClicked(String input);
}
