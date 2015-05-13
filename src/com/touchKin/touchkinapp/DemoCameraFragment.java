package com.touchKin.touchkinapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.commonsware.cwac.camera.CameraFragment;
import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.PictureTransaction;
import com.commonsware.cwac.camera.SimpleCameraHost;
import com.touchKin.touckinapp.R;

public class DemoCameraFragment extends CameraFragment {
	private static final String KEY_USE_FFC = "com.commonsware.cwac.camera.demo.USE_FFC";

	static DemoCameraFragment newInstance(boolean useFFC) {
		DemoCameraFragment f = new DemoCameraFragment();
		Bundle args = new Bundle();

		args.putBoolean(KEY_USE_FFC, useFFC);
		f.setArguments(args);

		return (f);
	}

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		SimpleCameraHost.Builder builder = new SimpleCameraHost.Builder(
				new DemoCameraHost(getActivity()));

		setHost(builder.useFullBleedPreview(false).build());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View cameraView = super.onCreateView(inflater, container,
				savedInstanceState);
		View results = inflater.inflate(R.layout.fragment, container, false);

		((ViewGroup) results.findViewById(R.id.camera)).addView(cameraView);
		return (results);
	}

	@Override
	public void onPause() {
		super.onPause();
		}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return (super.onOptionsItemSelected(item));
	}

	Contract getContract() {
		return ((Contract) getActivity());
	}

	void takeSimplePicture() {
		PictureTransaction xact = new PictureTransaction(getHost());
		takePicture(xact);
		
	}

	interface Contract {
		boolean isSingleShotMode();

		void setSingleShotMode(boolean mode);
	}
	class DemoCameraHost extends SimpleCameraHost {
		boolean supportsFaces = false;
		

		public DemoCameraHost(Context _ctxt) {
			super(_ctxt);
		}

		@Override
		public boolean useFrontFacingCamera() {
			if (getArguments() == null) {
				return (false);
			}

			return (getArguments().getBoolean(KEY_USE_FFC));
		}

		@Override
		public void saveImage(PictureTransaction xact, byte[] image) {
			super.saveImage(xact, image);
			
				SendTouchPreview.imageToShow = image;
				startActivity(new Intent(getActivity(), SendTouchPreview.class));
			
		}
@Override
protected String getVideoFilename() {
	// TODO Auto-generated method stub
	return "abc.mp4";
}
		@Override
		public void onCameraFail(CameraHost.FailureReason reason) {
			super.onCameraFail(reason);

			Toast.makeText(getActivity(),
					"Sorry, but you cannot use the camera now!",
					Toast.LENGTH_LONG).show();
		}
	}

}