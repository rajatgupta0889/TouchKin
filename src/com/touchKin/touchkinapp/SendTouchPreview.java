package com.touchKin.touchkinapp;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class SendTouchPreview extends Activity  {
	 static byte[] imageToShow=null;

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    if (imageToShow == null) {
	      Toast.makeText(this, "no image", Toast.LENGTH_LONG).show();
	      finish();
	    }
	    else {
	      ImageView iv=new ImageView(this);
	      BitmapFactory.Options opts=new BitmapFactory.Options();

	      opts.inPurgeable=true;
	      opts.inInputShareable=true;
	      opts.inMutable=false;
	      opts.inSampleSize=2;

	      iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
	      iv.setImageBitmap(BitmapFactory.decodeByteArray(imageToShow,
	                                                      0,
	                                                      imageToShow.length,
	                                                      opts));
	      imageToShow=null;

	      iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
	      setContentView(iv);
	    }
	  }

}
