package com.touchKin.touchkinapp;

import java.util.HashMap;

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
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.touchKin.touchkinapp.Interface.FragmentInterface;
import com.touchKin.touchkinapp.Interface.ViewPagerListener;
import com.touchKin.touchkinapp.adapter.DashBoardAdapter;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touckinapp.R;

public class Fragment1 extends Fragment implements OnClickListener {

	private ViewPager viewPager;
	public DashBoardAdapter adapter;
	// private CirclePageIndicator indicator;
	TextView sendTouch, getService;
	PageListener pageListener;
	ParentListModel parent;
	Vibrator vib;
	TextView sendTouchTextview;
	Boolean withoutMsg = false;
	Boolean isSendTouchAlreadyClicked = false;
	public static ViewPagerListener listener;

	public Fragment1() {
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActivity();
		vib = (Vibrator) this.getActivity().getSystemService(
				Context.VIBRATOR_SERVICE);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View v = inflater.inflate(R.layout.dashboard_fragment, null);
		init(v);
		DashBoardAdapter.context = getActivity();
		
		sendTouch.setOnClickListener(this);
		getService.setOnClickListener(this);
		return v;
	}

	public void init(View v) {
		// adapter = new DashBoardAdapter(getChildFragmentManager(), parent);
		viewPager = (ViewPager) v.findViewById(R.id.pager);
		// indicator = (CirclePageIndicator) v.findViewById(R.id.indicator);
		sendTouch = (TextView) v.findViewById(R.id.sendTouch);
		getService = (TextView) v.findViewById(R.id.getService);
		sendTouchTextview = (TextView) v.findViewById(R.id.textToSendTouch);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	//

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Add your menu entries here
		super.onCreateOptionsMenu(menu, inflater);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sendTouch:
			vib.vibrate(500);
			if (!isSendTouchAlreadyClicked) {
				viewPager.setCurrentItem(0);
				sendTouch.setText("Add a video");
				sendTouch.setCompoundDrawablesWithIntrinsicBounds(0,
						R.drawable.video_cam, 0, 0);
				listener.sendTouchCLicked(true);
				isSendTouchAlreadyClicked = true;
				new Handler().postDelayed(new Runnable() {

					/*
					 * Showing splash screen with a timer. This will be useful
					 * when you want to show case your app logo / company
					 */
					@Override
					public void run() {
						// This method will be executed once the timer is over
						// Start your app main activity
						if (!withoutMsg)
							sendTouchWithoutMessage();
						isSendTouchAlreadyClicked = false;
						sendTouch.setText("Send a Touch");
						sendTouch.setCompoundDrawablesWithIntrinsicBounds(0,
								R.drawable.ic_icon_send_touch, 0, 0);
						listener.sendTouchCLicked(false);

					}
				}, 10000);
			} else {
				withoutMsg = true;
				sendTouch();

			}

			// v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

			break;
		case R.id.getService:
			// v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
			if (parent != null) {
				Intent callIntent = new Intent(Intent.ACTION_DIAL);
				callIntent
						.setData(Uri.parse("tel:" + parent.getMobilenumber()));
				Intent chooser = Intent.createChooser(callIntent, "Call using");
				startActivity(chooser);
			} else {
				Toast.makeText(getActivity(),
						"Please Select Your parent to call", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		case R.id.textToSendTouch:

			withoutMsg = true;
			sendTouch();
			break;
		default:
			break;
		}
	}

	private void sendTouch() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), SendTouchActivity.class);
		intent.putExtra("userId", parent.getParentId());
		intent.putExtra("token", ((DashBoardActivity) getActivity()).getToken());
		startActivity(intent);
	}

	private void sendTouchWithoutMessage() {
		JSONObject params = new JSONObject();
		try {
			params.put("receivingUserId", parent.getParentId());

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

	private class PageListener extends SimpleOnPageChangeListener {
		public void onPageSelected(int position) {

			// SharedPreferences pref = getActivity().getSharedPreferences(
			// "countPref", 0);
			// if (!pref.getBoolean("count", false)) {
			// position = position - 1;
			// }
			// if (position == -1) {
			//
			// } else {
			// mTitle.setText(getResources().getStringArray(R.array.frag_titles)[position]);
			// }
			FragmentInterface fragment = (FragmentInterface) adapter
					.instantiateItem(viewPager, position);
			if (fragment != null) {
				fragment.fragmentBecameVisible();

			}

		}
	}

	public void notifyFrag() {
		parent = ((DashBoardActivity) getActivity()).getSelectedParent();
		adapter = new DashBoardAdapter(getChildFragmentManager(), parent);
		viewPager.setAdapter(adapter);
		pageListener = new PageListener();
		viewPager.setOnPageChangeListener(pageListener);
		adapter.notifyDataSetChanged();
	}

}
