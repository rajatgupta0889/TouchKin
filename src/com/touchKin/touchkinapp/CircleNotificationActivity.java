package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.request_list);
		init();
		getConnectionRequest();
		adapter.setCustomButtonListner(this);
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
									JSONObject obj = responseArray
											.getJSONObject(i);

									// RequestModel item = new RequestModel();
									//
									// requestList.add(item);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else {
							requestList.add(new RequestModel("", "", "", ""));
							requestList.add(new RequestModel("", "", "", ""));
							requestList.add(new RequestModel("", "", "", ""));
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
