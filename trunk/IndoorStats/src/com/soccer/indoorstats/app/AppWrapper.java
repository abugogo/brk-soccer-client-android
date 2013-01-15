package com.soccer.indoorstats.app;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

/*@ReportsCrashes(formKey = "dEJTa0pPVVI3ZEFaQ2EwS1RuT0lDQ2c6MQ")*/
public class AppWrapper extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		// The following line triggers the initialization of ACRA
		//ACRA.init(this);
	}
}
