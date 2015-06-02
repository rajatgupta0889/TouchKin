package com.touchKin.touchkinapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.touchKin.touchkinapp.custom.CustomRequest;
import com.touchKin.touchkinapp.custom.ImageLoader;
import com.touchKin.touchkinapp.custom.RoundedImageView;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touchkinapp.model.RequestModel;
import com.touchKin.touckinapp.R;

public class Details extends ActionBarActivity implements OnClickListener {

	final int PIC_CROP = 2;
	private Uri selectedImageUri;
	Button next;
	TextView detail, phone_detail, userYear;
	EditText name;
	String name_detail, phone;
	boolean hasFocus = false;
	private static int RESULT_LOAD_IMG = 1;
	String imgDecodableString;
	RoundedImageView imgView;
	ImageView addImageView;
	String previewFilePath;
	String userID, userName = null;
	String serverPath = "https://s3-ap-southeast-1.amazonaws.com/touchkin-dev/avatars/";
	Boolean exists;
	ImageLoader imgLoader;
	EditText userAge;
	RadioGroup radioGroup;
	String image_url;
	private ProgressDialog pDialog;
	private Toolbar toolbar;
	TextView mTitle;
	List<RequestModel> requestList;
	final String TAG = "Details";
	Boolean male = true;
	String yob = null;

	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	String SENDER_ID = "588149057277";

	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	Context context;
	String regid;
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
		init();
		context = Details.this;
		// if (getIntent() != null) {
		// if (getIntent().getExtras().getBoolean("fromOtp")) {
		// phone = getIntent().getExtras().getString("phoneNumber");
		// userID = getIntent().getExtras().getString("id");
		// if (getIntent().getExtras().getString("first_name") != null)
		// userName = getIntent().getExtras().getString("first_name");
		// } else {
		// getUserInfo();
		// }
		// }
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(Details.this);
			regid = getRegistrationId(context);

