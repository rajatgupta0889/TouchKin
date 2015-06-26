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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.v7.app.ActionBarActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.touchKin.touchkinapp.adapter.MyAdapter.ViewHolder.IMyViewHolderClicks;
import com.touchKin.touchkinapp.adapter.SendTouchParentListAdapter;
import com.touchKin.touchkinapp.custom.HorizontalListView;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touckinapp.R;

public class SendTouchPreview extends ActionBarActivity implements
		OnClickListener, OnItemClickListener, IMyViewHolderClicks {

	ImageView previewImage;
	Bitmap bm;
	VideoView videoPreview;
	EditText sendmessage;
	Button sendButton;
	Handler handler;
	private SendTouchParentListAdapter imageAdapter;
	Button thumbnailplaybutton;
	int type;
	Uri previewFilePath;
	Boolean keepPrivate = false;
	public static int imagetype;
	HorizontalListView listview;
	RelativeLayout parentRelativeLayout;
	List<ParentListModel> list;
	Bitmap thumbnail, bitmap;
	SendTouchActivity sendtouch;
	ProgressDialog pDialog;
	String userId;

	// private ParentListModel selectedParent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_touch_preview_video);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		init();
		Intent intent = getIntent();
		Typeface latofont = Typeface.createFromAsset(getAssets(),
				"fonts/Lato-LightItalic.ttf");
		sendmessage.setTypeface(latofont);
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Sending the touch...");
		pDialog.setCancelable(false);
		userId = intent.getExtras().getString("userId");
		// adding toolbar
		// Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
		// TextView mTitle = (TextView)
		// toolbar.findViewById(R.id.toolbar_title);
		// mTitle.setText("");
		// toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		// toolbar.setNavigationOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// NavUtils.navigateUpFromSameTask(SendTouchPreview.this);
		//
		// }
		// });
		// toolbar.setNavigationIcon(R.drawable.back);
		// setSupportActionBar(toolbar);
		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// toolbar.setNavigationOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(SendTouchPreview.this,
		// SendTouchActivity.class);
		// startActivity(intent);
		//
		// }
		// });
		sendmessage.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					sendMedia(type);
					handled = true;
				}
				return handled;
			}
		});
		sendmessage.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				sendmessage.requestLayout();
				SendTouchPreview.this
						.getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);

				return false;
			}
		});

		if (intent != null) {

			Bundle bundle = intent.getExtras();
			if (bundle != null) {

				type = bundle.getInt("Media_Type");
				if (type == SendTouchActivity.MEDIA_TYPE_IMAGE) {
					previewImage.setVisibility(View.VISIBLE);
					videoPreview.setVisibility(View.INVISIBLE);
					previewFilePath = (Uri) bundle.get(MediaStore.EXTRA_OUTPUT);
					String tempPath = getPath(previewFilePath);
					// setPic(tempPath);
					BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
					bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
					previewImage.setImageBitmap(bm);
				} else {
					previewImage.setVisibility(View.VISIBLE);
					videoPreview.setVisibility(View.INVISIBLE);

					previewFilePath = (Uri) bundle.get(MediaStore.EXTRA_OUTPUT);

					thumbnail = ThumbnailUtils.createVideoThumbnail(
							getPath(previewFilePath), Thumbnails.MINI_KIND);

					// Matrix matrix = new Matrix();
					// Bitmap bmThumbnail = Bitmap.createBitmap(thumbnail, 0, 0,
					// thumbnail.getWidth(), thumbnail.getHeight(), matrix,
					// true);
					Bitmap extractthumbnail = ThumbnailUtils.extractThumbnail(
							thumbnail, 640, 400);
					// Log.d("thumbnail", thumbnail + " " + extractthumbnail);
					// MediaMetadataRetriever retriever = new
					// MediaMetadataRetriever();
					// try {
					// Log.i("img_url", getPath(previewFilePath));
					//
					// Uri uri = Uri.parse(getPath(previewFilePath));
					// String scheme = uri.getScheme();
					// Log.i("uri", "" + uri + " " + scheme);
					// retriever.setDataSource(getPath(previewFilePath));
					// bitmap = retriever.getFrameAtTime(20000000,
					// MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
					// Log.d("bitmappp", " " + bitmap.getWidth() + " "
					// + bitmap.getHeight());
					// } catch (IllegalArgumentException ex) {
					// // Assume this is a corrupt video file
					// Log.d("IllegalArgumentException", "" + ex.getMessage());
					// } catch (RuntimeException ex) {
					// // Assume this is a corrupt video file.
					// Log.d("RuntimeException", "" + ex.getMessage());
					// } finally {
					// try {
					// retriever.release();
					// } catch (RuntimeException ex) {
					// // Ignore failures while cleaning up.
					// Log.d("RuntimeException", "" + ex.getMessage());
					// }
					// }

					previewImage.setImageBitmap(extractthumbnail);

				}

				getParentList();
				listview.setOnItemClickListener(this);
				sendButton.setOnClickListener(this);

			}

		}
		thumbnailplaybutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SendTouchPreview.this,
						VideoFullScreen.class);
				intent.putExtra("thumbnail", thumbnail);
				intent.putExtra("videopath", previewFilePath);
				startActivity(intent);
			}
		});

	}

	// public String getPath(Uri uri, Activity activity) {
	// String[] projection = { MediaColumns.DATA };
	// Cursor cursor = activity
	// .managedQuery(uri, projection, null, null, null);
	// int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
	// cursor.moveToFirst();
	// return cursor.getString(column_index);
	// }

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

	private void init() {
		previewImage = (ImageView) findViewById(R.id.imagePreview);
		videoPreview = (VideoView) findViewById(R.id.videoPreview);
		sendButton = (Button) findViewById(R.id.sendbutton);
		sendmessage = (EditText) findViewById(R.id.phone_number_detail);
		listview = (HorizontalListView) findViewById(R.id.parentListView);
		thumbnailplaybutton = (Button) findViewById(R.id.play_btn);
		parentRelativeLayout = (RelativeLayout) findViewById(R.id.parentListLayoutDashboard);
		list = new ArrayList<ParentListModel>();
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
		// case R.id.back_button:
		// openDiscardDialog();
		// break;
		case R.id.yesButton:
			disCardVideo();
			break;
		// case R.id.noButton:
		// dismiss();
		// break;
		case R.id.sendbutton:
			sendMedia(type);
			break;
		default:
			break;
		}

	}

	private void disCardVideo() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, SendTouchActivity.class);
		startActivity(intent);
		finish();
	}

	public void sendMedia(int type) {
		new ImageUploadTask(this).execute();

	}

	public String getCheckedParentId() {
		String array = "";
		for (ParentListModel item : list) {
			if (array.isEmpty()) {
				array = item.getParentId();
			} else {
				array = array + "," + item.getParentId();
			}
		}

		return array;

	}

	public void getParentList() {
		list = new ArrayList<ParentListModel>();
		JsonArrayRequest req = new JsonArrayRequest(
				"http://54.69.183.186:1340/user/care-receivers",
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray responseArray) {
						// TODO Auto-generated method stub
						// Log.d("Response Array", " " + responseArray);

						if (responseArray.length() > 0) {
							for (int i = 0; i < responseArray.length(); i++) {
								try {
									JSONObject obj = responseArray
											.getJSONObject(i);
									Log.d("Response Array", " " + obj);
									ParentListModel item = new ParentListModel();
									item.setParentId(obj.getString("id"));
									if (obj.has("nickname")) {
										item.setParentName(obj
												.getString("nickname"));
									} else {
										item.setParentName("maa");
									}
									if (item.getParentId().equals(userId)) {
										item.setIsSelected(true);
									} else {
										item.setIsSelected(false);
									}
									// //
									// item.setParentUserId(obj.getJSONObject(
									// // "user").getString("id"));
									// if (i == 0) {
									// item.setIsSelected(true);
									// //selectedParent = item;
									// } else {
									// item.setIsSelected(false);
									// }
									list.add(item);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else {
							// setMenuTitle(null);
						}
						// list.add(new ParentListModel("", false, "", "", ""));
						imageAdapter = new SendTouchParentListAdapter(
								SendTouchPreview.this, list);
						// if (list.size() > 1) {
						// selectedParent = list.get(0);
						// // setMenuTitle(selectedParent);
						// }
						listview.setAdapter(imageAdapter);
						imageAdapter.notifyDataSetChanged();

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
				entity.addPart("message", new StringBody(sendmessage.getText()
						.toString()));
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
				// if (resEntity != null) {
				// System.out.println(EntityUtils.toString(resEntity));
				// }
				// if (resEntity != null) {
				// resEntity.consumeContent();
				// }
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
					hidepDialog();
					Log.d("Response", sResponse);
					JSONObject JResponse = new JSONObject(sResponse);
					Log.d("JSON", JResponse.toString());

					Toast.makeText(context, "your message sent",
							Toast.LENGTH_SHORT).show();
					finish();
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
				} else {
					hidepDialog();
					Toast.makeText(context, "Problem while sending a touch",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				// Toast.makeText(context, getString(R.string.app_name_empty),
				// Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		}

		@Override
		protected void onPreExecute() {
			showpDialog();
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

	@Override
	public void onItemTouch(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onImageTouch() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		ParentListModel item = list.get(position);
		// Toast.makeText(getApplicationContext(), "touched " + position,
		// Toast.LENGTH_LONG).show();

		if (item.getIsSelected()) {
			item.setIsSelected(false);
		} else {
			item.setIsSelected(true);
		}
		// listview.setAdapter(imageAdapter);
		imageAdapter.notifyDataSetChanged();
	}

	// @Override
	// public void onConfigurationChanged(Configuration newConfig) {
	// super.onConfigurationChanged(newConfig);
	//
	// if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	// getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	// WindowManager.LayoutParams.FLAG_FULLSCREEN);
	//
	// videoPreview.setDimensions(displayHeight, displayWidth);
	//
	// videoPreview.getHolder().setFixedSize(displayHeight, displayWidth);
	//
	// } else {
	// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
	// WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	//
	// videoPreview.setDimensions(displayWidth, smallHeight);
	// videoPreview.getHolder().setFixedSize(displayWidth, smallHeight);
	//
	// }
	// }
	@Override
	public void onBackPressed() {
		Log.d("CDA", "onBackPressed Called");
		Intent setIntent = new Intent(this, SendTouchActivity.class);
		startActivity(setIntent);
		finish();
	}

	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}
}
