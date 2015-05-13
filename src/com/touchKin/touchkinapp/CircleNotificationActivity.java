package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.touchKin.touchkinapp.Interface.ButtonClickListener;
import com.touchKin.touchkinapp.adapter.RequestListAdapter;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touchkinapp.model.RequestModel;
import com.touchKin.touckinapp.R;

public class CircleNotificationActivity extends ActionBarActivity implements
		ButtonClickListener {
	ListView requestListView;
	RequestListAdapter adapter;
	List<RequestModel> requestList;
	private Toolbar toolbar;
	TextView mTitle, notifTv;
	Button skip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.request_list);
		init();
		// getConnectionRequest();
		adapter.setCustomButtonListner(this);
		mTitle.setText("Request");
		skip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(CircleNotificationActivity.this,
						AddCareActivity.class);

				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(i, bndlanimation);
				finish();
			}
		});

		if (getIntent().getExtras().getParcelableArrayList("request") != null) {
			requestList = getIntent().getExtras().getParcelableArrayList(
					"request");
		}
		Log.d("Request List Size", requestList.get(0).getCare_reciever_name() + "");
		adapter = new RequestListAdapter(requestList, this);
		adapter.notifyDataSetChanged();

	}

	// private void getConnectionRequest() {
	// // TODO Auto-generated method stub
	// JsonArrayRequest req = new JsonArrayRequest(
	// "http://54.69.183.186:1340/user/connection-requests",
	// new Listener<JSONArray>() {
	// @Override
	// public void onResponse(JSONArray responseArray) {
	// // TODO Auto-generated method stub
	// Log.d("Response Array", " " + responseArray);
	// if (responseArray.length() > 0) {
	// for (int i = 0; i < responseArray.length(); i++) {
	// try {
	// RequestModel item = new RequestModel();
	// JSONObject careRequest = responseArray
	// .getJSONObject(i);
	// JSONObject careInitiator = careRequest
	// .getJSONObject("initiator");
	// JSONObject careGiver = careRequest
	// .getJSONObject("care_giver");
	// JSONObject careReciever = careRequest
	// .getJSONObject("care_reciever");
	// if (careInitiator.getString("id").equals(
	// careGiver.get("id"))) {
	// if (careInitiator.has("nickname")) {
	// item.setCare_reciever_name(careInitiator
	// .getString("nickname"));
	// } else if (careInitiator
	// .has("first_name")) {
	// item.setCare_reciever_name(careInitiator
	// .getString("first_name"));
	// } else {
	// item.setCare_reciever_name(careInitiator
	// .getString("mobile"));
	// }
	// item.setReqMsg(item
	// .getCare_reciever_name()
	// + " wants to care for you");
	// }
	// if (careInitiator.get("id").equals(
	// careReciever.get("id"))) {
	// if (careInitiator.has("nickname")) {
	// item.setCare_reciever_name(careInitiator
	// .getString("nickname"));
	// } else if (careInitiator
	// .has("first_name")) {
	// item.setCare_reciever_name(careInitiator
	// .getString("first_name"));
	// } else {
	// item.setCare_reciever_name(careInitiator
	// .getString("mobile"));
	// }
	// item.setReqMsg(item
	// .getCare_reciever_name()
	// + " wants you to care for them");
	//
	// }
	// if (!careInitiator.get("id").equals(
	// careReciever.get("id"))
	// && !careInitiator
	// .getString("id")
	// .equals(careGiver.get("id"))) {
	// if (careInitiator.has("nickname")) {
	// item.setCare_reciever_name(careInitiator
	// .getString("nickname"));
	// } else if (careInitiator
	// .has("first_name")) {
	// item.setCare_reciever_name(careInitiator
	// .getString("first_name"));
	// } else {
	// item.setCare_reciever_name(careInitiator
	// .getString("mobile"));
	// }
	// item.setReqMsg(item
	// .getCare_reciever_name()
	// + " wants you to care for"
	// + careReciever
	// .getInt("nickname"));
	// }
	// // RequestModel item = new RequestModel();
	// //
	// item.setRequestID(careRequest
	// .getString("id"));
	// requestList.add(item);
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// } else {
	// Toast.makeText(CircleNotificationActivity.this,
	// "NO Request", Toast.LENGTH_SHORT).show();
	// notifTv.setVisibility(View.VISIBLE);
	//
	// }
	// adapter.notifyDataSetChanged();
	// requestListView.setAdapter(adapter);
	//
	// }
	//
	// }, new Response.ErrorListener() {
	// @Override
	// public void onErrorResponse(VolleyError error) {
	// VolleyLog.e("Error: ", error.getMessage());
	// Toast.makeText(CircleNotificationActivity.this,
	// error.getMessage(), Toast.LENGTH_SHORT).show();
	//
	// }
	//
	// });
	//
	// AppController.getInstance().addToRequestQueue(req);
	//
	// }

	public void init() {
		requestListView = (ListView) findViewById(R.id.requestListView);
		requestList = new ArrayList<RequestModel>();
		adapter = new RequestListAdapter(requestList, this);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
		skip = (Button) findViewById(R.id.skip);
		notifTv = (TextView) findViewById(R.id.notifTV);
		requestListView.setAdapter(adapter);
	}

	@Override
	public void onButtonClickListner(int position, String value,
			Boolean isAccept) {
		// TODO Auto-generated method stub
		if (isAccept) {
			Toast.makeText(CircleNotificationActivity.this,
					"Accept Button Clicked", Toast.LENGTH_SHORT).show();
			acceptRequest(value);
		} else {
			Toast.makeText(CircleNotificationActivity.this,
					"Reject Button Clicked", Toast.LENGTH_SHORT).show();
			rejectRequest(value);
		}

	}

	private void rejectRequest(String requestID) {
		// TODO Auto-generated method stub
		JSONObject param = new JSONObject();
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
				"http://54.69.183.186:1340/user/connection-request/"
						+ requestID + "/reject", param,

				new Response.Listener<JSONObject>() {
					@SuppressLint("NewApi")
					@Override
					public void onResponse(JSONObject response) {

						Log.d("Response", "" + response);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						String json = null;

						NetworkResponse response = error.networkResponse;

						if (response != null && response.data != null) {
							int code = response.statusCode;
							// json = new String(response.data);
							// json = trimMessage(json, "message");
							// if (json != null)
							// displayMessage(json, code);

						}
						// hidepDialog();
					}

				});

		AppController.getInstance().addToRequestQueue(req);

	}

	private void acceptRequest(String requestID) {
		// TODO Auto-generated method stub
		JSONObject param = new JSONObject();
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
				"http://54.69.183.186:1340/user/connection-request/"
						+ requestID + "/accept", param,

				new Response.Listener<JSONObject>() {
					@SuppressLint("NewApi")
					@Override
					public void onResponse(JSONObject response) {

						Log.d("Response", "" + response);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						String json = null;

						NetworkResponse response = error.networkResponse;

						if (response != null && response.data != null) {
							int code = response.statusCode;
							// json = new String(response.data);
							// json = trimMessage(json, "message");
							// if (json != null)
							// displayMessage(json, code);

						}
						// hidepDialog();
					}

				});

		AppController.getInstance().addToRequestQueue(req);
	}
}
