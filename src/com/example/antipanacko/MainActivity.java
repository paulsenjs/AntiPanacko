package com.example.antipanacko;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.antipanacko.httpconnection.HttpConnectionTask;
import com.example.antipanacko.httpconnection.IHttpResponseListener;
import com.example.antipanacko.system.DeviceControlling;
import com.example.antipanacko.system.location.GPSTracker;
import com.example.antitheft.R;

public class MainActivity extends PreferenceActivity {

	private DeviceControlling lockScreen;
	private boolean isChecked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lockScreen = new DeviceControlling(this);
		lockScreen.enablingAdminMode();

		addPreferencesFromResource(R.xml.settings);
		setShowNotificationBar();
		
		final Preference mRecordSoundPref = findPreference("prefRecordSound");

		final Preference mChkPref = findPreference("prefCheckIfUsingSound");
		mChkPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				isChecked = (Boolean) newValue;

				// isChecked ?
				// mRecordSoundPref.setEnabled(true):mRecordSoundPref.setEnabled(false);

				if (isChecked) {
					mRecordSoundPref.setEnabled(true);
					Log.d("##", "Disabled");
				} else {
					mRecordSoundPref.setEnabled(false);
					Log.d("##", "Enable");
				}

				return isChecked;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean itemSelected = false;

		switch (item.getItemId()) {
		case R.id.clear_admin:
			// lockScreen.disablingAdminMode();
//			getMyLatLong();
			break;
		default:
			itemSelected = super.onOptionsItemSelected(item);
			break;
		}

		return itemSelected;
	}


	private void setShowNotificationBar() {

		NotificationCompat.Builder mNotifBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher).setContentTitle("Anti Panacko !!").setContentText("Hei you.. !!").setOngoing(true);

		Intent resultIntent = new Intent(this, MainActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);

		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mNotifBuilder.setContentIntent(resultPendingIntent);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(100, mNotifBuilder.build());
	}

}
