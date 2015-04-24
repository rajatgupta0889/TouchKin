package com.touchKin.touchkinapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touckinapp.R;

public class SplashActivity extends Activity {
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 2000;
	ProgressBar progBar;
	private int mProgressStatus = 0;
	private Handler mHandler = new Handler();
	String applicationId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		SharedPreferences pref = this.getSharedPreferences("loginPref", 0);
		progBar = (ProgressBar) findViewById(R.id.progressBar1);
		// pref.edit().putString(GetUserLogin.UserTom, null);
		// pref.edit().commit();
		// System.out.println(pref.getString(GetUserLogin.UserTom, null));
		new Thread(new Runnable() {
			public void run() {
				while (mProgressStatus < 100) {

					progBar.incrementProgressBy(1);

					// Update the progress bar
					mHandler.post(new Runnable() {
						public void run() {
							progBar.setProgress(mProgressStatus);
						}
					});
				}
			}

		}).start();
		Log.d("Mobile", "" + pref.getString("mobile", null));
		Log.d("otp", "" + pref.getString("otp", null));
		if (pref.getString("mobile", null) != null
				&& pref.getString("otp", null) != null) {
			sendIntent(pref.getString("mobile", null),
					pref.getString("otp", null));
		} else {
			new Handler().postDelayed(new Runnable() {

				/*
				 * Showing splash screen with a timer. This will be useful when
				 * you want to show case your app logo / company
				 */

				@SuppressLint("NewApi")
				@Override
				public void run() {
					// This method will be executed once the timer is over
					// Start your app main activity
					Intent i = new Intent(SplashActivity.this,
							SignUpActivity.class);
					Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
							getApplicationContext(), R.anim.animation,
							R.anim.animation2).toBundle();
					startActivity(i, bndlanimation);
					finish();
					// close this activity
					finish();
				}
			}, SPLASH_TIME_OUT);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		return true;
	}

	public void sendIntent(String phone, String otp) {
		if (phone != null && otp != null) {

			JSONObject params = new JSONObject();
			try {
				params.put("mobile", phone);
				params.put("code", otp);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
					"http://54.69.183.186:1340/user/verify-mobile", params,
					new Response.Listener<JSONObject>() {
						@SuppressLint("NewApi")
						@Override
						public void onResponse(JSONObject response) {
							Intent i = new Intent(SplashActivity.this,
									DashBoardActivity.class);
							Bundle bndlanimation = ActivityOptions
									.makeCustomAnimation(
											getApplicationContext(),
											R.anim.animation, R.anim.animation2)
									.toBundle();
							startActivity(i, bndlanimation);
							finish();
							// Log.d(TAG, response.toString());
							// VolleyLog.v("Response:%n %s",
							// response.toString(4));
						}
					}, new Response.ErrorListener() {
						@SuppressLint("NewApi")
						@Override
						public void onErrorResponse(VolleyError error) {
							VolleyLog.e("Error: ", error.getMessage());
							Toast.makeText(getApplicationContext(),
									error.getMessage(), Toast.LENGTH_SHORT)
									.show();
							Intent i = new Intent(SplashActivity.this,
									SignUpActivity.class);
							Bundle bndlanimation = ActivityOptions
									.makeCustomAnimation(
											getApplicationContext(),
											R.anim.animation, R.anim.animation2)
									.toBundle();

							startActivity(i, bndlanimation);
							finish();
						}

					});

			AppController.getInstance().addToRequestQueue(req);
		}
	}

}
