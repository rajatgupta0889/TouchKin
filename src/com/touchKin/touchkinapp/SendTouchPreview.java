package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.ryanharter.android.tooltips.ToolTip;
import com.ryanharter.android.tooltips.ToolTip.Builder;
import com.ryanharter.android.tooltips.ToolTipLayout;
import com.touchKin.touchkinapp.adapter.AddParentToSendTouchAdapter;
import com.touchKin.touchkinapp.model.ParentSendTouchModel;
import com.touchKin.touckinapp.R;

public class SendTouchPreview extends Activity implements OnClickListener {
	ImageView previewImage;
	Bitmap bm;
	VideoView videoPreview;
	Button backButton, addParentButton;
	ToolTipLayout tipContainer;

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
				int type = bundle.getInt("Media_Type");

				if (type == SendTouchActivity.MEDIA_TYPE_IMAGE) {
					previewImage.setVisibility(View.VISIBLE);
					videoPreview.setVisibility(View.INVISIBLE);

					String tempPath = getPath((Uri) bundle
							.get(MediaStore.EXTRA_OUTPUT));
					setPic(tempPath);
					// BitmapFactory.Options btmapOptions = new
					// BitmapFactory.Options();
					// bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
					// previewImage.setImageBitmap(bm);
				} else {
					previewImage.setVisibility(View.INVISIBLE);
					videoPreview.setVisibility(View.VISIBLE);

					videoPreview.setMediaController(new MediaController(this));
					videoPreview.setVideoURI((Uri) bundle
							.get(MediaStore.EXTRA_OUTPUT));
					videoPreview.start();
					videoPreview.requestFocus();
				}
			}
		}
	}

	private void init() {
		previewImage = (ImageView) findViewById(R.id.imagePreview);
		videoPreview = (VideoView) findViewById(R.id.videoPreview);
		backButton = (Button) findViewById(R.id.back_button);
		tipContainer = (ToolTipLayout) findViewById(R.id.tooltip_container);
		addParentButton = (Button) findViewById(R.id.addParentButton);
		addParentButton.setOnClickListener(this);
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
		matrix.postRotate(90);

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
		default:
			break;
		}

	}

	private void openAddParent() {
		// TODO Auto-generated method stub
		// tipContainer.dismiss();
		RelativeLayout customLayout = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.add_parent_dialog_layout, null);
		ListView list = (ListView) customLayout.findViewById(R.id.parentList);
		List<ParentSendTouchModel> parentList = new ArrayList<ParentSendTouchModel>();
		parentList.add(new ParentSendTouchModel("Mom", "", true));
		parentList.add(new ParentSendTouchModel("uncle", "", false));
		View header = (View) getLayoutInflater().inflate(R.layout.header_view,
				null);
		AddParentToSendTouchAdapter adapter = new AddParentToSendTouchAdapter(
				parentList, this);
		list.addHeaderView(header);
		list.setAdapter(adapter);

		ToolTip t = new Builder(SendTouchPreview.this).anchor(addParentButton)
				.gravity(Gravity.TOP) // The location of the view in relation
				.dismissOnTouch(true) // to the anchor (LEFT, RIGHT, TOP,
				.color(Color.WHITE) // The color of the pointer arrow
				.pointerSize(30) // The size of the pointer
				.contentView(customLayout) // The actual contents of the ToolTip
				.build();

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
		ToolTip t = new Builder(SendTouchPreview.this).anchor(backButton) // The
																			// view
																			// to
																			// which
																			// the
																			// ToolTip
																			// should
																			// be
																			// anchored
				.gravity(Gravity.BOTTOM) // The location of the view in relation
											// to the anchor (LEFT, RIGHT, TOP,
											// BOTTOM)
				.color(Color.WHITE) // The color of the pointer arrow
				.pointerSize(30) // The size of the pointer
				.contentView(customLayout) // The actual contents of the ToolTip
				.dismissOnTouch(true).build();

		tipContainer.addTooltip(t);

	}
}
