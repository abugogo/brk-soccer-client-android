package com.soccer.dialog;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * helper for Prompt-Dialog creation
 */
public abstract class MultiSelectListDialog extends AlertDialog.Builder
		implements OnClickListener {

	private ArrayList<lstItem> mList = null;
	private final String RIVAL = "* "; 
	public MultiSelectListDialog(Context context, int title, int message,
			ArrayList<lstItem> lst, ArrayList<String> lstRivalIds) {
		super(context);
		// setTitle(title);

		mList = lst;
		int numItems = lst.size();
		CharSequence[] data = new CharSequence[numItems];
		final boolean[] chk = new boolean[numItems];

		for (int i = 0; i < numItems; i++) {
			lstItem nv = (lstItem) lst.get(i);
			data[i] = (lstRivalIds.contains(nv.mId))? RIVAL + nv.mText:nv.mText;
			chk[i] = nv.mChecked;
		}

		setMultiChoiceItems(data, chk,
				new DialogInterface.OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton, boolean isChecked) {
						if (((AlertDialog) dialog).getListView().getItemAtPosition(whichButton).toString().contains(RIVAL)) {
							chk[whichButton] = false;
							((AlertDialog) dialog).getListView().setItemChecked(whichButton, false);
							Toast.makeText(
									((AlertDialog) dialog).getContext(),
									"Player plays for the rival team and cannot be selected",
									Toast.LENGTH_SHORT).show();
						}
					}
				});

		setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				ListView list = ((AlertDialog) dialog).getListView();
				int listCount = list.getCount();
				for (int i = 0; i < listCount; i++) {
					boolean checked = list.isItemChecked(i);
					mList.get(i).mChecked = checked;
				}
				onOkClicked("");
			}
		});

		setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				onCancelClicked(dialog);
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
