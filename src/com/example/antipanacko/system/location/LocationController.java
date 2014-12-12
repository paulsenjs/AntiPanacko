package com.example.antipanacko.system.location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

import com.example.antipanacko.httpconnection.HttpConnectionTask;
import com.example.antipanacko.httpconnection.IHttpResponseListener;

public class LocationController implements IHttpResponseListener{
	private GPSTracker gpsTracker;
	private String phoneNum;
	
	public LocationController(Activity act) {
		gpsTracker = new GPSTracker(act);
	}
	
	public LocationController(Context ctx, String phoneNum) {
		gpsTracker = new GPSTracker(ctx);
		this.phoneNum = phoneNum;
	}

	public void getMyLatLong() {
		if (gpsTracker.canGetLocation()) {
			Log.d("##", "Latitude : " + gpsTracker.getLatitude() + " Longitude : " + gpsTracker.getLongitude() + "Location : " + gpsTracker.getLocation());

			String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + gpsTracker.getLatitude() + "," + gpsTracker.getLongitude() + "&sensor=true";
			getMyLocation(url);
		}
	}

	private void getMyLocation(String url) {
		final HttpConnectionTask httpConnectionTask = new HttpConnectionTask(this, 100, "GET");
		httpConnectionTask.execute(url);
	}
	
	@Override
	public void resultSuccess(int type, String result) {
		try {
			String locationName = null;
			JSONObject jObj = new JSONObject(result);
			JSONArray jArr = jObj.getJSONArray("results");

			JSONObject jObjLocation = jArr.getJSONObject(0);
			locationName = jObjLocation.optString("formatted_address");

			Log.d("##", "Location Name " + locationName);

			GPSTracker.setStrLocation(locationName);
			sendSmsMessage(phoneNum, locationName);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public void resultFailed(int type, String strError) {
		Log.d("##", "resultFailed " + strError);
	}
	
	private void sendSmsMessage(String phoneNumber, String message) {
		try {
			Log.d("##", "Send SMS from "+ phoneNumber +" Location "+message);
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNumber, null, "Last location : "+message, null, null);			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
