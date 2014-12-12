package com.example.antipanacko.httpconnection;

public interface IHttpResponseListener {
	public void resultSuccess(int type, String result);
	public void resultFailed(int type, String strError);
}
