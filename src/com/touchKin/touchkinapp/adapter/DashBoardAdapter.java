package com.touchKin.touchkinapp.adapter;

import com.touchKin.touchkinapp.DashBoardActivityFragment;
import com.touchKin.touchkinapp.DashboardLocationFragment;
import com.touchKin.touchkinapp.Fragment1;
import com.touchKin.touchkinapp.TouchFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class DashBoardAdapter extends FragmentPagerAdapter {
	private static int NUM_ITEMS = 3;

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

}
