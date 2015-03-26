package com.touchKin.touchkinapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.touchKin.touchkinapp.adapter.DashBoardAdapter;
import com.touchKin.touckinapp.R;
import com.viewpagerindicator.CirclePageIndicator;

public class Fragment1 extends Fragment {

	private TextView text;
	private ViewPager viewPager;
	private DashBoardAdapter adapter;
	private CirclePageIndicator indicator;

	public Fragment1() {
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.dashboard_fragment, null);
		adapter = new DashBoardAdapter(getChildFragmentManager());
		viewPager = (ViewPager) v.findViewById(R.id.pager);
		viewPager.setAdapter(adapter);
		indicator = (CirclePageIndicator) v.findViewById(R.id.indicator);
		indicator.setViewPager(viewPager);
		((CirclePageIndicator) indicator).setSnap(true);
		// text = (TextView) v.findViewById(R.id.text);
		// if (getArguments() != null) {
		// //
		// try {
		// String value = getArguments().getString("key");
		// text.setText("Current Tab is: " + value);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	//

}
