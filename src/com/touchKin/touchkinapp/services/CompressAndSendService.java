package com.touchKin.touchkinapp.services;
import java.io.File;

import java.io.IOException;
import java.security.PublicKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.netcompss.ffmpeg4android.GeneralUtils;
import com.netcompss.ffmpeg4android.Prefs;
import com.netcompss.loader.LoadJNI;
import com.touchKin.touchkinapp.SendTouchPreview;
import com.touchKin.touchkinapp.custom.AndroidMultiPartEntity;
import com.touchKin.touchkinapp.custom.AndroidMultiPartEntity.ProgressListener;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touckinapp.R;

public class CompressAndSendService extends Service {

	NotificationManager mNotifyManager;
	Notification.Builder mBuilder;
	int id = 1;
	private File videoDirectory = null;
	String workFolder = null;
	String videoFolder = null;
	String vkLogPath = null;
	String videoPath = null;
	private boolean commandValidationFailedFlag = false;
	Context _act;
	Intent intent;

	long totalSize = 0;

	String token;


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	protected File getVideoPath() {
		File dir = getVideoDirectory();

		dir.mkdirs();

		return (new File(dir, getVideoFilename()));
	}

	protected File getVideoDirectory() {
		if (videoDirectory == null) {
			initVideoDirectory();
		}

		return (videoDirectory);
	}

	private void initVideoDirectory() {
		videoDirectory = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
	}

	protected String getVideoFilename() {
		String ts = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
				.format(new Date());

		return ("Video_" + ts + ".mp4");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		this.intent = intent;
		videoPath = intent.getExtras().getString("videoPath");
		token = intent.getExtras().getString("token");
		videoFolder = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		workFolder = getApplicationContext().getFilesDir().getAbsolutePath()
				+ "/";
		vkLogPath = workFolder + "vk.log";
		GeneralUtils.copyLicenseFromAssetsToSDIfNeeded((Activity) _act,
				workFolder);
		Log.d("path", videoFolder);
		new TranscdingBackground(getApplicationContext()).execute();
	}

	String val;

