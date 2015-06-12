package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ExpandableListView;

import com.touchKin.touchkinapp.adapter.ExpandableListAdapter;
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
		adapter = new ExpandableListAdapter(MyFamily.this);
		adapter.setupTrips(careGiver, requests, CareReciever, null);
		expandListView.setAdapter(adapter);
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
