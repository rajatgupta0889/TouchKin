package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;

import android.util.Log;
import android.view.LayoutInflater;

import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;
import com.touchKin.touchkinapp.adapter.ExpandableListAdapter;
import com.touchKin.touchkinapp.adapter.ImageAdapter;
import com.touchKin.touchkinapp.custom.CustomRequest;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touchkinapp.model.ExpandableListGroupItem;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touchkinapp.model.RequestModel;
import com.touchKin.touckinapp.R;

public class MyFamily extends ActionBarActivity {
	ExpandableListAdapter adapter;
	HashMap<String, ArrayList<ParentListModel>> careGiver;
	ArrayList<RequestModel> requests;
	ArrayList<ParentListModel> parents;
	ArrayList<ExpandableListGroupItem> CareReciever;
	ArrayList<ExpandableListGroupItem> pendingReq;
	ExpandableListView expandListView;
	List<String> item;
	LinearLayout footerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_family);
		init();
		parents.add(new ParentListModel("", false, "", "", ""));
		parents.add(new ParentListModel("", false, "", "", ""));
		parents.add(new ParentListModel("", false, "", "", ""));
		CareReciever.add(new ExpandableListGroupItem("0", "", "", ""));
		CareReciever.add(new ExpandableListGroupItem("1", "", "", ""));
		CareReciever.add(new ExpandableListGroupItem("2", "", "", ""));
		careGiver.put(CareReciever.get(0).getUserId(), parents);
		careGiver.put(CareReciever.get(1).getUserId(), parents);
		careGiver.put(CareReciever.get(2).getUserId(), parents);
		requests.add(new RequestModel("", "", "", ""));
		requests.add(new RequestModel("", "", "", ""));
		pendingReq.add(new ExpandableListGroupItem());
		pendingReq.add(new ExpandableListGroupItem());
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerView = (LinearLayout) inflater.inflate(
				R.layout.expand_list_footer, null);
		expandListView.addFooterView(footerView);
		adapter = new ExpandableListAdapter(MyFamily.this);
		fetchMyFamily();
		adapter.setupTrips(careGiver, requests, CareReciever, pendingReq);
		expandListView.setAdapter(adapter);

	}

	private void fetchMyFamily() {
		// TODO Auto-generated method stub

		CustomRequest req = new CustomRequest(
				"http://54.69.183.186:1340/user/family",
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject responseArray) {
						// TODO Auto-generated method stub
						Log.d("Response Array", " " + responseArray);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());
						Toast.makeText(MyFamily.this, error.getMessage(),
								Toast.LENGTH_SHORT).show();

					}

				});

		AppController.getInstance().addToRequestQueue(req);

	}

	private void init() {
		// TODO Auto-generated method stub
		expandListView = (ExpandableListView) findViewById(R.id.familyList);
		careGiver = new HashMap<String, ArrayList<ParentListModel>>();
		CareReciever = new ArrayList<ExpandableListGroupItem>();
		pendingReq = new ArrayList<ExpandableListGroupItem>();
		requests = new ArrayList<RequestModel>();
		parents = new ArrayList<ParentListModel>();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void onStart() {
		super.onStart();

	}

}
