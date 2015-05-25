package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.touchKin.touchkinapp.custom.Constants;
import com.touchKin.touchkinapp.custom.GeofenceErrorMessages;
import com.touchKin.touchkinapp.model.Validation;
import com.touchKin.touchkinapp.services.GeofenceTransitionsIntentService;
import com.touchKin.touckinapp.R;

public class MapActivity extends ActionBarActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, OnMarkerClickListener,
		ResultCallback<Status> {
	String text = "";
	private GoogleMap googleMap;
	protected GoogleApiClient mGoogleApiClient;
	protected Location mLastLocation;
	Marker googleMarker = null;
	protected ArrayList<Geofence> mGeofenceList;
	private boolean mGeofencesAdded;
	private PendingIntent mGeofencePendingIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_fragment);
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
		}
		mGeofenceList = new ArrayList<Geofence>();
		mGeofencePendingIntent = null;
		populateGeofenceList();
		buildGoogleApiClient();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		googleMap.setMyLocationEnabled(true);
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
		LatLng latLng = new LatLng(mLastLocation.getLatitude(),
				mLastLocation.getLongitude());
		if (mLastLocation != null) {
			View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.custom_marker, null);

			if (googleMarker != null)
				googleMarker.remove();
			googleMarker = googleMap.addMarker(new MarkerOptions()
					.position(latLng)
					.title("randomlocation")
					.icon(BitmapDescriptorFactory.fromBitmap(CustomMarkerView(
							this, marker))));
			googleMarker = googleMap.addMarker(new MarkerOptions()
					.position(new LatLng(12.9667d, 77.5667d))
					.title("randomlocation")
					.icon(BitmapDescriptorFactory.fromBitmap(CustomMarkerView(
							this, marker))));

			googleMarker = googleMap.addMarker(new MarkerOptions()
					.position(new LatLng(12.9259d, 77.6229d))
					.title("randomlocation")
					.icon(BitmapDescriptorFactory.fromBitmap(CustomMarkerView(
							this, marker))));

			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
			googleMap.setOnMarkerClickListener(this);

		} else {
			Toast.makeText(this, "No Location Detected", Toast.LENGTH_LONG)
					.show();
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
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
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

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		DialogFragment newFragment = new MapDialogFragment();
		newFragment.setCancelable(false);
		newFragment.show(getSupportFragmentManager(), "TAG");
		if (!text.isEmpty())
			marker.setTitle(text);
		return false;
	}

	public class MapDialogFragment extends DialogFragment {
		static final int PICK_CONTACT_CIRCLE = 1;
		static final int PICK_CONTACT_KIN = 2;
		EditText nameBox;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			// Get the layout inflater
			LayoutInflater inflater = getActivity().getLayoutInflater();
			text = "";
			// Inflate and set the layout for the dialog
			// Pass null as the parent view because its going in the dialog
			// layout
			// Bundle mArgs = getArguments();

			View view = inflater.inflate(R.layout.set_location_dialog, null);
			Button addLocButton = (Button) view.findViewById(R.id.addButton);
			nameBox = (EditText) view.findViewById(R.id.customLoc);

			// nameBox.setText(mArgs.getString("name"));
			// phoneBox.setText(mArgs.getString("number"));

			// title.setText(mArgs.getString("title"));
			builder.setCancelable(false);
			builder.setView(view)
					// Add action buttons
					.setIcon(R.drawable.ic_action_uset)
					.setPositiveButton("Add",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									// sign in the user ...
								}

							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									MapDialogFragment.this.getDialog().cancel();
								}
							});
			final AlertDialog dialog = builder.create();
			dialog.show();
			Button positiveButton = (Button) dialog
					.getButton(Dialog.BUTTON_POSITIVE);
			positiveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Boolean wantToCloseDialog = false;
					// Do stuff, possibly set wantToCloseDialog to true
					// then...

					if (wantToCloseDialog)
						dismiss();

					if (Validation.hasText(nameBox)) {
						text = nameBox.getText().toString();
						dismiss();
					}
					// else dialog stays open. Make sure you have an obvious
					// way to close the dialog especially if you set
					// cancellable to false.
				}
			});

			addLocButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
					text = "home";
				}
			});
			return dialog;
		}
	}

	@Override
	public void onResult(Status status) {
		// TODO Auto-generated method stub
		if (status.isSuccess()) {
			// Update state and save in shared preferences.
			mGeofencesAdded = !mGeofencesAdded;
			// // SharedPreferences.Editor editor = mSharedPreferences.edit();
			// editor.putBoolean(Constants.GEOFENCES_ADDED_KEY,
			// mGeofencesAdded);
			// editor.commit();

			// Update the UI. Adding geofences enables the Remove Geofences
			// button, and removing
			// geofences enables the Add Geofences button.
			// setButtonsEnabledState();

			// Toast.makeText(
			// this,
			// getString(mGeofencesAdded ? R.string.geofences_added
			// : R.string.geofences_removed), Toast.LENGTH_SHORT)
			// .show();
		} else {
			// Get the status code for the error and log it using a
			// user-friendly message.
			String errorMessage = GeofenceErrorMessages.getErrorString(this,
					status.getStatusCode());
			Log.e("Error", errorMessage);
		}
	}

	private GeofencingRequest getGeofencingRequest() {
		GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

		// The INITIAL_TRIGGER_ENTER flag indicates that geofencing service
		// should trigger a
		// GEOFENCE_TRANSITION_ENTER notification when the geofence is added and
		// if the device
		// is already inside that geofence.
		builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

		// Add the geofences to be monitored by geofencing service.
		builder.addGeofences(mGeofenceList);

		// Return a GeofencingRequest.
		return builder.build();
	}

	private PendingIntent getGeofencePendingIntent() {
		// Reuse the PendingIntent if we already have it.
		if (mGeofencePendingIntent != null) {
			return mGeofencePendingIntent;
		}
		Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
		// We use FLAG_UPDATE_CURRENT so that we get the same pending intent
		// back when calling
		// addGeofences() and removeGeofences().
		return PendingIntent.getService(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	/**
	 * This sample hard codes geofence data. A real app might dynamically create
	 * geofences based on the user's location.
	 */
	public void populateGeofenceList() {
		for (Map.Entry<String, LatLng> entry : Constants.BAY_AREA_LANDMARKS
				.entrySet()) {

			mGeofenceList.add(new Geofence.Builder()
			// Set the request ID of the geofence. This is a string to identify
			// this
			// geofence.
					.setRequestId(entry.getKey())

					// Set the circular region of this geofence.
					.setCircularRegion(entry.getValue().latitude,
							entry.getValue().longitude,
							Constants.GEOFENCE_RADIUS_IN_METERS)

					// Set the expiration duration of the geofence. This
					// geofence gets automatically
					// removed after this period of time.
					.setExpirationDuration(
							Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

					// Set the transition types of interest. Alerts are only
					// generated for these
					// transition. We track entry and exit transitions in this
					// sample.
					.setTransitionTypes(
							Geofence.GEOFENCE_TRANSITION_ENTER
									| Geofence.GEOFENCE_TRANSITION_EXIT)

					// Create the geofence.
					.build());
		}
	}

	public void addGeofencesButtonHandler(View view) {
		if (!mGoogleApiClient.isConnected()) {
			Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		try {
			LocationServices.GeofencingApi.addGeofences(mGoogleApiClient,
			// The GeofenceRequest object.
					getGeofencingRequest(),
					// A pending intent that that is reused when calling
					// removeGeofences(). This
					// pending intent is used to generate an intent when a
					// matched geofence
					// transition is observed.
					getGeofencePendingIntent()).setResultCallback(this); // Result
																			// processed
																			// in
																			// onResult().
		} catch (SecurityException securityException) {
			// Catch exception generated if the app does not use
			// ACCESS_FINE_LOCATION permission.
			// logSecurityException(securityException);
		}
	}

}
