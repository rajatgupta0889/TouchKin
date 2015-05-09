package com.touchKin.touchkinapp;


import com.skyfishjy.library.RippleBackground;
import com.touchKin.touchkinapp.adapter.BluetoothDeviceAdapter;
import com.touchKin.touchkinapp.model.BluetoothDeviceModel;
import com.touchKin.touckinapp.R;

import android.app.ActivityOptions;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BluetoothScan extends ActionBarActivity {

	Button scan;
	private BluetoothAdapter mBluetoothAdapter;
	private static final int REQUEST_ENABLE_BT = 1;
	private static final int SCAN_PERIOD = 10000;
	BluetoothDeviceAdapter adapter;
	Handler mHandler;
	private boolean mScanning;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_scan);
		final RippleBackground rippleBackground = (RippleBackground) findViewById(R.id.content);
		final Handler handler = new Handler();
		scan = (Button) findViewById(R.id.bluetooth_scan);
		
		
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(BluetoothScan.this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(BluetoothScan.this,
					DashBoardActivity.class);
			Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
					getApplicationContext(), R.anim.animation,
					R.anim.animation2).toBundle();
			startActivity(i, bndlanimation);
            finish();
            
        }
		final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(BluetoothScan.this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
		
		scan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				rippleBackground.startRippleAnimation();
				
//				adapter = new BluetoothDeviceAdapter(null, null);
//		        setListAdapter(adapter);
				 scanLeDevice(true);
				
//				handler.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						// foundDevice();
//						rippleBackground.stopRippleAnimation();
//						
//
//						Intent i = new Intent(BluetoothScan.this,
//								BluetoothScanList.class);
//						Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
//								getApplicationContext(), R.anim.animation,
//								R.anim.animation2).toBundle();
//						startActivity(i, bndlanimation);
//					}
//				}, 6000);
			}
		});
	}
	
	 @Override
	    protected void onResume() {
	        super.onResume();

	        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
	        // fire an intent to display a dialog asking the user to grant permission to enable it.
	        if (!mBluetoothAdapter.isEnabled()) {
	            if (!mBluetoothAdapter.isEnabled()) {
	                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
	            }
	        }

	        
	       
	        
	    }
	 
	    private void scanLeDevice(final boolean enable) {
	        if (enable) {
	            // Stops scanning after a pre-defined scan period.
	            mHandler.postDelayed(new Runnable() {
	                @Override
	                public void run() {
	                    mScanning = false;
	                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
	                    invalidateOptionsMenu();
	                }
	            }, SCAN_PERIOD);

	            mScanning = true;
	            mBluetoothAdapter.startLeScan(mLeScanCallback);
	        } else {
	            mScanning = false;
	            mBluetoothAdapter.stopLeScan(mLeScanCallback);
	        }
	        invalidateOptionsMenu();
	    }
	    
	    private BluetoothAdapter.LeScanCallback mLeScanCallback =
	            new BluetoothAdapter.LeScanCallback() {

	        public void onLeScan(final BluetoothDeviceModel device, int rssi, byte[] scanRecord) {
	            runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                	adapter.addDevice(device);
	                	adapter.notifyDataSetChanged();
	                }
	            });
	        }

			@Override
			public void onLeScan(BluetoothDevice device, int rssi,
					byte[] scanRecord) {
				// TODO Auto-generated method stub
				
			}
	    };

}
