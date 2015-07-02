package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchKin.touchkinapp.Interface.ButtonClickListener;
import com.touchKin.touchkinapp.adapter.MyDashbaordAdapter;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touckinapp.R;

public class Fragment2 extends Fragment implements ButtonClickListener {
	List<ParentListModel> list;
	ViewPager myPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View v = inflater.inflate(R.layout.mydashboard, null);
		myPager = (ViewPager) v.findViewById(R.id.myPager);
		list = new ArrayList<ParentListModel>();
		((DashBoardActivity) getActivity()).setCustomButtonListner(this);
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

		if (list != null && list.size() > 0) {
			MyDashbaordAdapter adapter = new MyDashbaordAdapter(getActivity(),
					list);
			myPager.setAdapter(adapter);
		}
	}

}
