package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.touchKin.touchkinapp.adapter.DashBoardAdapter;
import com.touchKin.touchkinapp.adapter.ImageAdapter;
import com.touchKin.touchkinapp.custom.HorizontalListView;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touckinapp.R;
import com.touchKin.touckinapp.R.menu;
import com.viewpagerindicator.CirclePageIndicator;

public class Fragment1 extends Fragment implements AnimationListener,
		OnClickListener, OnItemClickListener {

	private ViewPager viewPager;
	private DashBoardAdapter adapter;
	private CirclePageIndicator indicator;
	private ImageAdapter imageAdapter;
	RelativeLayout parentRelativeLayout;
	HorizontalListView listview;
	Animation animSlideUp, animSlideDown;
	ImageView sendTouch, getService;
	PageListener pageListener;
	List<ParentListModel> list;
	private Menu menu;

	public Fragment1() {
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View v = inflater.inflate(R.layout.dashboard_fragment, null);
		init(v);
		viewPager.setAdapter(adapter);
		pageListener = new PageListener();
		list = new ArrayList<ParentListModel>();
		list.add(new ParentListModel("", true, "Mom", "1"));
		list.add(new ParentListModel("", false, "Dad", "2"));
		list.add(new ParentListModel("", false, "Uncle", "3"));
		list.add(new ParentListModel("", false, "Aunt", "4"));
		list.add(new ParentListModel("", false, "GM", "5"));
		imageAdapter = new ImageAdapter(getActivity(), list);
		indicator.setViewPager(viewPager);
		((CirclePageIndicator) indicator).setSnap(true);
		indicator
				.setFillColor(getResources().getColor(R.color.indicator_color));
		indicator.setStrokeColor(getResources().getColor(
				R.color.indicator_color));
		indicator.setOnPageChangeListener(pageListener);
		listview.setOnItemClickListener(this);
		listview.setAdapter(imageAdapter);
		animSlideUp.setAnimationListener(this);
		animSlideDown.setAnimationListener(this);
		sendTouch.setOnClickListener(this);
		return v;
	}

	public void init(View v) {
		adapter = new DashBoardAdapter(getChildFragmentManager());
		viewPager = (ViewPager) v.findViewById(R.id.pager);

		indicator = (CirclePageIndicator) v.findViewById(R.id.indicator);
		listview = (HorizontalListView) v.findViewById(R.id.parentListView);

		parentRelativeLayout = (RelativeLayout) v
				.findViewById(R.id.parentListLayout);
		animSlideUp = AnimationUtils.loadAnimation(getActivity(),
				R.anim.slide_up);
		animSlideDown = AnimationUtils.loadAnimation(getActivity(),
				R.anim.slide_down);
		sendTouch = (ImageView) v.findViewById(R.id.sendTouch);
		getService = (ImageView) v.findViewById(R.id.getService);
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
		this.menu = menu;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.parentName:
			// Not implemented here
			toggleVissibility();
			return true;

		default:
			break;
		}

		return false;
	}

	private void toggleVissibility() {
		// TODO Auto-generated method stub
		if (parentRelativeLayout.getVisibility() == View.VISIBLE) {

			parentRelativeLayout.setVisibility(View.VISIBLE);
			parentRelativeLayout.startAnimation(animSlideUp);

		} else {
			parentRelativeLayout.setVisibility(View.VISIBLE);
			parentRelativeLayout.startAnimation(animSlideDown);
		}
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		if (animation == animSlideDown) {
			parentRelativeLayout.setVisibility(View.VISIBLE);
		} else if (animation == animSlideUp) {
			parentRelativeLayout.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sendTouch:
			sendTouch();
			break;
		case R.id.getService:
			break;
		default:
			break;
		}
	}

	private void sendTouch() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), SendTouchActivity.class);
		startActivity(intent);
	}

	private class PageListener extends SimpleOnPageChangeListener {
		public void onPageSelected(int position) {
			Toolbar toolbar = (Toolbar) getActivity().findViewById(
					R.id.tool_bar);
			TextView mTitle = (TextView) toolbar
					.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getStringArray(R.array.frag_titles)[position]);

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		ParentListModel item = list.get(position);
		Log.d("Model", "" + item.getIsSelected());
		for (ParentListModel data : list) {
			if (data.equals(item)) {
				data.setIsSelected(true);
			} else {
				data.setIsSelected(false);
			}
		}
		MenuItem parentMenu = menu.findItem(R.id.parentName);
		parentMenu.setTitle(item.getParentName());
		listview.setAdapter(imageAdapter);
		toggleVissibility();

	}
}
