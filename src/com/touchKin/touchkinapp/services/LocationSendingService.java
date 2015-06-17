package com.touchKin.touchkinapp.services;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.touchKin.touchkinapp.model.AppController;

public class LocationSendingService extends Service implements LocationListener {
	private Context mContext;

	// flag for GPS status
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	// flag for GPS status
	boolean canGetLocation = false;

	Location location; // location
	double latitude; // latitude
	double longitude; // longitude

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	// Declaring a Location Manager
	protected LocationManager locationManager;

	public LocationSendingService(Context context) {
		this.mContext = context;
		getLocation();
	}

	public LocationSendingService() {

	}

	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				// First get location from Network Provider
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	/**
	 * Stop using GPS listener Calling this function will stop using GPS in your
	 * app
	 * */
	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(LocationSendingService.this);
		}
	}

	/**
	 * Function to get latitude
	 * */
	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}

		// return latitude
		return latitude;
	}

	/**
	 * Function to get longitude
	 * */
	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}

		// return longitude
		return longitude;
	}

	/**
	 * Function to check GPS/wifi enabled
	 * 
	 * @return boolean
	 * */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	/**
	 * Function to show settings alert dialog On pressing Settings button will
	 * lauch Settings Options
	 * */
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle("GPS is settings");

		// Setting Dialog Message
		alertDialog
				.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						mContext.startActivity(intent);
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, " MyService Created ", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Toast.makeText(this, " MyService Started", Toast.LENGTH_LONG).show();
		mContext = getApplicationContext();
		Location loc = getLocation();
		JSONObject param = new JSONObject();
		JSONObject point = new JSONObject();
		if (loc != null) {
			try {
				param.put("y", loc.getLongitude());
				param.put("x", loc.getLatitude());
				point.put("poin", param);
				sendLocationToServer(point);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Servics Stopped", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}

	public void sendLocationToServer(JSONObject param) {
		// JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
		// "http://54.69.183.186:1340/location/add", param,
		// new Response.Listener<JSONObject>() {
		// @Override
		// public void onResponse(JSONObject response) {
		//
		// Log.d("Response", "" + response);
		// // Log.d("mobile", "" + pref.getString("mobile",
		// // null));
		// // Log.d("otp", "" + pref.getString("mobile",
		// // null));
		//
		// }
		// }, new Response.ErrorListener() {
		// @Override
		// public void onErrorResponse(VolleyError error) {
		// Log.d("Response", "" + error);
		// }
		//
		// });
		//
		// AppController.getInstance().addToRequestQueue(req);
		new SendDataInBG().execute(param);
	}

	public class SendDataInBG extends AsyncTask<JSONObject, Void, String> {
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.d("result", result);
		}

		String response = null;

		@SuppressWarnings("deprecation")
		@Override
		protected String doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;
			try {

				// this is storage overwritten on each iteration with bytes

				HttpClient httpClient = AppController.mHttpClient;

				HttpPost httpPost = new HttpPost(
						"http://54.69.183.186:1340/location/add");
				// adding post params
				if (params != null) {
					httpPost.setEntity(new StringEntity(params.toString()));
				}

				httpResponse = httpClient.execute(httpPost);
				// entity.addPart("media", fileBody);

				// entity.addPart("photoCaption", new
				// StringBody(caption.getText()
				// .toString()));

				httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				Log.d("Response", response);
				// if (resEntity != null) {
				// System.out.println(EntityUtils.toString(resEntity));
				// }
				// if (resEntity != null) {
				// resEntity.consumeContent();
				// }
				return response;
			} catch (Exception e) {
				Log.e(e.getClass().getName(), e.getMessage(), e);
				return response;
			}

			// (null);
		}
	}

}
