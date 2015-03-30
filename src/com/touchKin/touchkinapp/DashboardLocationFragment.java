package com.touchKin.touchkinapp;

import android.content.Context;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.touchKin.touckinapp.R;

public class DashboardLocationFragment extends Fragment implements
		LocationListener {
	public static DashboardLocationFragment newInstance(int page, String title) {
		DashboardLocationFragment locationFragment = new DashboardLocationFragment();
		Bundle args = new Bundle();
		args.putInt("someInt", page);
		args.putString("someTitle", title);
		locationFragment.setArguments(args);
		return locationFragment;
	}

	private GoogleMap googleMap;
	static final LatLng randomlocation = new LatLng(21, 57);

	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dashboard_location_screen,
				container, false);
		final Resources resources = getResources();
		if (googleMap == null) {
			googleMap = ((MapFragment) getActivity().getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
		}
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		googleMap.getUiSettings().setScrollGesturesEnabled(false);
		googleMap.getUiSettings().setZoomControlsEnabled(false);
		LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String bestProvider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(bestProvider);
		if (location != null) {
			onLocationChanged(location);
		}
		locationManager.requestLocationUpdates(bestProvider, 0, 0, this);
		
		googleMap.addMarker(new MarkerOptions()
				.position(randomlocation)
				.title("randomlocation")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

		final PieGraph pg = (PieGraph) view.findViewById(R.id.piegraph);
		PieSlice slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		slice.setTitle("first");
		pg.addSlice(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_left));
		slice.setValue(1);
		pg.addSlice(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_left));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_left));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_left));
		slice.setValue(15);
		pg.addSlice(slice);
		return view;
	}

	@Override
	public void onLocationChanged(Location location) {

		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		LatLng latLng = new LatLng(latitude, longitude);
		googleMap.addMarker(new MarkerOptions().position(latLng));
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
}
