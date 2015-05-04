package com.touchKin.touchkinapp;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.touchKin.touchkinapp.Interface.FragmentInterface;
import com.touchKin.touchkinapp.custom.HoloCircularProgressBar;
import com.touchKin.touchkinapp.custom.PieSlice;
import com.touchKin.touckinapp.R;

public class DashboardLocationFragment extends Fragment implements
		LocationListener, FragmentInterface, OnMapReadyCallback {
	private HoloCircularProgressBar mHoloCircularProgressBar;
	private ObjectAnimator mProgressBarAnimator;
	Marker googleMarker = null;

	public static DashboardLocationFragment newInstance(int page, String title) {
		DashboardLocationFragment locationFragment = new DashboardLocationFragment();
		Bundle args = new Bundle();
		args.putInt("someInt", page);
		args.putString("someTitle", title);
		locationFragment.setArguments(args);

		return locationFragment;
	}

	private GoogleMap googleMap;
	private static View view;

	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		googleMap.clear();

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		try {
			view = inflater.inflate(R.layout.dashboard_location_screen,
					container, false);
		} catch (InflateException e) {
			/* map is already there, just return view as it is */
		}

		final Resources resources = getResources();
		if (googleMap == null) {
			googleMap = ((MapFragment) getActivity().getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
		}
		// if (isGooglePlayServicesAvailable()) {
		// googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// googleMap.getUiSettings().setZoomControlsEnabled(false);
		// LocationManager locationManager = (LocationManager) getActivity()
		// .getSystemService(Context.LOCATION_SERVICE);
		// boolean enabledGPS = locationManager
		// .isProviderEnabled(LocationManager.GPS_PROVIDER);
		// // Check if enabled and if not send user to the GSP settings
		// // Better solution would be to display a dialog and suggesting to
		// // go to the settings
		// if (!enabledGPS) {
		// Toast.makeText(getActivity(), "GPS signal not found",
		// Toast.LENGTH_LONG).show();
		// Intent intent = new Intent(
		// Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		// startActivity(intent);
		// }
		// Criteria criteria = new Criteria();
		// String bestProvider = locationManager.getBestProvider(criteria,
		// false);
		// Location location = locationManager
		// .getLastKnownLocation(bestProvider);
		// if (location != null) {
		// onLocationChanged(location);
		// }
		//
		// googleMap.getUiSettings().setScrollGesturesEnabled(false);
		// googleMap.getUiSettings().setZoomGesturesEnabled(false);
		//
		// }
		mHoloCircularProgressBar = (HoloCircularProgressBar) view
				.findViewById(R.id.holoCircularProgressBar);
		ArrayList<PieSlice> slices = new ArrayList<PieSlice>();
		// final PieGraph pg = (PieGraph) view.findViewById(R.id.piegraph);
		PieSlice slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slices.add(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_left));
		slices.add(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_left));
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
		slice.setColor(resources.getColor(R.color.daily_prog_left));
		slices.add(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_left));
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
		slice.setColor(resources.getColor(R.color.daily_prog_left));
		slices.add(slice);
		mHoloCircularProgressBar.setSlices(slices);

		return view;
	}

	@Override
	public void onLocationChanged(Location location) {

		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		LatLng latLng = new LatLng(latitude, longitude);
		View marker = ((LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.custom_marker, null);

		if (googleMarker != null)
			googleMarker.remove();
		googleMarker = googleMap.addMarker(new MarkerOptions()
				.position(latLng)
				.title("randomlocation")
				.icon(BitmapDescriptorFactory.fromBitmap(CustomMarkerView(
						getActivity(), marker))));
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

	private boolean isGooglePlayServicesAvailable() {
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity());
		if (ConnectionResult.SUCCESS == status) {
			return true;
		} else {
			GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0)
					.show();
			return false;
		}
	}

	public static Bitmap CustomMarkerView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels,
				displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
				view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
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
		if (isGooglePlayServicesAvailable()) {
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			googleMap.getUiSettings().setZoomControlsEnabled(false);
			LocationManager locationManager = (LocationManager) getActivity()
					.getSystemService(Context.LOCATION_SERVICE);
			boolean enabledGPS = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			// Check if enabled and if not send user to the GSP settings
			// Better solution would be to display a dialog and suggesting to
			// go to the settings
			if (!enabledGPS) {
				Toast.makeText(getActivity(), "GPS signal not found",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent(
						Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
			}
			Criteria criteria = new Criteria();
			String bestProvider = locationManager.getBestProvider(criteria,
					false);
			Location location = locationManager
					.getLastKnownLocation(bestProvider);
			if (location != null) {
				onLocationChanged(location);
			}

			googleMap.getUiSettings().setScrollGesturesEnabled(false);
			googleMap.getUiSettings().setZoomGesturesEnabled(false);

		}
		mHoloCircularProgressBar.setProgress(0.0f);
		// animate(mHoloCircularProgressBar, null, 0.05f, 3000);
		// Toast.makeText(getActivity(), "Resume", Toast.LENGTH_SHORT).show();
		mHoloCircularProgressBar.setProgress(0.0f);
		animate(mHoloCircularProgressBar, null, (float) (1.0f / 30), 3000);
		super.onResume();
	}

	@Override
	public void fragmentBecameVisible() {
		// TODO Auto-generated method stub
		mHoloCircularProgressBar.setProgress(0.0f);
		animate(mHoloCircularProgressBar, null, (float) (1.0f / 30), 3000);

	}

	@Override
	public void onMapReady(GoogleMap arg0) {
		// TODO Auto-generated method stub

	}

}
