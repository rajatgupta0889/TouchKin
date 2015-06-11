package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import com.touchKin.touchkinapp.adapter.ExpandableListAdapter;
import com.touchKin.touchkinapp.model.ExpandableListGroupItem;
import com.touchKin.touchkinapp.model.TravelItem;
import com.touchKin.touckinapp.R;

public class MyFamily extends AppCompatActivity {
	ExpandableListAdapter adapter;
	ArrayList<TravelItem> list;
	ArrayList<TravelItem> list1;
	ExpandableListView expandListView;
	List<String> item;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_family);
		init();
		list.add(new TravelItem());
		list.add(new TravelItem());
		list.add(new TravelItem());

		list1.add(new TravelItem());
		list1.add(new TravelItem());
		list1.add(new TravelItem());
		list1.add(new TravelItem());
		adapter = new ExpandableListAdapter(MyFamily.this);
		adapter.setupTrips(list, list1);
		expandListView.setAdapter(adapter);
	}

	private void init() {
		// TODO Auto-generated method stub
		expandListView = (ExpandableListView) findViewById(R.id.familyList);
		list = new ArrayList<TravelItem>();
		list1 = new ArrayList<TravelItem>();
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
