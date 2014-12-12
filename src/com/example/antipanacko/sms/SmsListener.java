package com.example.antipanacko.sms;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.antipanacko.system.DeviceControlling;
import com.example.antipanacko.system.PlaySound;
import com.example.antipanacko.system.location.LocationController;

public class SmsListener extends BroadcastReceiver {
	
	private DeviceControlling lockScreen;
	private PlaySound playSound;
	private ArrayList<String> arrData;
	private ArrayList<String> collectionArrData;
	private SmsMessage[] msgs;
	private String msgBody;
	private String msgFrom;
	private LocationController locationController;
	
	@Override
	public void onReceive(Context ctx, Intent intent) {
		
		lockScreen = new DeviceControlling(ctx);
		playSound = new PlaySound(ctx);
		arrData = new ArrayList<String>();
		collectionArrData = new ArrayList<String>();
		
		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
			Log.i(this.getClass().getName(), "Sms Text " + getSmsMessage(intent));
			
			String phoneNum = getSmsMessage(intent).get(0);
			String message = getSmsMessage(intent).get(1);
			
			if (message.equalsIgnoreCase(Config.CMD_LOCK)) {
				lockScreen.lockPhoneNow();				
			} else if (message.equalsIgnoreCase(Config.CMD_SOUND)) {
				playSound.startPlaying();
			} else if (message.equalsIgnoreCase(Config.CMD_LOCATION)) {
				locationController = new LocationController(ctx, phoneNum);
				locationController.getMyLatLong();
			}
		}
	}
	
	private ArrayList<String> getSmsMessage(Intent intent) {

		Bundle bundle = intent.getExtras();
		
		if (bundle != null) {
			try {
				Object[] pdus = (Object[]) bundle.get("pdus");
				msgs = new SmsMessage[pdus.length];
				
				for (int i=0; i<msgs.length; i++) {
					msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
					msgFrom = msgs[i].getOriginatingAddress();
					msgBody = msgs[i].getMessageBody();
					
					arrData.add(msgFrom);
					arrData.add(msgBody);
					
					collectionArrData.addAll(arrData);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	
		return collectionArrData;
	}
}
