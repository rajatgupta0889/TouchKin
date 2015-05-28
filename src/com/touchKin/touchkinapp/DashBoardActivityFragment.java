package com.touchKin.touchkinapp;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.touchKin.touchkinapp.Interface.FragmentInterface;
import com.touchKin.touchkinapp.custom.HoloCircularProgressBar;
import com.touchKin.touchkinapp.custom.PieSlice;
import com.touchKin.touckinapp.R;

public class DashBoardActivityFragment extends Fragment implements
		FragmentInterface {
	private HoloCircularProgressBar mHoloCircularProgressBar;
	private ObjectAnimator mProgressBarAnimator;
	TextView battery, wifiSignal, signalStr;
	TelephonyManager Tel;
	MyPhoneStateListener MyListener;

	// newInstance constructor for creating fragment with arguments
	public static DashBoardActivityFragment newInstance(int page, String title) {
		DashBoardActivityFragment dashBoardActivityFragment = new DashBoardActivityFragment();
		Bundle args = new Bundle();
		args.putInt("someInt", page);
		args.putString("someTitle", title);
		dashBoardActivityFragment.setArguments(args);
		return dashBoardActivityFragment;
	}

	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dashboard_activity_screen,
				container, false);
		final Resources resources = getResources();

		// final PieGraph pg = (PieGraph) view.findViewById(R.id.piegraph);
		mHoloCircularProgressBar = (HoloCircularProgressBar) view
				.findViewById(R.id.holoCircularProgressBar);
		ArrayList<PieSlice> slices = new ArrayList<PieSlice>();
		PieSlice slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_left));
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_left));
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slices.add(slice);
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_left));
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slices.add(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slices.add(slice);
		mHoloCircularProgressBar.setSlices(slices);
		battery = (TextView) view.findViewById(R.id.battery);
		wifiSignal = (TextView) view.findViewById(R.id.wifi);
		getActivity().registerReceiver(this.mBatInfoReceiver,
				new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		WifiManager wifi = (WifiManager) getActivity().getSystemService(
				Context.WIFI_SERVICE);
		onReceive(wifi);
		signalStr = (TextView) view.findViewById(R.id.signal);
		/* Update the listener, and start it */
		MyListener = new MyPhoneStateListener();
		Tel = (TelephonyManager) getActivity().getSystemService(
				Context.TELEPHONY_SERVICE);
		Tel.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		return view;
	}

	private void animate(final HoloCircularProgressBar progressBar,
			final AnimatorListener listener, final float progress,
			final int duration) {

		mProgressBarAnimator = ObjectAnimator.ofFloat(progressBar, "progress",
				progress);
		mProgressBarAnimator.setDuration(duration);

		mProgressBarAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationCancel(final Animator animation) {
			}

			@Override
			public void onAnimationEnd(final Animator animation) {
				progressBar.setProgress(progress);
			}

			@Override
			public void onAnimationRepeat(final Animator animation) {
			}

			@Override
			public void onAnimationStart(final Animator animation) {
			}
		});
		if (listener != null) {
			mProgressBarAnimator.addListener(listener);
		}
		mProgressBarAnimator.reverse();
		mProgressBarAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(final ValueAnimator animation) {
				progressBar.setProgress((Float) animation.getAnimatedValue());
			}
		});
		progressBar.setMarkerProgress(progress);
		mProgressBarAnimator.start();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mHoloCircularProgressBar.setProgress(0.0f);
		// animate(mHoloCircularProgressBar, null, 0.05f, 3000);
		// Toast.makeText(getActivity(), "Resume", Toast.LENGTH_SHORT).show();
		mHoloCircularProgressBar.setProgress(0.0f);
		animate(mHoloCircularProgressBar, null, (float) (1.0f / 30), 1000);
		Tel.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Tel.listen(MyListener, PhoneStateListener.LISTEN_NONE);
	}

	@Override
	public void fragmentBecameVisible() {
		// TODO Auto-generated method stub
		mHoloCircularProgressBar.setProgress(0.0f);
		animate(mHoloCircularProgressBar, null, (float) (1.0f / 30), 1000);

	}

	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context ctxt, Intent intent) {
			int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			battery.setText("Battery " + ' ' + String.valueOf(level) + "%");
		}
	};

	public void onReceive(WifiManager wifiManager) {
		int numberOfLevels = 5;
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(),
				numberOfLevels);
		float val = ((float) level / numberOfLevels) * 100f;
		wifiSignal.setText("Wifi " + ' ' + String.valueOf((int) val) + "%");
	}

	/* —————————– */
	/* Start the PhoneState listener */
	/* —————————– */
	private class MyPhoneStateListener extends PhoneStateListener {
		/*
		 * Get the Signal strength from the provider, each tiome there is an
		 * update
		 */
		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);
			signalStr.setText("Signal " + ' '
					+ String.valueOf(signalStrength.getGsmSignalStrength()));
		}

	};/* End of private Class */

}
