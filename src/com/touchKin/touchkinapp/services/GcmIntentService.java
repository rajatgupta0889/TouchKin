package com.touchKin.touchkinapp.services;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.touchKin.touchkinapp.CircleNotificationActivity;
import com.touchKin.touchkinapp.DashBoardActivity;
import com.touchKin.touchkinapp.broadcastReciever.GcmBroadcastReceiver;
import com.touchKin.touckinapp.R;

public class GcmIntentService extends IntentService {

	public GcmIntentService() {
		super("GcmIntentService");
		// TODO Auto-generated constructor stub
	}

	public static String TAG = "GCMIntentService";
	private NotificationManager mNotificationManager;
	public static final int NOTIFICATION_ID = 1;
	NotificationCompat.Builder builder;

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver

		String messageType = gcm.getMessageType(intent);
		Log.d("Intent", messageType);
		if (!extras.isEmpty()) {
			/*
			 * filter message based on message Type. Since it is likely that GCM
			 * will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send Error: " + extras.toString(), "");
				Log.d(TAG, messageType);
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification(
						"Deleted messagess on server: " + extras.toString(), "");
				Log.d(TAG, messageType);
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				Log.d(TAG, extras.getString("message"));
				// Log.d(TAG, extras.toString());
				String message = extras.getString("message");

				sendNotification("Message is:"+message, "");

				for (int i = 0; i < 5; i++) {
					Log.i(TAG,
							"Working ... " + (i + 1) + "/5 @"
									+ SystemClock.elapsedRealtime());
					try {
						Thread.sleep(5000);

					} catch (InterruptedException exp) {

					}

				}
				Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
				Log.i(TAG, "Recieved @ " + extras.toString());
			}

		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg, String resultType) {
		// TODO Auto-generated method stub
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = null;
		String message = null;

		message = msg;
		intent = new Intent(this, CircleNotificationActivity.class);
		intent.putExtra("Flag", true);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Touchkin")
				.setStyle(
						new NotificationCompat.BigTextStyle().bigText(message))
				.setContentText(message)
				.setAutoCancel(true)
				.setSound(
						RingtoneManager
								.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

	}

}
