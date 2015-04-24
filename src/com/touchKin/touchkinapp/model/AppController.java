package com.touchKin.touchkinapp.model;

import java.util.Map;

import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.Volley;

@SuppressWarnings("deprecation")
public class AppController extends Application {
	private static final String SET_COOKIE_KEY = "Set-Cookie";
	private static final String COOKIE_KEY = "Cookie";
	private static final String SESSION_COOKIE = "sessionid";
	/**
	 * Log or request TAG
	 */
	public static DefaultHttpClient mHttpClient = new DefaultHttpClient();;
	public static final String TAG = AppController.class.getSimpleName();
	/**
	 * Global request queue for Volley
	 */
	private RequestQueue mRequestQueue;

	private SharedPreferences _preferences;
	/**
	 * A singleton instance of the application class for easy access in other
	 * places
	 */
	private static AppController mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		// initialize the singleton
		mInstance = this;
		_preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Log.d("Pref", _preferences.getString(SESSION_COOKIE, ""));
	}

	/**
	 * @return ApplicationController singleton instance
	 */

	public static synchronized AppController getInstance() {
		return mInstance;

	}

	/**
	 * @return The Volley Request queue, the queue will be created if it is null
	 */

	public RequestQueue getRequestQueue() {
		// lazy initialize the request queue, the queue instance will be
		// created when it is accessed for the first time
		// lazy initialize the request queue, the queue instance will be
		// created when it is accessed for the first time
		if (mRequestQueue == null) {
			// Create an instance of the Http client.
			// We need this in order to access the cookie store
			// create the request queue
			mRequestQueue = Volley.newRequestQueue(this, new HttpClientStack(
					mHttpClient));
		}
		return mRequestQueue;
	}

	/**
	 * Adds the specified request to the global queue, if tag is specified then
	 * it is used else Default TAG is used.
	 * 
	 * @param req
	 * @param tag
	 */

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		VolleyLog.d("Adding request to queue: %s", req.getUrl());
		getRequestQueue().add(req);
	}

	/**
	 * Adds the specified request to the global queue using the Default TAG.
	 * 
	 * @param req
	 * @param tag
	 */

	public <T> void addToRequestQueue(Request<T> req) {
		// set the default tag if tag is empty
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	/**
	 * Cancels all pending requests by the specified TAG, it is important to
	 * specify a TAG so that the pending/ongoing requests can be cancelled.
	 * 
	 * @param tag
	 */

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	/**
	 * Checks the response headers for session cookie and saves it if it finds
	 * it.
	 * 
	 * @param headers
	 *            Response Headers.
	 */
	public final void checkSessionCookie(Map<String, String> headers) {
		if (headers.containsKey(SET_COOKIE_KEY)
				&& headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
			String cookie = headers.get(SET_COOKIE_KEY);
			if (cookie.length() > 0) {
				String[] splitCookie = cookie.split(";");
				String[] splitSessionId = splitCookie[0].split("=");
				cookie = splitSessionId[1];
				Editor prefEditor = _preferences.edit();
				prefEditor.putString(SESSION_COOKIE, cookie);
				prefEditor.commit();
			}
		}
	}

	/**
	 * Adds session cookie to headers if exists.
	 * 
	 * @param headers
	 */
	public final void addSessionCookie(Map<String, String> headers) {
		String sessionId = _preferences.getString(SESSION_COOKIE, "");
		if (sessionId.length() > 0) {
			StringBuilder builder = new StringBuilder();
			builder.append(SESSION_COOKIE);
			builder.append("=");
			builder.append(sessionId);
			if (headers.containsKey(COOKIE_KEY)) {
				builder.append("; ");
				builder.append(headers.get(COOKIE_KEY));
			}
			headers.put(COOKIE_KEY, builder.toString());
			Log.d("Pref", _preferences.getString(SESSION_COOKIE, ""));
		}
	}
}