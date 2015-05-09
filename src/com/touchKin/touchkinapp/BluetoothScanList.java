package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.touchKin.touchkinapp.Interface.ButtonClickListener;
import com.touchKin.touchkinapp.adapter.BluetoothDeviceAdapter;
import com.touchKin.touchkinapp.model.BluetoothDeviceModel;
import com.touchKin.touckinapp.R;

public class BluetoothScanList extends ActionBarActivity implements
		ButtonClickListener, OnItemClickListener {
	private Toolbar toolbar;
	TextView mTitle, skip, back;
	ListView bledevicelist;
	BluetoothDeviceAdapter adapter;
	List<BluetoothDeviceModel> deviceList;
	ImageButton addContactButton;
	Button next, previous;

	static final int PICK_CONTACT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.available_bluetooth_device_list);
		init();

		mTitle.setText("Add a device");
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(BluetoothScanList.this,
						DashBoardActivity.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(i, bndlanimation);
			}
		});
	
		deviceList
				.add(new BluetoothDeviceModel("123", "WristBand 0012", false));
		deviceList.add(new BluetoothDeviceModel("124", "Device 9660", false));
		deviceList.add(new BluetoothDeviceModel("125", "X-device 1234", false));
	
		adapter.notifyDataSetChanged();
		bledevicelist.setOnItemClickListener(this);
		bledevicelist.setAdapter(adapter);
	}

	private void init() {
		// TODO Auto-generated method stub
		bledevicelist = (ListView) findViewById(R.id.ble_device_list);
		deviceList = new ArrayList<BluetoothDeviceModel>();
		adapter = new BluetoothDeviceAdapter(deviceList, this);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
		next = (Button) findViewById(R.id.next_button);
		previous = (Button) findViewById(R.id.previous_button);

	}

	@Override
	public void onButtonClickListner(int position, String value,
			Boolean isAccept) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		BluetoothDeviceModel item = deviceList.get(position);

		for (BluetoothDeviceModel data : deviceList) {

			if (data.equals(item)) {
				data.setCheck(true);
			} else {
				data.setCheck(false);
			}

			bledevicelist.setAdapter(adapter);

		}
		Intent i = new Intent(BluetoothScanList.this, DashBoardActivity.class);
		Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
				getApplicationContext(), R.anim.animation, R.anim.animation2)
				.toBundle();
		startActivity(i, bndlanimation);

	}

}
