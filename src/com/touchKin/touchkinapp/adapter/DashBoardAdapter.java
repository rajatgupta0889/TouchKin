package com.touchKin.touchkinapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.touchKin.touchkinapp.DashBoardActivityFragment;
import com.touchKin.touchkinapp.DashboardLocationFragment;
import com.touchKin.touchkinapp.DashboardWithoutKinFragment;
import com.touchKin.touchkinapp.TouchFragment;

public class DashBoardAdapter extends FragmentPagerAdapter {
	private static int NUM_ITEMS = 3;
	public static Context context;

	public DashBoardAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub

		switch (position) {
		case 0: // Fragment # 0 - This will show FirstFragment
			return TouchFragment.newInstance(0, "Touch");
		case 1: // Fragment # 0 - This will show FirstFragment different title
			return DashboardLocationFragment.newInstance(1, "Location");
		case 2: // Fragment # 1 - This will show SecondFragment
			return DashBoardActivityFragment.newInstance(2, "Activity");
		default:
			return null;
		}
	}

	// Returns the page title for the top indicator
	@Override
	public CharSequence getPageTitle(int position) {
		return "Page " + position;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return NUM_ITEMS;
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView((View) view);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}
