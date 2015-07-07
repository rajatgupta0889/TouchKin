package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.touchKin.touchkinapp.Interface.ButtonClickListener;
import com.touchKin.touchkinapp.adapter.MyDashbaordAdapter;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touckinapp.R;

public class Fragment2 extends Fragment implements ButtonClickListener,
		OnClickListener {
	List<ParentListModel> list;
	ViewPager myPager;
	ParentListModel parent;
	Vibrator vib;
	TextView sendTouch, getService;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View v = inflater.inflate(R.layout.mydashboard, null);
		vib = (Vibrator) this.getActivity().getSystemService(
				getActivity().VIBRATOR_SERVICE);
		myPager = (ViewPager) v.findViewById(R.id.myPager);
		list = new ArrayList<ParentListModel>();
		((DashBoardActivity) getActivity()).setCustomButtonListner(this);
		sendTouch = (TextView) v.findViewById(R.id.sendTouch);
		getService = (TextView) v.findViewById(R.id.getService);
		return v;
	}

	@Override
	public void onButtonClickListner(int position, String value,
			Boolean isAccept) {
		// TODO Auto-generated method stub
		list = ((DashBoardActivity) getActivity()).getParentList();
		Log.d("Parent lIst", list + "");
		MyDashbaordAdapter adapter = new MyDashbaordAdapter(getActivity(), list);

		myPager.setAdapter(adapter);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		list = ((DashBoardActivity) getActivity()).getParentList();
		if (list != null && list.size() > 0) {
			MyDashbaordAdapter adapter = new MyDashbaordAdapter(getActivity(),
					list);
			myPager.setAdapter(adapter);
		}
		Log.d("pager pos", myPager.getCurrentItem() + "");
		sendTouch.setOnClickListener(this);
		getService.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sendTouch:
			vib.vibrate(500);
			// v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
			if (myPager.getCurrentItem() > 0)
				sendTouch();
			else {
				Toast.makeText(getActivity(),
						"You can not send touch to yourzself",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.getService:

			// v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
			if (list != null)
				parent = list.get(myPager.getCurrentItem());

			if (parent != null) {
				if (myPager.getCurrentItem() > 0) {

					Intent callIntent = new Intent(Intent.ACTION_DIAL);
					callIntent.setData(Uri.parse("tel:"
							+ parent.getMobilenumber()));
					Intent chooser = Intent.createChooser(callIntent,
							"Call using");
					startActivity(chooser);
				} else {
					Toast.makeText(getActivity(), "You can not call yourself",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getActivity(),
						"Please Select Your parent to call", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		default:
			break;
		}
	}

	private void sendTouch() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), SendTouchActivity.class);
		ParentListModel model = parent = list.get(myPager.getCurrentItem());
		intent.putExtra("userId", model.getParentId());
		startActivity(intent);
	}

}
