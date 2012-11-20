package com.soccer.rest.asynctask;

import org.apache.http.NameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.soccer.indoorstats.activity.i.IAsyncTaskAct;
import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.rest.RestClient;

public class AsyncRestCall extends AsyncTask<AsyncRestCallParams, Void, RestClient> {

	private ProgressDialog mDialog;
	protected IAsyncTaskAct mAct = null;
	protected String mStatusMessage = "";

	public AsyncRestCall(IAsyncTaskAct act, String status) {
		mAct = act;
		mStatusMessage = status;
	}

	@Override
	protected void onPreExecute() {
		this.mDialog = new ProgressDialog((Context) mAct);
		this.mDialog.setMessage(mStatusMessage);
		this.mDialog.show();
	}

	@Override
	protected RestClient doInBackground(AsyncRestCallParams... params) {
		RestClient c = new RestClient(params[0].getmUrl(), params[0].getContext());
		if (params.length == 1) {
			if(params[0].getmHeaders() != null){
				for(NameValuePair nv:params[0].getmHeaders())
					c.AddHeader(nv.getName(), nv.getValue());
			}
			try {
				c.setJSonBody(params[0].getBody());
				c.ExecuteCall(params[0].getmMethod());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Logger.e("Asyncrestcall failed", e);
			}
			
			
		}
		return c;
	}

	@Override
	protected void onPostExecute(RestClient c) {
		if (this.mDialog.isShowing())
			this.mDialog.dismiss();
	}

}
