package com.example.antipanacko.httpconnection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Hashtable;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class HttpConnectionTask extends AsyncTask<String, Void, String> {

	private Hashtable<String, String> mParams;
	private IHttpResponseListener mListener;
	private int type = 0;	
	private final String strConnectionError = "Http Connection error";
	private String method = "GET";
	
	public HttpConnectionTask(Activity context, Hashtable<String, String> params, IHttpResponseListener listener, int type) {
		this.mParams = params;
		this.mListener = listener;
		this.type = type;
		method = "POST";
		
	}
	
	public HttpConnectionTask(IHttpResponseListener listener, int type, String method) {
		this.mListener = listener;
		this.type = type;
		this.method = method;
	}

	@Override
	protected void onPreExecute() {

	}
	
	@Override
	protected String doInBackground(String... sUrls) {
		String result = null;
		Log.d(this.getClass().getName(), "doInBackground");
		
		if (mParams != null) {//POST
			try {
				result = HttpRequest.post(sUrls[0], mParams);
			} catch (UnsupportedEncodingException e) {
				result = strConnectionError + ", " + e.getMessage();
			} catch (MalformedURLException e) {
				result = strConnectionError + ", " + e.getMessage();
			} catch (IOException e) {
				result = strConnectionError + ", " + e.getMessage();
			}
		} else {//GET
			
			if (method.equals("DELETE")) {
				try {
					result = HttpRequest.delete(sUrls[0]);
				} catch (MalformedURLException e) {
					result = strConnectionError + ", " + e.getMessage();
				} catch (IOException e) {
					result = strConnectionError + ", " + e.getMessage();
				}
			} else {
				try {
					result = HttpRequest.get(sUrls[0]);
				} catch (MalformedURLException e) {
					result = strConnectionError + ", " + e.getMessage();
				} catch (IOException e) {
					result = strConnectionError + ", " + e.getMessage();
				}
			}
		}
		
		Log.d(this.getClass().getName(), "returning "+result);
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		
		if (mListener != null) {
			if (result.startsWith(strConnectionError)) {
				Log.d(this.getClass().getName(), "call resultFailed");
					mListener.resultFailed(type, result);
			}
			else {

				Log.d(this.getClass().getName(), "call resultSuccess");
					mListener.resultSuccess(type, result);
			}
		} else {
			Log.d(this.getClass().getName(), "mListener is null, how is that?");
		}

	}

}
