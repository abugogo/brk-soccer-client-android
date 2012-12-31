package com.soccer.rest;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class LoopjRestClient {
	private static AsyncHttpClient client = null;

	private LoopjRestClient() {

	}

	public static synchronized AsyncHttpClient getAsyncInst(Context ctxt) {
		// context should be one that is known and used by all subsequent calls otherwise the cookie will not be fetched (it is used as the key)
		if (client == null) {
			client = new AsyncHttpClient();
			PersistentCookieStore myCookieStore = new PersistentCookieStore(ctxt);
			client.setCookieStore(myCookieStore);
		}
		return client;
	}

	public static void get(Context ctxt, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		getAsyncInst(ctxt).get(url, params, responseHandler);
	}

	public static void post(Context ctxt, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {

		getAsyncInst(ctxt).post(url, params, responseHandler);
	}

	public static void put(Context ctxt, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {

		getAsyncInst(ctxt).put(url, params, responseHandler);
	}

}
