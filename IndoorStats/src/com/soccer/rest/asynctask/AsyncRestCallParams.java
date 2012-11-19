package com.soccer.rest.asynctask;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.content.Context;

import com.soccer.rest.RestClient.RequestMethod;

public class AsyncRestCallParams {
	private String mUrl;
	private RequestMethod mMethod;
	private ArrayList<NameValuePair> mHeaders;
	private String mBody;
	private Context mCtxt;

	public AsyncRestCallParams(String url, RequestMethod method,
			ArrayList<NameValuePair> headers, String body, Context ctxt) {
		super();
		this.mUrl = url;
		this.mMethod = method;
		this.mHeaders = headers;
		this.mBody = body;
		this.mCtxt = ctxt;
	}

	public String getBody() {
		return mBody;
	}

	public void setBody(String body) {
		this.mBody = body;
	}

	public String getmUrl() {
		return mUrl;
	}

	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}

	public RequestMethod getmMethod() {
		return mMethod;
	}

	public void setmMethod(RequestMethod mMethod) {
		this.mMethod = mMethod;
	}

	public ArrayList<NameValuePair> getmHeaders() {
		return mHeaders;
	}

	public void setmHeaders(ArrayList<NameValuePair> mHeaders) {
		this.mHeaders = mHeaders;
	}
	
	public Context getContext() {
		return this.mCtxt;
	}

}
