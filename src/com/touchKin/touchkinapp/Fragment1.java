package com.touchKin.touchkinapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.touchKin.touchkinapp.Interface.FragmentInterface;
import com.touchKin.touchkinapp.adapter.DashBoardAdapter;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touckinapp.R;

public class Fragment1 extends Fragment implements OnClickListener {

	private ViewPager viewPager;
	public DashBoardAdapter adapter;
	// private CirclePageIndicator indicator;
	TextView sendTouch, getService;
	PageListener pageListener;
	ParentListModel parent;
	Vibrator vib;

	public Fragment1() {
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		vib = (Vibrator) this.getActivity().getSystemService(
				getActivity().VIBRATOR_SERVICE);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View v = inflater.inflate(R.layout.dashboard_fragment, null);
		init(v);
		DashBoardAdapter.context = getActivity();
		viewPager.setAdapter(adapter);
		pageListener = new PageListener();
		viewPager.setOnPageChangeListener(pageListener);

		// indicator.setViewPager(viewPager);
		// ((CirclePageIndicator) indicator).setSnap(false);
		// indicator
		// .setFillColor(getResources().getColor(R.color.indicator_color));
		// indicator.setStrokeColor(getResources().getColor(
		// R.color.indicator_color));
		// indicator.setOnPageChangeListener(pageListener);

		sendTouch.setOnClickListener(this);
		getService.setOnClickListener(this);
		return v;
	}

	public void init(View v) {
		adapter = new DashBoardAdapter(getChildFragmentManager(), parent);
		viewPager = (ViewPager) v.findViewById(R.id.pager);

		// indicator = (CirclePageIndicator) v.findViewById(R.id.indicator);

		sendTouch = (TextView) v.findViewById(R.id.sendTouch);
		getService = (TextView) v.findViewById(R.id.getService);
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

		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sendTouch:
			vib.vibrate(500);
			// v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
			sendTouch();
			break;
		case R.id.getService:
			vib.vibrate(500);
			// v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
			parent = ((DashBoardActivity) getActivity()).getSelectedParent();

			if (parent != null) {
				Intent callIntent = new Intent(Intent.ACTION_DIAL);
				callIntent
						.setData(Uri.parse("tel:" + parent.getMobilenumber()));
				Intent chooser = Intent.createChooser(callIntent, "Call using");
				startActivity(chooser);
			} else {
				Toast.makeText(getActivity(),
						"Please Select Your parent to call", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		default:
			break;
		}
	}

	private void sendTouch() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), SendTouchActivity.class);
		ParentListModel model = ((DashBoardActivity) getActivity())
				.getSelectedParent();

		intent.putExtra("userId", model.getParentId());
		startActivity(intent);
	}

	private class PageListener extends SimpleOnPageChangeListener {
		public void onPageSelected(int position) {
			Toolbar toolbar = (Toolbar) getActivity().findViewById(
					R.id.tool_bar);
			TextView mTitle = (TextView) toolbar
					.findViewById(R.id.toolbar_title);
			// SharedPreferences pref = getActivity().getSharedPreferences(
			// "countPref", 0);
			// if (!pref.getBoolean("count", false)) {
			// position = position - 1;
			// }
			// if (position == -1) {
			//
			// } else {
			// mTitle.setText(getResources().getStringArray(R.array.frag_titles)[position]);
			// }
			FragmentInterface fragment = (FragmentInterface) adapter
					.instantiateItem(viewPager, position);
			if (fragment != null) {
				fragment.fragmentBecameVisible();

			}

		}
	}

	public void notifyFrag() {
		adapter.notifyDataSetChanged();
	}

}
