package com.touchKin.touchkinapp.services;

import com.touchKin.touckinapp.R;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class DeviceAcivityService extends Service {

	TelephonyManager Tel;
	MyPhoneStateListener MyListener;
	public int level;
	public int battery;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		fecthbattery();
		fetchwifi();
		fetchsignal();
		Toast.makeText(getApplicationContext(),
				fetchsignal() + " " + battery + " " + fetchwifi(),
				Toast.LENGTH_SHORT).show();
	}

	private int fecthbattery() {
		// TODO Auto-generated method stub
		registerReceiver(this.mBatInfoReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		return battery;

	}

	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context ctxt, Intent intent) {
			battery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			Toast.makeText(getApplicationContext(), battery+"", Toast.LENGTH_SHORT);
		}
	};

	private float fetchsignal() {
		// TODO Auto-generated method stub
		MyListener = new MyPhoneStateListener();
		Tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		Tel.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		Toast.makeText(getApplicationContext(), level+"1", Toast.LENGTH_SHORT);
		return level;
	}

	private class MyPhoneStateListener extends PhoneStateListener {
		/*
		 * Get the Signal strength from the provider, each tiome there is an
		 * update
		 */

		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);
			level = signalStrength.getGsmSignalStrength();
			Toast.makeText(getApplicationContext(), level+"", Toast.LENGTH_SHORT);
		}
	}

	private float fetchwifi() {
		// TODO Auto-generated method stub
		WifiManager wifi = (WifiManager) getSystemService(getApplicationContext().WIFI_SERVICE);
		int numberOfLevels = 5;
		WifiInfo wifiInfo = wifi.getConnectionInfo();
		int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(),
				numberOfLevels);
		float val = ((float) level / numberOfLevels) * 100f;
		Toast.makeText(getApplicationContext(), val+"", Toast.LENGTH_SHORT);
		return val;
		

	}

}
