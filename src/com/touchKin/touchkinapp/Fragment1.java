package com.touchKin.touchkinapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.devsmart.android.ui.HorizontalListView;
import com.touchKin.touchkinapp.adapter.DashBoardAdapter;
import com.touchKin.touchkinapp.adapter.ImageAdapter;
import com.touchKin.touckinapp.R;
import com.viewpagerindicator.CirclePageIndicator;

public class Fragment1 extends Fragment implements AnimationListener {

	private ViewPager viewPager;
	private DashBoardAdapter adapter;
	private CirclePageIndicator indicator;
	private ImageAdapter imageAdapter;
	RelativeLayout parentRelativeLayout;
	HorizontalListView listview;
	Animation animSlideUp, animSlideDown;

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

		indicator.setViewPager(viewPager);
		((CirclePageIndicator) indicator).setSnap(true);
		indicator
				.setFillColor(getResources().getColor(R.color.indicator_color));
		indicator.setStrokeColor(getResources().getColor(
				R.color.indicator_color));
		listview.setAdapter(imageAdapter);
		animSlideUp.setAnimationListener(this);
		animSlideDown.setAnimationListener(this);
		return v;
	}

	public void init(View v) {
		adapter = new DashBoardAdapter(getChildFragmentManager());
		viewPager = (ViewPager) v.findViewById(R.id.pager);
		indicator = (CirclePageIndicator) v.findViewById(R.id.indicator);
		listview = (HorizontalListView) v.findViewById(R.id.parentListView);
		imageAdapter = new ImageAdapter(getActivity());
		parentRelativeLayout = (RelativeLayout) v
				.findViewById(R.id.parentListLayout);
		animSlideUp = AnimationUtils.loadAnimation(getActivity(),
				R.anim.slide_up);
		animSlideDown = AnimationUtils.loadAnimation(getActivity(),
				R.anim.slide_down);
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

}
