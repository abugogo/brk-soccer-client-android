package com.soccer.dialog;

import java.util.Set;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.soccer.indoorstats.activity.i.StrListDialogAct;

public class ListDialog extends AlertDialog.Builder {
	private CharSequence list[] = null;
	private String title = "";
	private Context cont = null;

	public ListDialog(Context ctxt, String ttl, Set<CharSequence> opts) {
		super(ctxt);
		cont = ctxt;
		list = new CharSequence[opts.size()];
		list = opts.toArray(list);
		title = ttl;
	}

	@Override
	public AlertDialog create() {
		setTitle(title);
		setItems(list, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				CharSequence str = list[which];
				((StrListDialogAct)cont).StringSelected(str);
			}
		});
		return super.create();
	}

}
