package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
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
	TextView mTitle, skip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.request_list);
		init();
		getConnectionRequest();
		adapter.setCustomButtonListner(this);
		mTitle.setText("Notification");
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
			}
		});

	}

	private void getConnectionRequest() {
		// TODO Auto-generated method stub
		JsonArrayRequest req = new JsonArrayRequest(
				"http://54.69.183.186:1340/user/connection-requests",
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray responseArray) {
						// TODO Auto-generated method stub
						Log.d("Response Array", " " + responseArray);
						if (responseArray.length() > 0) {
							for (int i = 0; i < responseArray.length(); i++) {
								try {
									RequestModel item = new RequestModel();
									JSONObject careRequest = responseArray
											.getJSONObject(i);
									JSONObject careInitiator = careRequest
											.getJSONObject("initiator");
									JSONObject careGiver = careRequest
											.getJSONObject("care_giver");
									JSONObject careReciever = careRequest
											.getJSONObject("care_reciever");
									if (careInitiator.getString("id").equals(
											careGiver.get("id"))) {
										if (careInitiator.has("nickname")) {
											item.setCare_reciever_name(careInitiator
													.getString("nickname"));
										} else if (careInitiator
												.has("first_name")) {
											item.setCare_reciever_name(careInitiator
													.getString("first_name"));
										} else {
											item.setCare_reciever_name(careInitiator
													.getString("mobile"));
										}
										item.setReqMsg(item
												.getCare_reciever_name()
												+ " wants to care for you");
									}
									if (careInitiator.get("id").equals(
											careReciever.get("id"))) {
										if (careInitiator.has("nickname")) {
											item.setCare_reciever_name(careInitiator
													.getString("nickname"));
										} else if (careInitiator
												.has("first_name")) {
											item.setCare_reciever_name(careInitiator
													.getString("first_name"));
										} else {
											item.setCare_reciever_name(careInitiator
													.getString("mobile"));
										}
										item.setReqMsg(item
												.getCare_reciever_name()
												+ " wants you to care for them");

									}
									if (!careInitiator.get("id").equals(
											careReciever.get("id"))
											&& !careInitiator
													.getString("id")
													.equals(careGiver.get("id"))) {
										if (careInitiator.has("nickname")) {
											item.setCare_reciever_name(careInitiator
													.getString("nickname"));
										} else if (careInitiator
												.has("first_name")) {
											item.setCare_reciever_name(careInitiator
													.getString("first_name"));
										} else {
											item.setCare_reciever_name(careInitiator
													.getString("mobile"));
										}
										item.setReqMsg(item
												.getCare_reciever_name()
												+ " wants you to care for"
												+ careReciever
														.getInt("nickname"));
									}
									// RequestModel item = new RequestModel();
									//
									item.setRequestID(careRequest
											.getString("id"));
									requestList.add(item);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else {
							Toast.makeText(CircleNotificationActivity.this,
									"NO Request", Toast.LENGTH_SHORT).show();

						}
						adapter.notifyDataSetChanged();
						requestListView.setAdapter(adapter);

					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());
						Toast.makeText(CircleNotificationActivity.this,
								error.getMessage(), Toast.LENGTH_SHORT).show();

					}

				});

		AppController.getInstance().addToRequestQueue(req);

	}

	public void init() {
		requestListView = (ListView) findViewById(R.id.requestListView);
		requestList = new ArrayList<RequestModel>();
		adapter = new RequestListAdapter(requestList, this);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
		skip = (TextView) findViewById(R.id.skip);
	}

	@Override
	public void onButtonClickListner(int position, String value,
			Boolean isAccept) {
		// TODO Auto-generated method stub
		if (isAccept) {
			Toast.makeText(CircleNotificationActivity.this,
					"Accept Button Clicked", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(CircleNotificationActivity.this,
					"Reject Button Clicked", Toast.LENGTH_SHORT).show();
		}

	}
}
