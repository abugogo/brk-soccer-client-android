package com.soccer.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.soccer.indoorstats.R;

public class SoccerPrefsActivity extends PreferenceActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {        
        super.onCreate(savedInstanceState);        
        addPreferencesFromResource(R.xml.preferences);        
    }
}


