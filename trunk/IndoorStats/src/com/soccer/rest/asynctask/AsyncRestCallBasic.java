package com.soccer.rest.asynctask;

import com.soccer.indoorstats.activity.i.IAsyncTaskAct;
import com.soccer.rest.RestClient;

public class AsyncRestCallBasic extends AsyncRestCall {

	public AsyncRestCallBasic(IAsyncTaskAct act, String status) {
		super(act, status);
	}

	@Override
	protected void onPostExecute(RestClient c) {
		// TODO Auto-generated method stub
		super.onPostExecute(c);
		if(c.getResponseCode() == 200) {
			mAct.onSuccess(c.getResponse());
		}
		else 
			mAct.onFailure(c.getResponseCode(), c.getErrorMessage());
	}
	
	

}
