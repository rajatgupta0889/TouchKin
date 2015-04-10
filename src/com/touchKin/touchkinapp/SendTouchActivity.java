package com.touchKin.touchkinapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.media.CamcorderProfile;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ryanharter.android.tooltips.ToolTip;
import com.ryanharter.android.tooltips.ToolTip.Builder;
import com.ryanharter.android.tooltips.ToolTipLayout;
import com.touchKin.touchkinapp.custom.AlbumStorageDirFactory;
import com.touchKin.touchkinapp.custom.BaseAlbumDirFactory;
import com.touchKin.touchkinapp.custom.CameraPreview;
import com.touchKin.touchkinapp.custom.FroyoAlbumDirFactory;
import com.touchKin.touckinapp.R;

@SuppressWarnings("deprecation")
public class SendTouchActivity extends Activity implements OnClickListener {
	private boolean cameraFront = false;
	boolean recording = false;
	public static int count = 0;
	private Camera mCamera;
	private CameraPreview mPreview;
	private MediaRecorder mediaRecorder;
	private Button switchCamera, imageCapture, videoMode, imageMode,
			menuButton;
	RelativeLayout menuLayout;
	private ToggleButton videoCapture;
	private Context myContext;
	private FrameLayout cameraPreview;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private static AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	File outFile;
	private int cameraid;
	String mCurrentPhotoPath;
	private long startTime = 0L;
	private Handler customHandler = new Handler();
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	String videoTime;
	private Button backButton;
	ToolTipLayout tipContainer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_touch);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		myContext = this;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
		initialize();
	}

	public void initialize() {
		cameraPreview = (FrameLayout) findViewById(R.id.camera_preview);
		mPreview = new CameraPreview(myContext, mCamera);
		cameraPreview.addView(mPreview);
		videoCapture = (ToggleButton) findViewById(R.id.video_capture_button);
		videoCapture.setOnClickListener(captrureListener);
		switchCamera = (Button) findViewById(R.id.change_camera_button);
		switchCamera.setOnClickListener(switchCameraListener);
		imageCapture = (Button) findViewById(R.id.take_picture_button);
		imageCapture.setOnClickListener(picturelistener);
		videoMode = (Button) findViewById(R.id.video_mode_button);
		videoMode.setOnClickListener(this);
		imageMode = (Button) findViewById(R.id.image_mode_button);
		imageMode.setOnClickListener(this);
		menuButton = (Button) findViewById(R.id.menuButton);
		menuButton.setOnClickListener(this);
		menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
		backButton = (Button) findViewById(R.id.back_button);
		backButton.setOnClickListener(this);
		tipContainer = (ToolTipLayout) findViewById(R.id.tooltip_container);

	}

	public void onResume() {
		super.onResume();
		if (!hasCamera(myContext)) {
			Toast toast = Toast.makeText(myContext,
					"Sorry, your phone does not have a camera!",
					Toast.LENGTH_LONG);
			toast.show();
			finish();
		}
		if (mCamera == null) {
			// if the front facing camera does not exist
			if (findFrontFacingCamera() < 0) {
				Toast.makeText(this, "No front facing camera found.",
						Toast.LENGTH_LONG).show();
				switchCamera.setVisibility(View.GONE);
			}
			mCamera = Camera.open(findBackFacingCamera());
			mPreview.refreshCamera(mCamera, cameraid);
		}
	}

	private boolean hasCamera(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseCamera();
	}

	OnClickListener switchCameraListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!recording) {
				int camerasNumber = Camera.getNumberOfCameras();
				if (camerasNumber > 1) {
					releaseCamera();
					chooseCamera();
				} else {
					Toast toast = Toast.makeText(myContext,
							"Sorry, your phone has only one camera!",
							Toast.LENGTH_LONG);
					toast.show();
				}
			}
		}
	};

	public void chooseCamera() {
		if (cameraFront) {
			int cameraId = findBackFacingCamera();
			if (cameraId >= 0) {
				mCamera = Camera.open(cameraId);
				mPreview.refreshCamera(mCamera, cameraid);
			}
		} else {
			int cameraId = findFrontFacingCamera();
			if (cameraId >= 0) {
				mCamera = Camera.open(cameraId);
				mPreview.refreshCamera(mCamera, cameraid);
			}
		}
	}

	private int findFrontFacingCamera() {
		int cameraId = -1;
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				cameraId = i;
				cameraid = cameraId;
				cameraFront = true;
				break;
			}
		}
		return cameraid;
	}

	private int findBackFacingCamera() {
		int cameraId = -1;
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
				cameraId = i;
				cameraid = cameraId;
				cameraFront = false;
				break;
			}
		}
		return cameraid;
	}

	OnClickListener picturelistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mCamera.takePicture(null, null, mPicture);
		}
	};

	private PictureCallback mPicture = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
			if (pictureFile == null) {
				return;
			}
			try {
				mCurrentPhotoPath = pictureFile.getAbsolutePath();

				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();

				// ByteArrayOutputStream stream = new ByteArrayOutputStream();
				// rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
				// stream);
				// byte[] byteArray = stream.toByteArray();
				// fos = new FileOutputStream(pictureFile);
				// fos.write(byteArray);
				// fos.close();
				releaseCamera();
				galleryAddPic();
				startPreview(MEDIA_TYPE_IMAGE, pictureFile);
				// mCamera = Camera.open(cameraid);
				// mPreview.refreshCamera(mCamera, cameraid);
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		}

	};

	@SuppressLint("SimpleDateFormat")
	private File getOutputMediaFile(int type) {
		File mediaStorageDir = getAlbumDir();
		Log.d("Directory", mediaStorageDir.toString());
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("TouchKin", "failed to create directory");
				return null;
			}
		}
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}
		return mediaFile;
	}

	private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			storageDir = mAlbumStorageDirFactory
					.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(getString(R.string.app_name),
					"External storage is not mounted READ/WRITE.");
		}
		Log.d("StorageDir", storageDir.toString());
		return storageDir;
	}

	private String getAlbumName() {
		return getString(R.string.album_name);
	}

	OnClickListener captrureListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (recording) {
				mediaRecorder.stop();
				releaseMediaRecorder();
				timeSwapBuff += timeInMilliseconds;
				customHandler.removeCallbacks(updateTimerThread);

				Toast.makeText(SendTouchActivity.this,
						"Video captured!" + videoTime, Toast.LENGTH_LONG)
						.show();
				recording = false;
				galleryAddPic();
				startPreview(MEDIA_TYPE_VIDEO, outFile);

			} else {
				if (!prepareMediaRecorder()) {
					Toast.makeText(SendTouchActivity.this,
							"Fail in prepareMediaRecorder()!\n - Ended -",
							Toast.LENGTH_LONG).show();
					finish();
				}
				runOnUiThread(new Runnable() {
					public void run() {
						try {
							mediaRecorder.start();
							startTime = SystemClock.uptimeMillis();
							customHandler.postDelayed(updateTimerThread, 0);

						} catch (final Exception ex) {
						}
					}
				});
				recording = true;
			}
		}
	};

	private boolean prepareMediaRecorder() {
		count++;
		mediaRecorder = new MediaRecorder();
		mCamera.unlock();
		mediaRecorder.setCamera(mCamera);
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mediaRecorder.setProfile(CamcorderProfile
				.get(CamcorderProfile.QUALITY_HIGH));
		outFile = getOutputMediaFile(MEDIA_TYPE_VIDEO);
		mCurrentPhotoPath = outFile.getAbsolutePath();
		mediaRecorder.setOutputFile(outFile.toString());
		mediaRecorder.setMaxDuration(60000); // Set max duration 60 sec.
		mediaRecorder.setMaxFileSize(50000000); // Set max file size 50M
		if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
			mediaRecorder.setOrientationHint(90);
		} else {
			mediaRecorder.setOrientationHint(0);
		}

		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			releaseMediaRecorder();
			return false;
		}
		return true;
	}

	private void releaseMediaRecorder() {
		if (mediaRecorder != null) {
			mediaRecorder.reset();
			mediaRecorder.release();
			mediaRecorder = null;
			mCamera.lock();
		}
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.menuButton:
			openMenu();
			break;
		case R.id.video_mode_button:
			startVideoMode();
			break;
		case R.id.image_mode_button:
			startImageMode();
			break;
		case R.id.back_button:
			openDialog();
			break;
		case R.id.yesButton:
			goBack();
			break;
		case R.id.noButton:
			dismiss();
			break;
		default:
			break;
		}

	}

	private void dismiss() {
		// TODO Auto-generated method stub
		tipContainer.dismiss(true);
	}

	private void goBack() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, DashBoardActivity.class);
		startActivity(intent);
		finish();
	}

	private void openDialog() {
		// TODO Auto-generated method stub
		RelativeLayout customLayout = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.discard_popup, null);

		// Create a content view however you'd like
		// ...
		TextView message = (TextView) customLayout.findViewById(R.id.message);
		message.setText("Go Back");
		Button noButton = (Button) customLayout.findViewById(R.id.noButton);
		Button yesButton = (Button) customLayout.findViewById(R.id.yesButton);
		noButton.setOnClickListener(this);
		yesButton.setOnClickListener(this);
		// Create a ToolTip using the Builder class
		ToolTip t = new Builder(SendTouchActivity.this).anchor(backButton)
				.gravity(Gravity.BOTTOM) // The location of the view in relation
				.dismissOnTouch(false) // to the anchor (LEFT, RIGHT, TOP,
				// BOTTOM)
				.color(Color.WHITE) // The color of the pointer arrow
				.pointerSize(30) // The size of the pointer
				.contentView(customLayout) // The actual contents of the ToolTip
				.build();

		tipContainer.addTooltip(t);
	}

	private void startImageMode() {
		// TODO Auto-generated method stub
		menuLayout.setVisibility(View.INVISIBLE);
		menuButton.setVisibility(View.VISIBLE);
		imageCapture.setVisibility(View.VISIBLE);
	}

	private void startVideoMode() {
		// TODO Auto-generated method stub
		menuLayout.setVisibility(View.INVISIBLE);
		menuButton.setVisibility(View.VISIBLE);
		videoCapture.setVisibility(View.VISIBLE);
	}

	private void openMenu() {
		// TODO Auto-generated method stub
		tipContainer.dismiss(true);
		videoCapture.setVisibility(View.INVISIBLE);
		menuButton.setVisibility(View.INVISIBLE);
		menuLayout.setVisibility(View.VISIBLE);
		imageCapture.setVisibility(View.INVISIBLE);
	}

	private void startPreview(int mediaTypeImage, File file) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getApplicationContext(),
				SendTouchPreview.class);
		intent.putExtra("Media_Type", mediaTypeImage);
		Log.d("Extra", Uri.fromFile(file).toString());

		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivity(intent);
		finish();
	}

	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent(
				"android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	private Runnable updateTimerThread = new Runnable() {

		public void run() {

			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

			updatedTime = timeSwapBuff + timeInMilliseconds;

			int secs = (int) (updatedTime / 1000);

			int mins = secs / 60;

			secs = secs % 60;

			int milliseconds = (int) (updatedTime % 1000);
			videoTime = "" + mins + ":" + String.format("%02d", secs) + ":"
					+ String.format("%03d", milliseconds);

			customHandler.postDelayed(this, 0);

		}

	};

	public int getCameraPhotoOrientation(Context context, Uri imageUri,
			String imagePath) {
		int rotate = 0;
		try {
			context.getContentResolver().notifyChange(imageUri, null);
			File imageFile = new File(imagePath);

			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			}

			Log.i("RotateImage", "Exif orientation: " + orientation);
			Log.i("RotateImage", "Rotate value: " + rotate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rotate;
	}

	public static int setCameraDisplayOrientation(Activity activity) {

		// android.hardware.Camera.CameraInfo info = new
		// android.hardware.Camera.CameraInfo();

		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}
		return degrees;
		// int result;
		// if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
		// result = (info.orientation + degrees) % 360;
		// result = (360 - result) % 360; // compensate the mirror
		// } else { // back-facing
		// result = (info.orientation - degrees + 360) % 360;
		// }

	}

}