	public class TranscdingBackground extends
			AsyncTask<String, Integer, Integer> {

		// ProgressDialog progressDialog;

		public TranscdingBackground(Context context) {
			_act = context;
		}

		@Override
		protected void onPreExecute() {
			// progressDialog = new ProgressDialog(_act);
			// progressDialog
			// .setMessage("FFmpeg4Android Transcoding in progress...");
			// progressDialog.show();

			mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mBuilder = new Notification.Builder(getApplicationContext());
			mBuilder.setContentTitle("Touchkin")
					.setContentText("Wait for a sec")
					.setSmallIcon(R.drawable.ic_launcher);
			// Start a lengthy operation in a background thread
			new Thread(new Runnable() {
				@Override
				public void run() {
					int incr;
					// Do the "lengthy" operation 20 times
					// for (incr = 0; incr <= 100; incr += 5) {
					// Sets the progress indicator to a max value, the
					// current completion percentage, and "determinate"
					// state
					mBuilder.setProgress(0, 0, true);
					// Displays the progress bar for the first time.
					mNotifyManager.notify(id, mBuilder.build());
					try {
						// Sleep for 2 seconds
						Thread.sleep(1 * 1000);
					} catch (InterruptedException e) {
					}
				}
			}).start();

		}

		protected Integer doInBackground(String... paths) {
			Log.i(Prefs.TAG, "doInBackground started...");

			PowerManager powerManager = (PowerManager) _act
					.getSystemService(Activity.POWER_SERVICE);
			WakeLock wakeLock = powerManager.newWakeLock(
					PowerManager.PARTIAL_WAKE_LOCK, "VK_LOCK");
			Log.d(Prefs.TAG, "Acquire wake lock");
			wakeLock.acquire();
			val = getVideoPath().toString();
			Log.d("value", val);

			String[] commandStr = { "ffmpeg", "-y", "-i", videoPath, "-strict",
					"experimental", "-r", "24", "-ac", "2", "-ar", "22050",
					"-b", "1600k", "-preset", "ultrafast", val };
			Log.d("cmd", "");

			LoadJNI vk = new LoadJNI();
			try {

				// complex command
				vk.run(commandStr, workFolder, _act);
				GeneralUtils.copyFileToFolder(vkLogPath, videoFolder);
			} catch (Throwable e) {
				Log.e(Prefs.TAG, "vk run exeption.", e);
			} finally {
				if (wakeLock.isHeld())
					wakeLock.release();
				else {
					Log.i(Prefs.TAG,
							"Wake lock is already released, doing nothing");
				}
			}
			Log.i(Prefs.TAG, "doInBackground finished");
			return Integer.valueOf(0);
		}

		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected void onCancelled() {
			Log.i(Prefs.TAG, "onCancelled");
			// progressDialog.dismiss();
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Integer result) {
			Log.i(Prefs.TAG, "onPostExecute");
			// progressDialog.dismiss();
			// Removes the progress bar
			mBuilder.setContentText("").setProgress(0, 0, false);

			mNotifyManager.notify(id, mBuilder.build());
			sendMedia(SendTouchPreview.type);
			super.onPostExecute(result);

			// finished Toast
			String rc = null;
			if (commandValidationFailedFlag) {
				rc = "Command Vaidation Failed";
			} else {
				rc = GeneralUtils.getReturnCodeFromLog(vkLogPath);
			}
			final String status = rc;
			// ((Activity) _act).runOnUiThread(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// Toast.makeText(getApplicationContext(), status,
			// Toast.LENGTH_LONG).show();
			// if (status.equals("Transcoding Status: Failed")) {
			// Toast.makeText(
			// getApplicationContext(),
			// "Check: " + vkLogPath
			// + " for more information.",
			// Toast.LENGTH_LONG).show();
			//
			// }
			//
			// }
			// });
		}
	}

	public void sendMedia(int type) {

		new ImageUploadTask(this).execute();

	}

	class ImageUploadTask extends AsyncTask<String, String, String> {

		Context context;

