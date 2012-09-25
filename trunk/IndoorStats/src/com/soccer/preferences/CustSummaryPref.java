package com.soccer.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

import com.soccer.indoorstats.R;

public class CustSummaryPref extends EditTextPreference {
	private String _summaryTemplate;
	
	public CustSummaryPref(Context context) {
		super(context);
	}

	public CustSummaryPref(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray att=context.obtainStyledAttributes(attrs, R.styleable.CustSummaryPref);
		_summaryTemplate=att.getString(R.styleable.CustSummaryPref_summary_template);

	}

	@Override
	public CharSequence getSummary() {
		return String.format(_summaryTemplate, getText());
	}
}
