package com.soccer.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.soccer.indoorstats.utils.log.Logger;
import com.soccer.preferences.Prefs;

import android.content.Context;

public class RestClient {

	public enum RequestMethod {
		GET, POST, PUT
	}

	protected void onPostExecute(String result) {
	}

	private String jsonBody;
	private ArrayList<NameValuePair> params;
	private ArrayList<NameValuePair> headers;

	private String url;
	private boolean mProxyEnabled;
	private String mProxyServer;
	private int mProxyPort;
	private int responseCode;
	private String message;

	private String response;

	public String getResponse() {
		return response;
	}

	public String getErrorMessage() {
		return message;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public RestClient(String url, Context ctxt) {
		this.url = url;
		params = new ArrayList<NameValuePair>();
		jsonBody = new String("");
		headers = new ArrayList<NameValuePair>();
		Prefs prefs = new Prefs(ctxt);
		mProxyEnabled = prefs.getBoolPreference("use_proxy", false);
		mProxyServer = prefs.getPreference("proxy_server", "");
		mProxyPort = Integer.parseInt(prefs.getPreference("proxy_port", "0"));
		
	}

	public void AddParam(String name, String value) {
		params.add(new BasicNameValuePair(name, value));
	}

	public void setJSonBody(String value) {
		jsonBody = value;
	}

	public void AddHeader(String name, String value) {
		headers.add(new BasicNameValuePair(name, value));
	}

	public void ExecuteCall(RequestMethod method) throws Exception {
		switch (method) {
		case GET: {
			// add parameters
			String combinedParams = "";
			if (!params.isEmpty()) {
				combinedParams += "?";
				for (NameValuePair p : params) {
					String paramString = p.getName() + "="
							+ URLEncoder.encode(p.getValue(), "UTF-8");
					if (combinedParams.length() > 1) {
						combinedParams += "&" + paramString;
					} else {
						combinedParams += paramString;
					}
				}
			}

			HttpGet request = new HttpGet(url + combinedParams);

			// add headers
			for (NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
			}

			executeRequest(request, url);
			break;
		}
		case POST: {
			HttpPost request = new HttpPost(url);
			// add headers
			for (NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
			}
			JSONObject jo;
			if (jsonBody != "")
				jo = new JSONObject(jsonBody);
			else
				jo = new JSONObject();
			if (!params.isEmpty()) {
				for (int i = 0; i < params.size(); i++) {
					jo.put(params.get(i).getName(), params.get(i).getValue());
				}
			}
			StringEntity se = new StringEntity(jo.toString());
			request.setEntity(se);
			executeRequest(request, url);
			break;
		}
		case PUT: {
			HttpPut request = new HttpPut(url);
			// add headers
			for (NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
			}
			JSONObject jo;
			if (jsonBody != "")
				jo = new JSONObject(jsonBody);
			else
				jo = new JSONObject();
			if (!params.isEmpty()) {
				for (int i = 0; i < params.size(); i++) {
					jo.put(params.get(i).getName(), params.get(i).getValue());
				}
			}
			StringEntity se = new StringEntity(jo.toString());
			request.setEntity(se);
			executeRequest(request, url);
			break;
		}
		}
	}

	private void executeRequest(HttpUriRequest request, String url) {
		// HttpClient client = new DefaultHttpClient();
		HttpClient client = HttpClientFactory.getThreadSafeClient();
		boolean useproxy = (mProxyEnabled && mProxyServer != null && !mProxyServer.equals("") && mProxyPort > 0);
		if (useproxy) {
			HttpHost proxy = new HttpHost(mProxyServer, mProxyPort);
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
			//System.setProperty("http.proxyHost", mProxyServer);
			//System.setProperty("http.proxyPort", Integer.toString(mProxyPort));
		}
		HttpResponse httpResponse;

		try {
			httpResponse = client.execute(request);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			message = httpResponse.getStatusLine().getReasonPhrase();

			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {

				InputStream instream = entity.getContent();
				response = convertStreamToString(instream);

				// Closing the input stream will trigger connection release
				instream.close();
			}

		} catch (ClientProtocolException e) {
			client.getConnectionManager().shutdown();
			Logger.e("restclient failed ClientProtocolException ", e);
		} catch (IOException e) {
			client.getConnectionManager().shutdown();
			Logger.e("restclient failed IOException", e);
		}
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			Logger.e("restclient failed convertStreamToString due to ioexception", e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Logger.e("restclient failed convertStreamToString due to ioexception in close", e);
			}
		}
		return sb.toString();
	}

}
