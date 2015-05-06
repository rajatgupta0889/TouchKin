package com.touchKin.touchkinapp;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.touchKin.touchkinapp.custom.ImageLoader;
import com.touchKin.touchkinapp.custom.RoundedImageView;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touckinapp.R;

@SuppressWarnings("deprecation")
public class Details extends ActionBarActivity {

	Button next;
	TextView detail, phone_detail;
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
	String image_url;
	private ProgressDialog pDialog;
	private Toolbar toolbar;
	TextView mTitle;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
		init();
		if (getIntent() != null) {
			phone = getIntent().getExtras().getString("phoneNumber");
			userID = getIntent().getExtras().getString("id");
			if (getIntent().getExtras().getString("first_name") != null)
				userName = getIntent().getExtras().getString("first_name");
		}
		pDialog.setMessage("Updating info");
		pDialog.setCancelable(false);
		phone_detail.setText(phone);
		// Image url
		image_url = serverPath + userID + ".jpeg";
		mTitle.setText("Profile");
		// ImageLoader class instance
		imgLoader = new ImageLoader(getApplicationContext());
		new MyTask().execute(image_url);
		// whenever you want to load an image from url
		// call DisplayImage function
		// url - image url to load
		// loader - loader image, will be displayed before getting image
		// image - ImageView

		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!name.getText().toString().isEmpty()) {
					String userName = name.getText().toString();
					// if (name.isDirty())
					updateUser(userName);
					// else {
					// Intent i = new Intent(Details.this,
					// CircleNotificationActivity.class);
					// Bundle bndlanimation = ActivityOptions
					// .makeCustomAnimation(getApplicationContext(),
					// R.anim.animation, R.anim.animation2)
					// .toBundle();
					// i.putExtra("phoneNumber", phone);
					// i.putExtra("id", userID);
					// i.putExtra("first_name", userName);
					//
					// startActivity(i, bndlanimation);
					//
					// }
					// Intent i = new Intent(Details.this,
					// DashBoardActivity.class);
					// Bundle bndlanimation =
					// ActivityOptions.makeCustomAnimation(
					// getApplicationContext(), R.anim.animation,
					// R.anim.animation2).toBundle();
					// startActivity(i, bndlanimation);
				} else {
					Toast.makeText(Details.this, "PLease Add your Name",
							Toast.LENGTH_SHORT).show();
				}
			}

		});
		if (userName != null && !userName.isEmpty()) {
			detail.setText(userName);
			name.setText(userName);
		}

		detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				detail.setVisibility(View.GONE);
				name.setVisibility(View.VISIBLE);
				// name_detail = name.getText().toString();
				// detail.setText(name_detail);
			}
		});

		// name.setOnFocusChangeListener(new OnFocusChangeListener() {
		//
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// // TODO Auto-generated method stub
		// if (!hasFocus) {
		// name_detail = name.getText().toString();
		// name.setVisibility(View.GONE);
		// detail.setVisibility(View.VISIBLE);
		// detail.setText(name_detail);
		//
		// }
		// }
		// });

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

				Uri selectedImageUri = data.getData();

				String tempPath = getPath(selectedImageUri, this);
				BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
				Bitmap bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
				// Set the Image in ImageView after decoding the String
				previewFilePath = tempPath;
				imgView.setImageBitmap(bm);
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

	class ImageUploadTask extends AsyncTask<Void, Void, Void> {

		Context context;

		public ImageUploadTask(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
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

		// @Override
		// protected void onPostExecute(Void) {
		// try {
		//
		// if (sResponse != null) {
		// Log.d("Response", sResponse);
		// JSONObject JResponse = new JSONObject(sResponse);
		// Log.d("JSON", JResponse.toString());
		// // int success = JResponse.getInt("SUCCESS");
		// // String message = JResponse.getString("MESSAGE");
		// // if (success == 0) {
		// // // Toast.makeText(getApplicationContext(), message,
		// // // Toast.LENGTH_LONG).show();
		// // } else {
		// // // Toast.makeText(getApplicationContext(),
		// // // "Photo uploaded successfully",
		// // // Toast.LENGTH_SHORT).show();
		// // }
		// }
		// } catch (Exception e) {
		// // Toast.makeText(context, getString(R.string.app_name_empty),
		// // Toast.LENGTH_LONG).show();
		// Log.e(e.getClass().getName(), e.getMessage(), e);
		// }

	}

	public String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaColumns.DATA };
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

	private class MyTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			imgLoader.clearCache();
		}

		@Override
		protected Boolean doInBackground(String... params) {

			try {
				HttpURLConnection.setFollowRedirects(false);
				HttpURLConnection con = (HttpURLConnection) new URL(params[0])
						.openConnection();
				con.setRequestMethod("HEAD");
				System.out.println(con.getResponseCode());
				Boolean result = con.getResponseCode() == HttpURLConnection.HTTP_OK;
				con.disconnect();
				return (result);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				imgLoader.DisplayImage(image_url, R.drawable.add_people,
						imgView);
				// addImageView.setVisibility(View.INVISIBLE);
			}
		}
	}

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

	private void updateUser(final String name) {
		// TODO Auto-generated method stub
		showpDialog();
		JSONObject params = new JSONObject();
		try {
			params.put("first_name", name);

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
				"http://54.69.183.186:1340/user/complete-profile", params,
				new Response.Listener<JSONObject>() {
					@SuppressLint("NewApi")
					@Override
					public void onResponse(JSONObject response) {
						hidepDialog();
						Toast.makeText(Details.this, "PLease response",
								Toast.LENGTH_SHORT).show();
						Intent i = new Intent(Details.this,
								CircleNotificationActivity.class);
						Bundle bndlanimation = ActivityOptions
								.makeCustomAnimation(getApplicationContext(),
										R.anim.animation, R.anim.animation2)
								.toBundle();
						i.putExtra("phoneNumber", phone);
						i.putExtra("id", userID);

						i.putExtra("first_name", name);

						startActivity(i, bndlanimation);

						// Log.d(TAG, response.toString());
						// VolleyLog.v("Response:%n %s",
						// response.toString(4));

						finish();
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
}