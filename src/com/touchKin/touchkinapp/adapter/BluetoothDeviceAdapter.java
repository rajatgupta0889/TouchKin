package com.touchKin.touchkinapp.adapter;

import java.util.List;

import com.touchKin.touchkinapp.Interface.ButtonClickListener;
import com.touchKin.touchkinapp.adapter.BluetoothDeviceAdapter.ViewHolder;
import com.touchKin.touchkinapp.custom.RoundedImageView;
import com.touchKin.touchkinapp.model.AddCircleModel;
import com.touchKin.touchkinapp.model.BluetoothDeviceModel;
import com.touchKin.touckinapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class BluetoothDeviceAdapter extends BaseAdapter {
	List<BluetoothDeviceModel> devices;
	Context context;
	LayoutInflater inflater;
	ButtonClickListener buttonListener;

	public BluetoothDeviceAdapter(List<BluetoothDeviceModel> devices, Context context) {
		super();
		this.devices = devices;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return devices.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return devices.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public static class ViewHolder {
		TextView deviceName;
		CheckBox checkblestatus;
//		Button addKin, removeKin;
//		RoundedImageView userImage;
	}

	public void setCustomButtonListner(ButtonClickListener listener) {
		this.buttonListener = listener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		BluetoothDeviceModel item = (BluetoothDeviceModel) getItem(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.bluetooth_scan_list_item, null);
			viewHolder.deviceName = (TextView) convertView
					.findViewById(R.id.device_name);
			viewHolder.checkblestatus = (CheckBox) convertView.findViewById(R.id.checkBox);
//			viewHolder.addKin = (Button) convertView
//					.findViewById(R.id.addKinButton);
//			viewHolder.removeKin = (Button) convertView
//					.findViewById(R.id.removeKinButton);
//			viewHolder.userImage = (RoundedImageView) convertView
//					.findViewById(R.id.parentImage);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.deviceName.setText(item.getDeviceName());
		viewHolder.checkblestatus.setChecked(item.getCheck());
//		viewHolder.addKin.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				buttonListener.onButtonClickListner(position, "", true);
//
//			}
//		});
//		viewHolder.removeKin.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				buttonListener.onButtonClickListner(position, "", false);
//			}
//		});
		// viewHolder.userImage.setImageUrl("", AppController.getInstance()
		// .getImageLoader());
		return convertView;
	}

}
