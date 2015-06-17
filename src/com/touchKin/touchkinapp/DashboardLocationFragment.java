package com.touchKin.touchkinapp;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.touchKin.touchkinapp.Interface.FragmentInterface;
import com.touchKin.touchkinapp.custom.HoloCircularProgressBar;
import com.touchKin.touchkinapp.custom.PieSlice;
import com.touchKin.touchkinapp.custom.ServiceHelper;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touckinapp.R;

public class DashboardLocationFragment extends Fragment implements
		FragmentInterface, ConnectionCallbacks, OnConnectionFailedListener {
	private HoloCircularProgressBar mHoloCircularProgressBar;
	private ObjectAnimator mProgressBarAnimator;
	TextView parentName, parentNameBottom;
	ParentListModel parent;

	Marker googleMarker = null;
	/**
	 * Provides the entry point to Google Play services.
	 */
	protected GoogleApiClient mGoogleApiClient;
	protected Location mLastLocation;

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
		//ServiceHelper.startBackgroundServiceIfNotAlreadyRunning(getActivity().getApplicationContext());
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		googleMap.clear();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}

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
			parent = ((DashBoardActivity) getActivity()).getSelectedParent();
			parentName = (TextView) view.findViewById(R.id.ParentLocTV);
			parentNameBottom = (TextView) view.findViewById(R.id.textView5);
			if (parent != null)
				parentName.setText(parent.getParentName() + " is in ");
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
		buildGoogleApiClient();
		return view;
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

	Builder dialog;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		LocationManager lm = null;
		boolean gps_enabled = false, network_enabled = false;
		if (lm == null)
			lm = (LocationManager) getActivity().getSystemService(
					Context.LOCATION_SERVICE);
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		if (!gps_enabled && !network_enabled) {
			dialog = new AlertDialog.Builder(getActivity());
			dialog.setMessage("GPS is not enabled");
			dialog.setPositiveButton(
					getActivity().getResources().getString(
							R.string.open_location_settings),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(
								DialogInterface paramDialogInterface,
								int paramInt) {
							// TODO Auto-generated method stub
							Intent myIntent = new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							getActivity().startActivity(myIntent);
							// get gps
						}
					});

			dialog.show();

		}
		if (isGooglePlayServicesAvailable()) {
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			googleMap.getUiSettings().setZoomControlsEnabled(false);
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

			googleMap.getUiSettings().setScrollGesturesEnabled(false);
			googleMap.getUiSettings().setZoomGesturesEnabled(false);
			googleMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public void onMapClick(LatLng arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "Hi", Toast.LENGTH_LONG)
							.show();
					Intent intent = new Intent(getActivity(), MapActivity.class);
					startActivity(intent);
				}
			});
		}
		mHoloCircularProgressBar.setProgress(0.0f);
		// animate(mHoloCircularProgressBar, null, 0.05f, 3000);
		// Toast.makeText(getActivity(), "Resume", Toast.LENGTH_SHORT).show();
		mHoloCircularProgressBar.setProgress(0.0f);
		animate(mHoloCircularProgressBar, null, (float) (1.0f / 30), 1000);
		super.onResume();
	}

	@Override
	public void fragmentBecameVisible() {
		// TODO Auto-generated method stub
		mHoloCircularProgressBar.setProgress(0.0f);
		animate(mHoloCircularProgressBar, null, (float) (1.0f / 30), 1000);
		parent = ((DashBoardActivity) getActivity()).getSelectedParent();
		Log.d("Parent", parent + "");
		if (parent != null) {
			parentName.setText(parent.getParentName() + " is in ");
			parentNameBottom.setText("Tap on map to set "
					+ parent.getParentName() + "'s");
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// Provides a simple way of getting a device's location and is well
		// suited for
		// applications that do not require a fine-grained location and that do
		// not need location
		// updates. Gets the best and most recent location currently available,
		// which may be null
		// in rare cases when a location is not available.
		mLastLocation = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);

		if (mLastLocation != null) {
			LatLng latLng = new LatLng(mLastLocation.getLatitude(),
					mLastLocation.getLongitude());

			View marker = ((LayoutInflater) getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE)).inflate(
					R.layout.custom_marker, null);

			if (googleMarker != null)
				googleMarker.remove();
			// googleMarker = googleMap.addMarker(new MarkerOptions()
			// .position(latLng)
			// .title("randomlocation")
			// .icon(BitmapDescriptorFactory.fromBitmap(CustomMarkerView(
			// getActivity(), marker))));
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
		} else {
			Toast.makeText(getActivity(), "No Location Detected",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Refer to the javadoc for ConnectionResult to see what error codes
		// might be returned in
		// onConnectionFailed.
		Log.i("Location",
				"Connection failed: ConnectionResult.getErrorCode() = "
						+ result.getErrorCode());
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// The connection to Google Play services was lost for some reason. We
		// call connect() to
		// attempt to re-establish the connection.
		Log.i("Location", "Connection suspended");
		mGoogleApiClient.connect();
	}

	@Override
	public void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	/**
	 * Builds a GoogleApiClient. Uses the addApi() method to request the
	 * LocationServices API.
	 */
	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

}
