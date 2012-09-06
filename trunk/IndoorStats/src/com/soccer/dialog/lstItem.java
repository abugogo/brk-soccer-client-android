package com.soccer.dialog;

public class lstItem {
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