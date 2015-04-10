package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.aphidmobile.flip.FlipViewController;
import com.touchKin.touchkinapp.adapter.CommentListAdapter;
import com.touchKin.touchkinapp.adapter.FlipViewAdapter;
import com.touchKin.touchkinapp.adapter.ImageAdapter;
import com.touchKin.touchkinapp.custom.HorizontalListView;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touchkinapp.model.TouchKinBookModel;
import com.touchKin.touchkinapp.model.TouchKinComments;
import com.touchKin.touckinapp.R;

public class TouchKinBookFragment extends Fragment implements
		OnItemClickListener, AnimationListener {

	ListView commentList;
	List<TouchKinComments> comments;
	CommentListAdapter adapter;
	private FlipViewController flipView;
	private List<TouchKinBookModel> touchKinBook;
	RelativeLayout parentRelativeLayout;
	private Menu menu;
	Animation animSlideUp, animSlideDown;
	HorizontalListView listview;
	List<ParentListModel> list;
	private ImageAdapter imageAdapter;
	FlipViewAdapter flipViewAdapter;
	TabHost host;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.tochkin_book_fragment, null);

		init(v);
		setHasOptionsMenu(true);
		comments.add(new TouchKinComments(
				"Mom, watch Mili and shaum playing with there freinds", "6:40",
				"Today", "Roy", ""));
		comments.add(new TouchKinComments(
				"Mom, watch Mili and shaum playing with there freinds", "6:40",
				"Today", "da", ""));
		comments.add(new TouchKinComments(
				"Mom, watch Mili and shaum playing with there freinds", "6:40",
				"Today", "fsdafasfasd", ""));
		comments.add(new TouchKinComments(
				"Mom, watch Mili and shaum playing with there freinds", "6:40",
				"Today", "fasdfasdfasdfasdfas", ""));
		touchKinBook.add(new TouchKinBookModel("", "Hi this is the video",
				"12:00 Am", "today", "", "Rajat", "0", "", comments));
		touchKinBook.add(new TouchKinBookModel("", "Hi this is the video",
				"12:00 Am", "today", "", "Rajat", "0", "", comments));
		touchKinBook.add(new TouchKinBookModel("", "Hi this is the video",
				"12:00 Am", "today", "", "Rajat", "0", "", comments));
		touchKinBook.add(new TouchKinBookModel("", "Hi this is the video",
				"12:00 Am", "today", "", "Rajat", "0", "", comments));
		touchKinBook.add(new TouchKinBookModel("", "Hi this is the video",
				"12:00 Am", "today", "", "Rajat", "0", "", comments));
		touchKinBook.add(new TouchKinBookModel("", "Hi this is the video",
				"12:00 Am", "today", "", "Rajat", "0", "", comments));
		flipViewAdapter = new FlipViewAdapter(touchKinBook, getActivity());
		flipView.setAdapter(flipViewAdapter);
		flipView.invalidate();
		// commentList.setAdapter(adapter);
		// Helper.getListViewSize(commentList);
		list = new ArrayList<ParentListModel>();
		list.add(new ParentListModel("", true, "Mom", "1"));
		list.add(new ParentListModel("", false, "Dad", "2"));
		list.add(new ParentListModel("", false, "Uncle", "3"));
		list.add(new ParentListModel("", false, "Aunt", "4"));
		list.add(new ParentListModel("", false, "GM", "5"));
		imageAdapter = new ImageAdapter(getActivity(), list);
		listview.setOnItemClickListener(this);
		listview.setAdapter(imageAdapter);
		animSlideUp.setAnimationListener(this);
		animSlideDown.setAnimationListener(this);
		return v;
	}

	void init(View v) {
		comments = new ArrayList<TouchKinComments>();
		// adapter = new CommentListAdapter(comments, getActivity());
		touchKinBook = new ArrayList<TouchKinBookModel>();

		flipView = (FlipViewController) v.findViewById(R.id.flipView);
		parentRelativeLayout = (RelativeLayout) v
				.findViewById(R.id.parentListLayout);
		animSlideUp = AnimationUtils.loadAnimation(getActivity(),
				R.anim.slide_up);
		animSlideDown = AnimationUtils.loadAnimation(getActivity(),
				R.anim.slide_down);
		listview = (HorizontalListView) v.findViewById(R.id.parentListView);
		host = (TabHost) getActivity().findViewById(android.R.id.tabhost);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Add your menu entries here
		super.onCreateOptionsMenu(menu, inflater);
		this.menu = menu;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.parentName:
			// Not implemented here
			toggleVissibility();
			return true;

		default:
			break;
		}

		return false;
	}

	private void toggleVissibility() {
		// TODO Auto-generated method stub

		if (parentRelativeLayout.getVisibility() == View.VISIBLE) {
			Log.d("Debug", "vissibility true");
			parentRelativeLayout.setVisibility(View.VISIBLE);
			parentRelativeLayout.startAnimation(animSlideUp);

		} else {

			parentRelativeLayout.setVisibility(View.VISIBLE);
			parentRelativeLayout.startAnimation(animSlideDown);
			Log.d("Debug", "vissibility false");
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		ParentListModel item = list.get(position);
		Log.d("Model", "" + item.getIsSelected());
		for (ParentListModel data : list) {
			if (data.equals(item)) {
				data.setIsSelected(true);
			} else {
				data.setIsSelected(false);
			}
		}
		MenuItem parentMenu = menu.findItem(R.id.parentName);
		parentMenu.setTitle(item.getParentName());
		listview.setAdapter(imageAdapter);
		toggleVissibility();
		host.setCurrentTab(0);

	}

	@Override
	public void onResume() {
		super.onResume();
		// flipView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		// flipView.onPause();
	}

	// @Override
	// public void onAnimationStart(Animation animation) {
	// // TODO Auto-generated method stub
	//
	// Log.d("Debug", "vissibility animation");
	// if (animation == animSlideUp) {
	// Log.d("Debug", "vissibility animation slideuo");
	// parentRelativeLayout.setVisibility(View.INVISIBLE);
	// }
	// }
	//
	// @Override
	// public void onAnimationEnd(Animation animation) {
	// // TODO Auto-generated method stub
	// if (animation == animSlideUp) {
	// Log.d("Debug", "vissibility animation slideup end");
	// parentRelativeLayout.setVisibility(View.INVISIBLE);
	// }
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation) {
	// // TODO Auto-generated method stub
	//
	// }

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		if (animation == animSlideUp) {
			parentRelativeLayout.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}
}
