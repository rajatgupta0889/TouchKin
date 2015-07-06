package com.touchKin.touchkinapp.adapter;

import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.touchKin.touchkinapp.custom.ImageLoader;
import com.touchKin.touchkinapp.custom.RoundedImageView;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touckinapp.R;

public class MyDashbaordAdapter extends PagerAdapter {
	Context context;
	List<ParentListModel> parentList;
	LayoutInflater inflater;
	String serverPath = "https://s3-ap-southeast-1.amazonaws.com/touchkin-dev/avatars/";

	public MyDashbaordAdapter(Context context, List<ParentListModel> parentList) {
		this.parentList = parentList;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager) container).removeView((LinearLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub

		RoundedImageView imageView;
		ParentListModel parent = parentList.get(position);
		View view = inflater.inflate(R.layout.dashboard_touch_screen,
				container, false);
		imageView = (RoundedImageView) view.findViewById(R.id.profile_pic);
		ImageLoader imageLoader = new ImageLoader(context);
		String name = parent.getParentName();
		int resID = 0;
		if (!name.equalsIgnoreCase("")) {
			String cut = name.substring(0, 1).toLowerCase();
			resID = context.getResources().getIdentifier(cut, "drawable",
					context.getPackageName());
			Log.d("cut", cut + " " + resID);
		}
		imageLoader.DisplayImage(serverPath + parent.getParentId() + ".jpeg",
				resID, imageView);
		((ViewPager) container).addView(view);
		return view;

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return super.saveState();
	}

	public int getCount() {
		return parentList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == ((LinearLayout) arg1);

	}

}
