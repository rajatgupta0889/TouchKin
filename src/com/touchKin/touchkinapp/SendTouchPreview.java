package com.touchKin.touchkinapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ryanharter.android.tooltips.ToolTip;
import com.ryanharter.android.tooltips.ToolTip.Builder;
import com.ryanharter.android.tooltips.ToolTipLayout;
import com.touchKin.touchkinapp.adapter.SendTouchParentListAdapter;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touckinapp.R;

public class SendTouchPreview extends Activity implements OnClickListener {
	ImageView previewImage;
	Bitmap bm;
	VideoView videoPreview;
	Button backButton, addParentButton, sendButton;
	CheckBox keepPrivateCB;
	ToolTipLayout tipContainer;
	int type;
	Uri previewFilePath;
	Boolean keepPrivate = false;
	List<ParentListModel> parentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_touch_preview);
		init();
		backButton.setOnClickListener(this);
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				getParentList();
				type = bundle.getInt("Media_Type");

				if (type == SendTouchActivity.MEDIA_TYPE_IMAGE) {
					previewImage.setVisibility(View.VISIBLE);
					videoPreview.setVisibility(View.INVISIBLE);
					previewFilePath = (Uri) bundle.get(MediaStore.EXTRA_OUTPUT);
					String tempPath = getPath(previewFilePath);
					setPic(tempPath);
					// BitmapFactory.Options btmapOptions = new
					// BitmapFactory.Options();
					// bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
					// previewImage.setImageBitmap(bm);
				} else {
					previewImage.setVisibility(View.INVISIBLE);
					videoPreview.setVisibility(View.VISIBLE);

					videoPreview.setMediaController(new MediaController(this));
					previewFilePath = (Uri) bundle.get(MediaStore.EXTRA_OUTPUT);

					videoPreview.setVideoURI(previewFilePath);
					videoPreview.start();
					videoPreview.requestFocus();
				}
			}
			sendButton.setOnClickListener(this);
			keepPrivateCB
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							keepPrivate = isChecked;

						}
					});
			// parentList.add(new ParentListModel("", true, "Mom", "1", "1"));
			// parentList.add(new ParentListModel("", false, "Dad", "2", "2"));
		}
	}

	private void init() {
		previewImage = (ImageView) findViewById(R.id.imagePreview);
		videoPreview = (VideoView) findViewById(R.id.videoPreview);
		backButton = (Button) findViewById(R.id.back_button);
		tipContainer = (ToolTipLayout) findViewById(R.id.tooltip_container);
		addParentButton = (Button) findViewById(R.id.addParentButton);
		addParentButton.setOnClickListener(this);
		keepPrivateCB = (CheckBox) findViewById(R.id.keepProvateCB);
		keepPrivateCB.setChecked(keepPrivate);
		sendButton = (Button) findViewById(R.id.sendButton);
		parentList = new ArrayList<ParentListModel>();
	}

	/**
	 * helper to retrieve the path of an image URI
	 */

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

	private void setPic(String file) {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		int targetW = previewImage.getWidth();
		int targetH = previewImage.getHeight();

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW / targetW, photoH / targetH);
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		final Bitmap bitmap = BitmapFactory.decodeFile(file, bmOptions);

		Matrix matrix = new Matrix();
		Camera.CameraInfo info;
		if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {

			matrix.postRotate(90);
		} else {
			// This is an undocumented although widely known feature
			matrix.postRotate(0);
		}

		Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, photoW,
				photoH, matrix, true);
		// TODO Auto-generated method stub

		/* Associate the Bitmap to the ImageView */
		previewImage.setImageBitmap(rotatedBitmap);
		previewImage.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_button:
			openDiscardDialog();
			break;
		case R.id.yesButton:
			disCardVideo();
			break;
		case R.id.noButton:
			dismiss();
			break;
		case R.id.addParentButton:
			openAddParent();
			break;
		case R.id.sendButton:
			sendMedia(type);
			break;
		default:
			break;
		}

	}

	private void openAddParent() {
		// TODO Auto-generated method stub
		// tipContainer.dismiss();
		ToolTip t = new Builder(SendTouchPreview.this)
				.anchor(addParentButton)
				.gravity(Gravity.TOP)
				// The location of the view in relation
				.dismissOnTouch(true)
				// to the anchor (LEFT, RIGHT, TOP,
				.color(Color.WHITE)
				// The color of the pointer arrow
				.pointerSize(30)
				// The size of the pointer
				.contentView(
						getLayoutInflater().inflate(
								R.layout.add_parent_dialog_layout, null)) // The
																			// actual
																			// contents
																			// of
																			// the
																			// ToolTip
				.build();

		LinearLayout customLayout = (LinearLayout) t.getView();
		ListView list = (ListView) customLayout.findViewById(R.id.parentList);

		View header = (View) getLayoutInflater().inflate(R.layout.header_view,
				null);
		SendTouchParentListAdapter adapter = new SendTouchParentListAdapter(
				this, parentList);
		list.addHeaderView(header);
		list.setAdapter(adapter);

		tipContainer.addTooltip(t);
	}

	private void dismiss() {
		// TODO Auto-generated method stub
		tipContainer.dismiss(true);
	}

	private void disCardVideo() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, SendTouchActivity.class);
		startActivity(intent);
		finish();
	}

	private void openDiscardDialog() {
		// TODO Auto-generated method stub
		RelativeLayout customLayout = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.discard_popup, null);
		tipContainer = (ToolTipLayout) findViewById(R.id.tooltip_container);

		// Create a content view however you'd like
		// ...
		TextView message = (TextView) customLayout.findViewById(R.id.message);
		Button noButton = (Button) customLayout.findViewById(R.id.noButton);
		Button yesButton = (Button) customLayout.findViewById(R.id.yesButton);
		noButton.setOnClickListener(this);
		yesButton.setOnClickListener(this);
		message.setText("Discard this video and Go Back");
		// Create a ToolTip using the Builder class
		ToolTip t = new Builder(SendTouchPreview.this).anchor(backButton)
				.gravity(Gravity.BOTTOM) // The location of the view in relation
											// to the anchor (LEFT, RIGHT, TOP,
											// BOTTOM)
				.color(Color.WHITE) // The color of the pointer arrow
				.pointerSize(30) // The size of the pointer
				.contentView(customLayout) // The actual contents of the ToolTip
				.dismissOnTouch(true).build();

		tipContainer.addTooltip(t);

	}

	public void sendMedia(int type) {
		new ImageUploadTask(this).execute();

	}

	public JSONArray getCheckedParentId() {
		JSONArray array = new JSONArray();
		for (ParentListModel item : parentList) {
			if (item.getIsSelected())
				array.put(item.getParentUserId());
		}
		if (array.length() < 1) {
			return null;
		}

		return array;

	}

	public void getParentList() {

		JsonArrayRequest req = new JsonArrayRequest(
				"http://54.69.183.186:1340/kin/seniors",
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray responseArray) {
						// TODO Auto-generated method stub
						Log.d("Response Array", " " + responseArray);
						for (int i = 0; i < responseArray.length(); i++) {
							try {
								JSONObject obj = responseArray.getJSONObject(i);
								ParentListModel item = new ParentListModel();

								item.setParentId(obj.getString("id"));
								item.setParentName(obj.getString("name"));
								item.setParentUserId(obj.getJSONObject("user")
										.getString("id"));
								if (i == 0) {
									item.setIsSelected(true);
								} else {
									item.setIsSelected(false);
								}
								parentList.add(item);

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());
						Toast.makeText(SendTouchPreview.this,
								error.getMessage(), Toast.LENGTH_SHORT).show();

					}

				});

		AppController.getInstance().addToRequestQueue(req);

	}

	class ImageUploadTask extends AsyncTask<Void, Void, String> {

		Context context;

		public ImageUploadTask(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected String doInBackground(Void... unsued) {
			try {

				File file = new File(getPath(previewFilePath));

				// this is storage overwritten on each iteration with bytes
				AppController.mHttpClient.getParams().setParameter(
						CoreProtocolPNames.PROTOCOL_VERSION,
						HttpVersion.HTTP_1_1);

				HttpClient httpClient = AppController.mHttpClient;

				HttpPost httpPost = new HttpPost(
						"http://54.69.183.186:1340/kinbook/message/add");

				MultipartEntity entity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				entity.addPart("shared_with", new StringBody(
						getCheckedParentId().toString()));
				ContentBody cbFile = new FileBody(
						file,
						ContentType.create(getMimeType(file.getAbsolutePath())),
						file.getName());

				entity.addPart("media", cbFile);

				// entity.addPart("media", fileBody);
				Log.d("ParentId", getCheckedParentId().toString());

				// entity.addPart("photoCaption", new
				// StringBody(caption.getText()
				// .toString()));
				httpPost.setEntity(entity);
				Log.d("HttpPost", httpPost.getEntity() + "");
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity resEntity = response.getEntity();

				System.out.println(response.getStatusLine());
				if (resEntity != null) {
					System.out.println(EntityUtils.toString(resEntity));
				}
				if (resEntity != null) {
					resEntity.consumeContent();
				}
				return EntityUtils.toString(resEntity);
			} catch (Exception e) {

				Log.e(e.getClass().getName(), e.getMessage(), e);
				return null;
			}

			// (null);
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {

				if (sResponse != null) {
					Log.d("Response", sResponse);
					JSONObject JResponse = new JSONObject(sResponse);
					Log.d("JSON", JResponse.toString());
					// int success = JResponse.getInt("SUCCESS");
					// String message = JResponse.getString("MESSAGE");
					// if (success == 0) {
					// // Toast.makeText(getApplicationContext(), message,
					// // Toast.LENGTH_LONG).show();
					// } else {
					// // Toast.makeText(getApplicationContext(),
					// // "Photo uploaded successfully",
					// // Toast.LENGTH_SHORT).show();
					// }
				}
			} catch (Exception e) {
				// Toast.makeText(context, getString(R.string.app_name_empty),
				// Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
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
