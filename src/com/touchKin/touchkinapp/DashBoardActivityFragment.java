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
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.touchKin.touchkinapp.Interface.FragmentInterface;
import com.touchKin.touchkinapp.custom.HoloCircularProgressBar;
import com.touchKin.touchkinapp.custom.PieSlice;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touckinapp.R;

public class DashBoardActivityFragment extends Fragment implements
		FragmentInterface {
	private HoloCircularProgressBar mHoloCircularProgressBar;
	private ObjectAnimator mProgressBarAnimator;
	TextView battery;
	ImageView battery5, wifi4, network4;
	TelephonyManager Tel;
	MyPhoneStateListener MyListener;
	TextView parentName;
	ParentListModel parent;

	Context context;

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

		parentName = (TextView) view.findViewById(R.id.parentNameTV);

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
		battery5 = (ImageView) view.findViewById(R.id.battery5);
		wifi4 = (ImageView) view.findViewById(R.id.wifi4);
		network4 = (ImageView) view.findViewById(R.id.ImageView05);

		// wifiSignal = (TextView) view.findViewById(R.id.wifi);

		WifiManager wifi = (WifiManager) getActivity().getSystemService(
				Context.WIFI_SERVICE);
		onReceive(wifi);
		// signalStr = (TextView) view.findViewById(R.id.signal);
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
	public void onStart() {
		super.onStart();
		getActivity().registerReceiver(this.mBatInfoReceiver,
				new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

	};

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		Tel.listen(MyListener, PhoneStateListener.LISTEN_NONE);
		if (this.mBatInfoReceiver != null)
			getActivity().unregisterReceiver(this.mBatInfoReceiver);
	}

	@Override
	public void fragmentBecameVisible() {
		// TODO Auto-generated method stub
		mHoloCircularProgressBar.setProgress(0.0f);
		animate(mHoloCircularProgressBar, null, (float) (1.0f / 30), 1000);
		parent = ((DashBoardActivity) getActivity()).getSelectedParent();
		Log.d("Parent", parent + "");
		if (parent != null) {
			parentName.setText(parent.getParentName() + " activity ");
		}
	}

	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context ctxt, Intent intent) {
			int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			battery.setText(String.valueOf(level) + "%");
			if (level == 100) {
				battery5.setImageResource(R.drawable.battery100);
			} else if (level >= 80 || level < 100) {
				battery5.setImageResource(R.drawable.battery80);

			} else if (level >= 60 || level < 80) {
				battery5.setImageResource(R.drawable.battery60);

			} else if (level >= 40 || level < 60) {
				battery5.setImageResource(R.drawable.battery40);

			} else if (level >= 20 || level < 40) {
				battery5.setImageResource(R.drawable.battery20);

			} else if (level == 0) {
				battery5.setImageResource(R.drawable.battery0);

			}
		}
	};

	public void onReceive(WifiManager wifiManager) {
		int numberOfLevels = 5;
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(),
				numberOfLevels);
		float val = ((float) level / numberOfLevels) * 100f;
		Log.d("wifi", level + " " + val);
		if (level == 5) {
			wifi4.setImageResource(R.drawable.wifi4);
		} else if (level == 4) {
			wifi4.setImageResource(R.drawable.wifi3);

		} else if (level == 3) {
			wifi4.setImageResource(R.drawable.wifi3);

		} else if (level == 2) {
			wifi4.setImageResource(R.drawable.wifi2);

		} else if (level == 1) {
			wifi4.setImageResource(R.drawable.wifi1);

		} else if (level == 0) {
			wifi4.setImageResource(R.drawable.wifi0);

		}
		// wifiSignal.setText("Wifi " + ' ' + String.valueOf((int) val) + "%");
	}

	/* —————————– */
	/* Start the PhoneState listener */
	/* —————————– */
	// @SuppressWarnings("deprecation")
	// private static boolean isAirplaneModeOn(Context context) {
	// boolean isEnabled = Settings.System.getInt(
	// context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON,
	// 0) == 1;
	// return isEnabled;
	//
	// }

	private class MyPhoneStateListener extends PhoneStateListener {
		/*
		 * Get the Signal strength from the provider, each tiome there is an
		 * update
		 */
		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);
			int level = signalStrength.getGsmSignalStrength();
			Log.d("signal", "" + level);
			// if (!isAirplaneModeOn(context)) {
			if (level >= 24) {
				network4.setImageResource(R.drawable.network4);

			} else if (level >= 16 || level < 24) {
				network4.setImageResource(R.drawable.network3);

			} else if (level >= 8 || level < 16) {
				network4.setImageResource(R.drawable.network2);

			} else if (level >= 0 || level < 8) {
				network4.setImageResource(R.drawable.network1);

			} else if (level == 0) {
				network4.setImageResource(R.drawable.network0);

			}
		}
		// else {
		// network4.setImageResource(R.drawable.network0);
		// }
		// signalStr.setText("Signal " + ' '
		// + String.valueOf(signalStrength.getGsmSignalStrength()));
	}

	/* End of private Class */

}