			if (regid.isEmpty()) {
				registerInBackground();
			}
			/*
			 * if(Session.getActiveSession() != null){ startActivity(new
			 * Intent(getActivity(),LoginActivity.class)); }
			 */

		} else {
			Log.i(TAG, "No valid Google Play Services APK found.");
		}
		getUserInfo();
		pDialog.setMessage("Updating info");
		pDialog.setCancelable(false);
		// phone_detail.setText(phone);
		// Image url
		// image_url = serverPath + userID + ".jpeg";
		mTitle.setText("Profile");
		// ImageLoader class instance
		imgLoader = new ImageLoader(getApplicationContext());
		// new MyTask().execute(image_url);
		// whenever you want to load an image from url
		// call DisplayImage function
		// url - image url to load
		// loader - loader image, will be displayed before getting image
		// image - ImageView

		next.setOnClickListener(this);
		detail.setOnClickListener(this);
		// if (userName != null && !userName.isEmpty()) {
		// detail.setText(userName);
		// name.setText(userName);
		// }
		// if (yob != null) {
		// userYear.setText(yob);
		// Calendar calendar = Calendar.getInstance();
		// int year = calendar.get(Calendar.YEAR);
		// userAge.setText(year - Integer.parseInt(yob));
		// }
		userAge.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
						|| (actionId == EditorInfo.IME_ACTION_DONE)) {
					// Toast.makeText(MainActivity.this, "enter press",
					// Toast.LENGTH_LONG).show();
					String age = userAge.getText().toString();
					Calendar calendar = Calendar.getInstance();
					int year = calendar.get(Calendar.YEAR);
					int yob = year - Integer.parseInt(age);
					userYear.setText(" " + yob);
				}
				return false;
			}
		});

		radioGroup.check(R.id.radioMale);
		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup arg0, int id) {
						switch (id) {
						case R.id.radioFemale:
							Log.v(TAG, "female");
							male = false;
							break;
						default:
							Log.v(TAG, "Male?");
							male = true;
							break;
						}
					}
				});
	}

	private void init() {
		// TODO Auto-generated method stub
		next = (Button) findViewById(R.id.next_detail_button);
		detail = (TextView) findViewById(R.id.add_name);
		phone_detail = (TextView) findViewById(R.id.phn_number_detail);
		name = (EditText) findViewById(R.id.edit_name);
		addImageView = (ImageView) findViewById(R.id.profile_pic);
		imgView = (RoundedImageView) findViewById(R.id.change_profile_pic);
		pDialog = new ProgressDialog(this);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
		requestList = new ArrayList<RequestModel>();
		userAge = (EditText) findViewById(R.id.userAge);
		userYear = (TextView) findViewById(R.id.userYear);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.next_detail_button:
			if (!name.getText().toString().isEmpty()
					&& !userAge.getText().toString().isEmpty()) {
				String userName = name.getText().toString();
				String gender = "male";
				if (!male) {
					gender = "female";
				}
				String yob = userYear.getText().toString();
				updateUser(userName, gender, yob);
				getConnectionRequest();
			} else {
				Toast.makeText(Details.this, "PLease Add your Name",
						Toast.LENGTH_SHORT).show();
			}
			// =======
			// // if (!name.getText().toString().isEmpty()) {
			// // String userName = name.getText().toString();
			// // updateUser(userName);
			// // getConnectionRequest();
			// Intent intent = new Intent(Details.this, AddCareActivity.class);
			// Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
			// getApplicationContext(), R.anim.animation,
			// R.anim.animation2).toBundle();
			// startActivity(intent, bndlanimation);
			// // } else {
			// // Toast.makeText(Details.this, "PLease Add your Name",
			// // Toast.LENGTH_SHORT).show();
			// // }
			// >>>>>>> 894dff3bee0a0f3854c747e997d954fd54a5a35c
			break;
		case R.id.add_name:
			detail.setVisibility(View.GONE);
			name.setVisibility(View.VISIBLE);
			// name_detail = name.getText().toString();
			// detail.setText(name_detail);
			break;
		default:
			break;
		}
	}

	public void loadImagefromGallery(View view) {
		// Create intent to Open Image applications like Gallery, Google Photos
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// Start the Intent
		startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			// When an Image is picked
			if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
					&& null != data) {
				// Get the Image from data

				selectedImageUri = data.getData();

				performCrop();
			} else if (requestCode == PIC_CROP) {

				// get the returned data
				Bundle extras = data.getExtras();
				// get the cropped bitmap
				Bitmap thePic = extras.getParcelable("data");
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				thePic.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

				// you can create a new file name "test.jpg" in sdcard folder.
				File f = new File(Environment.getExternalStorageDirectory()
						+ File.separator + "test.jpg");
				f.createNewFile();
				previewFilePath = f.getAbsolutePath();
				// write the bytes in file
				FileOutputStream fo = new FileOutputStream(f);
				fo.write(bytes.toByteArray());

				// remember close de FileOutput
				fo.close();
				// String tempPath = getPath(selectedImageUri, this);
				// BitmapFactory.Options btmapOptions = new
				// BitmapFactory.Options();
				// Bitmap bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
				// // Set the Image in ImageView after decoding the String
				// previewFilePath = tempPath;

				imgView.setImageBitmap(thePic);
				new ImageUploadTask(this).execute();

			} else {
				Toast.makeText(this, "You haven't picked Image",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
					.show();
		}

	}

	private void performCrop() {

		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(selectedImageUri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 256);
			cropIntent.putExtra("outputY", 256);
			// retrieve data on return
			cropIntent.putExtra("return-data", true);
			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, PIC_CROP);

		} catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	class ImageUploadTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (previewFilePath != null) {
				File f = new File(previewFilePath);
				if (f != null) {
					if (f.delete())
						Log.d("file", "Update and delete");

				}
			}
		}

		Context context;

		public ImageUploadTask(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
			imgLoader.clearCache();
		}

		@SuppressWarnings("deprecation")
		@Override
		protected Void doInBackground(Void... unsued) {
			try {

				File file = new File(previewFilePath);

				// this is storage overwritten on each iteration with bytes
				AppController.mHttpClient.getParams().setParameter(
						CoreProtocolPNames.PROTOCOL_VERSION,
						HttpVersion.HTTP_1_1);

				HttpClient httpClient = AppController.mHttpClient;

				HttpPost httpPost = new HttpPost(
						"http://54.69.183.186:1340/user/avatar");

				MultipartEntity entity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				ContentBody cbFile = new FileBody(
						file,
						ContentType.create(getMimeType(file.getAbsolutePath())),
						file.getName());

				entity.addPart("avatar", cbFile);

				// entity.addPart("media", fileBody);

				// entity.addPart("photoCaption", new
				// StringBody(caption.getText()
				// .toString()));
				httpPost.setEntity(entity);
				Log.d("HttpPost", httpPost.getEntity() + "");
				httpClient.execute(httpPost);

				// if (resEntity != null) {
				// System.out.println(EntityUtils.toString(resEntity));
				// }
				// if (resEntity != null) {
				// resEntity.consumeContent();
				// }
				return null;
			} catch (Exception e) {
				Log.e(e.getClass().getName(), e.getMessage(), e);
				return null;
			}

			// (null);
		}

		// @Override
		// protected void onProgressUpdate(Void... unsued) {
		//
		// }

	}

	public String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaColumns.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = activity
				.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public String getPath(Uri uri) {
		// just some safety built in
		if (uri == null) {
			// TODO perform some logging or show user feedback
			return null;
		}
		// try to retrieve the image from the media store first
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };

		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		// this is our fallback here
		return uri.getPath();
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

	//
	// private class MyTask extends AsyncTask<String, Void, Boolean> {
	//
	// @Override
	// protected void onPreExecute() {
	//
	// }
	//
	// @Override
	// protected Boolean doInBackground(String... params) {
	//
	// try {
	// HttpURLConnection.setFollowRedirects(false);
	// HttpURLConnection con = (HttpURLConnection) new URL(params[0])
	// .openConnection();
	// con.setRequestMethod("HEAD");
	// System.out.println(con.getResponseCode());
	// Boolean result = con.getResponseCode() == HttpURLConnection.HTTP_OK;
	// con.disconnect();
	// return (result);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// }
	//
	// @Override
	// protected void onPostExecute(Boolean result) {
	// if (result) {
	// imgLoader.DisplayImage(image_url, R.drawable.people, imgView);
	// // addImageView.setVisibility(View.INVISIBLE);
	// }
	// }
	// }

	// public static boolean exists(String URLName) {
	// try {
	// HttpURLConnection.setFollowRedirects(false);
	// // note : you may also need
	// // HttpURLConnection.setInstanceFollowRedirects(false)
	// HttpURLConnection con = (HttpURLConnection) new URL(URLName)
	// .openConnection();
	// con.setRequestMethod("HEAD");
	// return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// }
	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

	private void updateUser(final String name, final String gender,
			final String yob) {
		// TODO Auto-generated method stub
		showpDialog();
		JSONObject params = new JSONObject();
		try {
			params.put("first_name", name);
			params.put("gender", gender);
			params.put("yob", yob);
			params.put("mobile_device_id", getRegistrationId(context));
			params.put("mobile_os", "android");

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
				"http://54.69.183.186:1340/user/complete-profile", params,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						hidepDialog();
						SharedPreferences pref = getApplicationContext()
								.getSharedPreferences("loginPref", 0);
						try {

							Editor edit = pref.edit();
							edit.putString("name",
									response.getString("first_name"));
							edit.apply();
							// Log.d("Response", "" + response);
							// Log.d("mobile", "" + pref.getString("mobile",
							// null));
							// Log.d("otp", "" + pref.getString("mobile",
							// null));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// Intent i = new Intent(Details.this,
						// CircleNotificationActivity.class);
						// Bundle bndlanimation = ActivityOptions
						// .makeCustomAnimation(getApplicationContext(),
						// R.anim.animation, R.anim.animation2)
						// .toBundle();
						// i.putExtra("phoneNumber", phone);
						// i.putExtra("id", userID);
						//
						// i.putExtra("first_name", name);
						//
						// startActivity(i, bndlanimation);

						// Log.d(TAG, response.toString());
						// VolleyLog.v("Response:%n %s",
						// response.toString(4));

						// finish();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						String json = null;
						Toast.makeText(Details.this, "PLease Error" + error,
								Toast.LENGTH_SHORT).show();
						NetworkResponse response = error.networkResponse;
						hidepDialog();
						if (response != null && response.data != null) {
							int code = response.statusCode;
							json = new String(response.data);
							// json = trimMessage(json, "message");
							// if (json != null)
							// displayMessage(json, code);
							//
							// }
							// hidepDialog();
						}
					}

				});

		AppController.getInstance().addToRequestQueue(req);
	}

	private void getConnectionRequest() {
		// TODO Auto-generated method stub
		JsonArrayRequest req = new JsonArrayRequest(
				"http://54.69.183.186:1340/user/connection-requests",
				new Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray responseArray) {
						// TODO Auto-generated method stub
						Log.d("Response Array", " " + responseArray);
						if (responseArray.length() > 0) {
							for (int i = 0; i < responseArray.length(); i++) {
								try {
									RequestModel item = new RequestModel();
									JSONObject careRequest = responseArray
											.getJSONObject(i);
									JSONObject careInitiator = careRequest
											.getJSONObject("initiator");
									JSONObject careGiver = careRequest
											.getJSONObject("care_giver");
									JSONObject careReciever = careRequest
											.getJSONObject("care_receiver");
									if (careInitiator.getString("id").equals(
											careGiver.get("id"))) {
										if (careInitiator.has("nickname")) {
											item.setCare_reciever_name(careInitiator
													.getString("nickname"));
										} else if (careInitiator
												.has("first_name")) {
											item.setCare_reciever_name(careInitiator
													.getString("first_name"));
										} else {
											item.setCare_reciever_name(careInitiator
													.getString("mobile"));
										}
										item.setReqMsg(item
												.getCare_reciever_name()
												+ " wants to care for you");
									}
									if (careInitiator.get("id").equals(
											careReciever.get("id"))) {
										if (careInitiator.has("nickname")) {
											item.setCare_reciever_name(careInitiator
													.getString("nickname"));
										} else if (careInitiator
												.has("first_name")) {
											item.setCare_reciever_name(careInitiator
													.getString("first_name"));
										} else {
											item.setCare_reciever_name(careInitiator
													.getString("mobile"));
										}
										item.setReqMsg(item
												.getCare_reciever_name()
												+ " wants you to care for them");

									}
									if (!careInitiator.get("id").equals(
											careReciever.get("id"))
											&& !careInitiator
													.getString("id")
													.equals(careGiver.get("id"))) {
										if (careInitiator.has("nickname")) {
											item.setCare_reciever_name(careInitiator
													.getString("nickname"));
										} else if (careInitiator
												.has("first_name")) {
											item.setCare_reciever_name(careInitiator
													.getString("first_name"));
										} else {
											item.setCare_reciever_name(careInitiator
													.getString("mobile"));
										}
										item.setReqMsg(item
												.getCare_reciever_name()
												+ " wants you to care for"
												+ careReciever
														.getInt("nickname"));
									}
									// RequestModel item = new RequestModel();
									//
									item.setRequestID(careRequest
											.getString("id"));
									requestList.add(item);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							hidepDialog();
							Intent i = new Intent(Details.this,
									CircleNotificationActivity.class);
							Bundle bndlanimation = ActivityOptions
									.makeCustomAnimation(
											getApplicationContext(),
											R.anim.animation, R.anim.animation2)
									.toBundle();

							i.putParcelableArrayListExtra(
									"request",
									(ArrayList<? extends Parcelable>) requestList);
							startActivity(i, bndlanimation);
							finish();

						} else {
							Intent i = new Intent(Details.this,
									AddCareActivity.class);
							Bundle bndlanimation = ActivityOptions
									.makeCustomAnimation(
											getApplicationContext(),
											R.anim.animation, R.anim.animation2)
									.toBundle();
							startActivity(i, bndlanimation);
							finish();
						}

					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());

					}

				});

		AppController.getInstance().addToRequestQueue(req);

	}

	public void getUserInfo() {
		pDialog.setMessage("Fetching User Info");
		showpDialog();
		CustomRequest req = new CustomRequest(
				"http://54.69.183.186:1340/user/profile",
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject responseArray) {
						// TODO Auto-generated method stub
						Log.d("Response Array", " " + responseArray);
						hidepDialog();
						try {
							phone = responseArray.getString("mobile");
							userID = responseArray.getString("id");

							if (responseArray.has("gender")
									&& !responseArray.isNull("gender")) {
								if (responseArray.getString("gender").equals(
										"male")) {
									male = true;
								} else {
									male = false;
								}
								if (male)
									radioGroup.check(R.id.radioMale);
								else
									radioGroup.check(R.id.radioFemale);
								if (responseArray.has("yob")
										&& !(responseArray.isNull("yob"))) {
									yob = responseArray.getString("yob");
									if (!yob.equalsIgnoreCase(null)) {
										Log.d("YOB", yob);
										userYear.setText(yob);
										Calendar calendar = Calendar
												.getInstance();
										int year = calendar.get(Calendar.YEAR);
										userAge.setText(""
												+ (year - Integer.parseInt(yob)));
									}
								}
								if (responseArray.has("first_name")) {
									userName = responseArray
											.getString("first_name");
									detail.setText(userName);
									name.setText(userName);

								}

							}
							phone_detail.setText(phone);
							image_url = serverPath + userID + ".jpeg";
							imgLoader.DisplayImage(image_url,
									R.drawable.people, imgView);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						hidepDialog();
						VolleyLog.e("Error get contach Info: ",
								error.getMessage());
					}

				});

		AppController.getInstance().addToRequestQueue(req);

	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(Details.this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, Details.this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(Details.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	@SuppressWarnings("unchecked")
	private void registerInBackground() {
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;
					Log.d(TAG, msg);
					// You should send the registration ID to your server over
					// HTTP,
					// so it can use GCM/HTTP or CCS to send messages to your
					// app.
					// The request to your server should be authenticated if
					// your app
					// is using accounts.
					sendRegistrationIdToBackend();

					// For this demo: we don't need to send it because the
					// device
					// will send upstream messages to a server that echo back
					// the
					// message using the 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					// msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return null;
			}

		}.execute(null, null, null);
	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
	 * since the device sends upstream messages to a server that echoes back the
	 * message using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend() {
		// Your implementation here.
	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}
}