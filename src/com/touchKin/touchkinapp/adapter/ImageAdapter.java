package com.touchKin.touchkinapp.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.touchKin.touchkinapp.custom.ImageLoader;
import com.touchKin.touchkinapp.custom.RoundedImageView;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touckinapp.R;

public class ImageAdapter extends BaseAdapter {
	Context mContext;
	LayoutInflater mLayoutInflater;

	List<ParentListModel> parentList;
	String serverPath = "https://s3-ap-southeast-1.amazonaws.com/touchkin-dev/avatars/";

	// int[] mResources = { R.drawable.mom, R.drawable.activity_bg,
	// R.drawable.mom, R.drawable.mom, R.drawable.mom };

	public ImageAdapter(Context context, List<ParentListModel> parentList) {
		mContext = context;
		this.parentList = parentList;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return parentList.size();
	}

	@Override
	public ParentListModel getItem(int position) {
		// TODO Auto-generated method stub
		return parentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		// if (position == parentList.size()) {
		// return position - 1;
		// }
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		convertView = mLayoutInflater.inflate(R.layout.image_item, null);
		RelativeLayout lay = (RelativeLayout)convertView.findViewById(R.id.rel_lay);
		TextView parentname = (TextView)convertView.findViewById(R.id.parentname);
		RoundedImageView imageView = (RoundedImageView) convertView
				.findViewById(R.id.parentImage);
		if (parentList.get(position).getIsSelected()) {
			lay.setBackgroundDrawable(mContext.getResources()
					.getDrawable(R.drawable.circular_image_selected));
		} else {
			lay.setBackgroundDrawable(mContext.getResources()
					.getDrawable(R.drawable.parent_image));
		}
		// imageView.setImageResource(R.drawable.mom);
		parentname.setText(parentList.get(position).getParentName());
		ImageLoader imageLoader = new ImageLoader(mContext);
		if (position + 1 != parentList.size()) {
			imageLoader.DisplayImage(serverPath
					+ getItem(position).getParentId() + ".jpeg",
					R.drawable.ic_user_image, imageView);
		} else {
			convertView = mLayoutInflater
					.inflate(R.layout.image_add_item, null);
		}
		return convertView;
	}
	// @Override
	// public boolean isViewFromObject(View view, Object object) {
	// return view == ((LinearLayout) object);
	// }
	//
	// @Override
	// public Object instantiateItem(ViewGroup container, int position) {
	// View itemView = mLayoutInflater.inflate(R.layout.image_item, container,
	// false);
	//
	// RoundedImageView imageView = (RoundedImageView) itemView
	// .findViewById(R.id.parentImage);
	// imageView.setImageResource(mResources[position]);
	//
	// container.addView(itemView);
	//
	// return itemView;
	// }
	//
	// @Override
	// public void destroyItem(ViewGroup container, int position, Object object)
	// {
	// container.removeView((LinearLayout) object);
	// }

}
