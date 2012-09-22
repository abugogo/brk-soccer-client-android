package com.soccer.dialog;

import java.io.Serializable;

public class lstItem implements Serializable{
	private static final long serialVersionUID = 6983509531169847290L;
	public String mText;
	public String mId;
	public boolean mChecked;

	public lstItem(String text, String id, boolean checked) {
		mText = text;
		mId = id;
		mChecked = checked;

	}
	
	@Override
    public String toString() {
        return this.mText;
    }
}