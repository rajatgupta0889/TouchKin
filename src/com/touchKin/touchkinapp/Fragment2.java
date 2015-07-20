package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.touchKin.touchkinapp.Interface.ButtonClickListener;
import com.touchKin.touchkinapp.Interface.ViewPagerListener;
import com.touchKin.touchkinapp.adapter.MyDashbaordAdapter;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touckinapp.R;

public class Fragment2 extends Fragment implements ButtonClickListener,
		OnClickListener {
	List<ParentListModel> list;
	ViewPager myPager;
	ParentListModel parent;
	Vibrator vib;
	TextView sendTouch, getService;
	TextView sendTouchTextview;
	Boolean withoutMsg = false;
	Boolean isSendTouchAlreadyClicked = false;
	public static ViewPagerListener listener;
	MyDashbaordAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View v = inflater.inflate(R.layout.mydashboard, null);
		getActivity();
		vib = (Vibrator) this.getActivity().getSystemService(
				Context.VIBRATOR_SERVICE);
		myPager = (ViewPager) v.findViewById(R.id.myPager);
		list = new ArrayList<ParentListModel>();
		((DashBoardActivity) getActivity()).setCustomButtonListner(this);
		sendTouch = (TextView) v.findViewById(R.id.sendTouch);
		getService = (TextView) v.findViewById(R.id.getService);
		sendTouchTextview = (TextView) v.findViewById(R.id.textToSendTouch);

		return v;
	}

	@Override
	public void onButtonClickListner(int position, String value,
			Boolean isAccept) {
		// TODO Auto-generated method stub
		list = ((DashBoardActivity) getActivity()).getParentList();
		Log.d("Parent lIst", list + "");
		adapter = new MyDashbaordAdapter(getActivity(), list);

		myPager.setAdapter(adapter);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		list = ((DashBoardActivity) getActivity()).getParentList();
		if (list != null && list.size() > 0) {
			adapter = new MyDashbaordAdapter(getActivity(), list);
			myPager.setAdapter(adapter);
		}
		sendTouch.setOnClickListener(this);
		getService.setOnClickListener(this);
		sendTouchTextview.setOnClickListener(this);

		myPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				sendTouch.setText("Send a Touch");
				sendTouch.setCompoundDrawablesWithIntrinsicBounds(0,
						R.drawable.ic_icon_send_touch, 0, 0);
				isSendTouchAlreadyClicked = false;

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sendTouch:
			if (myPager.getCurrentItem() > 0) {

				if (!isSendTouchAlreadyClicked) {
					vib.vibrate(500);
					sendTouch.setText("Add a video");
					sendTouch.setCompoundDrawablesWithIntrinsicBounds(0,
							R.drawable.video_cam, 0, 0);
					listener.sendTouchCLicked(true);
					isSendTouchAlreadyClicked = true;
					new Handler().postDelayed(new Runnable() {

						/*
						 * Showing splash screen with a timer. This will be
						 * useful when you want to show case your app logo /
						 * company
						 */
						@Override
						public void run() {
							// This method will be executed once the timer is
							// over
							// Start your app main activity
							if (!withoutMsg)
								sendTouchWithoutMessage();
							isSendTouchAlreadyClicked = false;
							sendTouch.setText("Send a Touch");
							sendTouch.setCompoundDrawablesWithIntrinsicBounds(
									0, R.drawable.ic_icon_send_touch, 0, 0);
							listener.sendTouchCLicked(false);

						}
					}, 10000);
				} else {
					withoutMsg = true;
					sendTouch();

				}
			} else {
				Toast.makeText(getActivity(),
						"You can not send touch to yourzself",
						Toast.LENGTH_SHORT).show();
			}
			// v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

			break;
		case R.id.getService:
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
		case R.id.textToSendTouch:
			if (myPager.getCurrentItem() > 0) {
				withoutMsg = true;
				sendTouch();
			} else {
				Toast.makeText(getActivity(),
						"You can not send touch to yourzself",
						Toast.LENGTH_SHORT).show();
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
		intent.putExtra("token", ((DashBoardActivity) getActivity()).getToken());
		startActivity(intent);
	}

	private void sendTouchWithoutMessage() {
		JSONObject params = new JSONObject();
		try {
			params.put("receivingUserId", list.get(myPager.getCurrentItem())
					.getParentId());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
				"http://54.69.183.186:1340/touch/add", params,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d("Activity Result", response.toString());
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d("Error", error.getMessage() + " ");

					}

				}) {
			public java.util.Map<String, String> getHeaders()
					throws com.android.volley.AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Authorization", "Bearer "
						+ ((DashBoardActivity) getActivity()).getToken());

				return headers;

			};
		};
		AppController.getInstance().addToRequestQueue(req);
	}

}
