package com.example.antipanacko.system;

import com.example.antipanacko.receiver.AdminReceiver;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DeviceControlling {
	private static final String description = "Description";
	private DevicePolicyManager devicePolicyManager;
	private ComponentName componentName;
	private Context context;
	
	public DeviceControlling(Context _ctx) {
		this.context = _ctx;
		
		devicePolicyManager = (DevicePolicyManager)_ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);
		componentName = new ComponentName(_ctx, AdminReceiver.class);
	}
	
	public void enablingAdminMode() {
		if (!devicePolicyManager.isAdminActive(componentName)) {
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, description);
			context.startActivity(intent);
		}
	}
	
	public void disablingAdminMode() {
		devicePolicyManager.removeActiveAdmin(componentName);
		Toast.makeText(context, "Admin removed", Toast.LENGTH_SHORT).show();
	}
	
	public void lockPhoneNow() {
		if (devicePolicyManager.isAdminActive(componentName)) {
			devicePolicyManager.lockNow();
		}else{
			Toast.makeText(context, "Not registered as admin", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void wipeAllData() {
		if (devicePolicyManager.isAdminActive(componentName)) {
			devicePolicyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
		}else{
			Toast.makeText(context, "Not registered as admin", Toast.LENGTH_SHORT).show();
		}
	}
}