		public ImageUploadTask(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected String doInBackground(String... unsued) {
			return uploadFile();
			// try {
			// File file = new File(val);
			//
			// // this is storage overwritten on each iteration with bytes
			// AppController.mHttpClient.getParams().setParameter(
			// CoreProtocolPNames.PROTOCOL_VERSION,
			// HttpVersion.HTTP_1_1);
			//
			// HttpClient httpClient = AppController.mHttpClient;
			//
			// HttpPost httpPost = new HttpPost(
			// "http://54.69.183.186:1340/kinbook/message/add");
			//
			// MultipartEntity entity = new MultipartEntity(
			// HttpMultipartMode.BROWSER_COMPATIBLE);
			//
			//
			//
			// entity.addPart("shared_with",
			// new StringBody(SendTouchPreview.getCheckedParentId()));
			// entity.addPart("message", new StringBody(
			// SendTouchPreview.sendmessage.getText().toString()));
			// ContentBody cbFile = new FileBody(
			// file,
			// ContentType.create(getMimeType(file.getAbsolutePath())),
			// file.getName());
			//
			// entity.addPart("media", cbFile);
			//
			// // entity.addPart("media", fileBody);
			// Log.d("ParentId", SendTouchPreview.getCheckedParentId()
			// .toString());
			//
			// // entity.addPart("photoCaption", new
			// // StringBody(caption.getText()
			// // .toString()));
			// httpPost.setEntity(entity);
			// Log.d("HttpPost", httpPost.getEntity() + "");
			// HttpResponse response = httpClient.execute(httpPost);
			// HttpEntity resEntity = response.getEntity();
			//
			// System.out.println(response.getStatusLine());
			// // if (resEntity != null) {
			// // System.out.println(EntityUtils.toString(resEntity));
			// // }
			// // if (resEntity != null) {
			// // resEntity.consumeContent();
			// // }
			// return EntityUtils.toString(resEntity);
			// } catch (Exception e) {
			// Log.e(e.getClass().getName(), e.getMessage(), e);
			// return null;
			// }
		}
		@SuppressWarnings("deprecation")
		private String uploadFile() {
			String responseString = null;

			HttpClient httpclient = new DefaultHttpClient();
			AppController.mHttpClient.getParams().setParameter(
					CoreProtocolPNames.PROTOCOL_VERSION,
					HttpVersion.HTTP_1_1);

			HttpClient httpClient = AppController.mHttpClient;
			HttpPost httppost = new HttpPost("http://54.69.183.186:1340/kinbook/message/add");
			httppost.setHeader("Authorization", "Bearer " + token);
			try {
				AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
						new ProgressListener() {

							@Override
							public void transferred(long num) {
								publishProgress(""+(int) ((num / (float) totalSize) * 100));
							}
						});

				File sourceFile = new File(val);
				entity.addPart("shared_with",
						new StringBody(SendTouchPreview.getCheckedParentId()));
				entity.addPart("message", new StringBody(
						SendTouchPreview.sendmessage.getText().toString()));
				ContentBody cbFile = new FileBody(
						sourceFile,
						ContentType.create(getMimeType(sourceFile.getAbsolutePath())),
						sourceFile.getName());
				entity.addPart("media", cbFile);

				// entity.addPart("media", fileBody);
				Log.d("ParentId", SendTouchPreview.getCheckedParentId()
						.toString());


//				entity.addPart("media", cbFile);
//
//				// Adding file data to http body
//				entity.addPart("image", new FileBody(sourceFile));
//
//				// Extra parameters if you want to pass to server
//				entity.addPart("website",
//						new StringBody("www.androidhive.info"));
//				entity.addPart("email", new StringBody("abc@gmail.com"));

				totalSize = entity.getContentLength();
				httppost.setEntity(entity);

				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					responseString = EntityUtils.toString(r_entity);
				} else {
					responseString = "Error occurred! Http Status Code: "
							+ statusCode;
				}

			} catch (ClientProtocolException e) {
				responseString = e.toString();
			} catch (IOException e) {
				responseString = e.toString();
			}

			return responseString;

		}

		@Override
		protected void onProgressUpdate(String... unsued) {
		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				if (sResponse != null) {
					// hidepDialog();
					Log.d("Response", sResponse);
					JSONObject JResponse = new JSONObject(sResponse);
					Log.d("JSON", JResponse.toString());
					Toast.makeText(context, "your message sent",
							Toast.LENGTH_SHORT).show();
					mBuilder.setContentText("Sent").setProgress(0, 0, false);
					mNotifyManager.notify(id, mBuilder.build());
				} else {
					mBuilder.setContentText("Problem while sending a touch")
							.setProgress(0, 0, false);
					mNotifyManager.notify(id, mBuilder.build());
					Toast.makeText(context, "Problem while sending a touch",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
			stopService(intent);
		}

		@Override
		protected void onPreExecute() {
			// showpDialog();
			mBuilder.setContentText("sending...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					mBuilder.setProgress(0, 0, true);
					// Displays the progress bar for the first time.
					mNotifyManager.notify(id, mBuilder.build());
					// Sleeps the thread, simulating an operation
					// that takes time
					try {
						// Sleep for 5 seconds
						Thread.sleep(1 * 1000);
					} catch (InterruptedException e) {
						// Log.d(TAG, "sleep failure");
					}
				}
			}
			// Starts the thread by calling the run() method in its Runnable
			).start();
		}

	}

	public static String getMimeType(String url) {
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		if (extension != null) {
			MimeTypeMap mime = MimeTypeMap.getSingleton();
			type = mime.getMimeTypeFromExtension(extension);
		}
		Log.d("Type", type);
		return type;
	}
}
