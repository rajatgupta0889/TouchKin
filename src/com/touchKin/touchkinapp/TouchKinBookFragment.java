package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.aphidmobile.flip.FlipViewController;
import com.touchKin.touchkinapp.adapter.CommentListAdapter;
import com.touchKin.touchkinapp.adapter.FlipViewAdapter;
import com.touchKin.touchkinapp.custom.Helper;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touchkinapp.model.TouchKinBookModel;
import com.touchKin.touchkinapp.model.TouchKinComments;
import com.touchKin.touckinapp.R;

public class TouchKinBookFragment extends Fragment {

	// List<TouchKinComments> commentList;
	// CommentListAdapter adapter;
	private FlipViewController flipView;
	private List<TouchKinBookModel> touchKinBook;
	FlipViewAdapter flipViewAdapter;
	FragmentTabHost host;

	String baseImageUrl = "https://s3-ap-southeast-1.amazonaws.com/touchkin-dev/";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// host.getTabWidget().setVisibility(View.GONE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.tochkin_book_fragment, null);

		init(v);
		setHasOptionsMenu(true);
		// commentList.add(new TouchKinComments(
		// "Mom, watch Mili and shaum playing with there freinds", "6:40",
		// "Today", "Roy", ""));
		// commentList.add(new TouchKinComments(
		// "Mom, watch Mili and shaum playing with there freinds", "6:40",
		// "Today", "da", ""));
		// commentList.add(new TouchKinComments(
		// "Mom, watch Mili and shaum playing with there freinds", "6:40",
		// "Today", "fsdafasfasd", ""));
		// commentList.add(new TouchKinComments(
		// "Mom, watch Mili and shaum playing with there freinds", "6:40",
		// "Today", "fasdfasdfasdfasdfas", ""));
		// touchKinBook.add(new TouchKinBookModel("", "Hi this is the video",
		// "12:00 Am", "today", "", "Rajat", "0", "", "", commentList));
		touchKinBook.add(new TouchKinBookModel(
				"ksnn_compilation_master_the_internet_512kb.mp4",
				"Hi this is the video related to server handling", "12:00 Am",
				"today", "", "Rajat", "0", ""));
		touchKinBook
				.add(new TouchKinBookModel(
						"ksnn_compilation_master_the_internet.mp4",
						"Hi this is the video related to server handling in .mov format",
						"12:10 Am", "Monday", "", "Praf", "10", ""));
		touchKinBook.add(new TouchKinBookModel("", "Hi this is the video",
				"12:00 Am", "today", "", "Rajat", "0", ""));
		// touchKinBook.add(new TouchKinBookModel("", "Hi this is the video",
		// "12:00 Am", "today", "", "Rajat", "0", "" ));
		// touchKinBook.add(new TouchKinBookModel("", "Hi this is the video",
		// "12:00 Am", "today", "", "Rajat", "0", "" ));

		flipViewAdapter = new FlipViewAdapter(touchKinBook, getActivity());
		flipView.setAdapter(flipViewAdapter);
		flipView.invalidate();
		Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar);
		TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
		mTitle.setText("Kinbook");
		host = ((DashBoardActivity) getActivity()).getTabHost();
		host.setVisibility(View.GONE);

		getKinMessages();
		return v;
	}

	private void getKinMessages() {
		// TODO Auto-generated method stub
		JsonArrayRequest req = new JsonArrayRequest(
				"http://54.69.183.186:1340/kinbook/messages",
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray responseArray) {
						// TODO Auto-generated method stub
						Log.d("Response Array", " " + responseArray);
//						for (int i = 0; i < responseArray.length(); i++) {
//							try {
//								JSONObject obj = responseArray.getJSONObject(i);
//								JSONArray comments = obj
//										.getJSONArray("comments");
//								// commentList = getCommentList(comments);
//								TouchKinBookModel item = new TouchKinBookModel();
//								// item.setTouchKinComments(commentList);
//								if (obj.getString("type").equals("image/jpeg"))
//									item.setVideoUrl("https://s3-ap-southeast-1.amazonaws.com/touchkin-dev/"
//											+ obj.getString("id") + ".jpg");
//								else if (obj.getString("type").equals(
//										"video/mp4"))
//									item.setVideoUrl("https://s3-ap-southeast-1.amazonaws.com/touchkin-dev/"
//											+ obj.getString("id") + ".mp4");
//								else
//									item.setVideoUrl("https://s3-ap-southeast-1.amazonaws.com/touchkin-dev/"
//											+ obj.getString("id") + ".mov");
//								String time = obj.getString("createdAt");
//								// Date date = new Date(time);
//								item.setVideoDay("");
//								item.setVideoDate(":");
//								item.setVideoSenderName(obj.getJSONObject(
//										"owner").getString("mobile"));
//								item.setVideoText("");
//
//								item.setUserId(obj.getJSONObject("owner")
//										.getString("id"));
//								item.setMessageId(obj.getString("id"));
//
//								touchKinBook.add(item);
//							} catch (JSONException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//						flipViewAdapter = new FlipViewAdapter(touchKinBook,
//								getActivity());
//						flipView.setAdapter(flipViewAdapter);
//						flipView.invalidate();
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());
						Toast.makeText(getActivity(), error.getMessage(),
								Toast.LENGTH_SHORT).show();

					}

				});

		AppController.getInstance().addToRequestQueue(req);

	}

	void init(View v) {
		// commentList = new ArrayList<TouchKinComments>();
		// adapter = new CommentListAdapter(commentList, getActivity());
		touchKinBook = new ArrayList<TouchKinBookModel>();
		flipView = (FlipViewController) v.findViewById(R.id.flipView);

	}

	// private List<TouchKinComments> getCommentList(JSONArray comments) {
	// List<TouchKinComments> list = new ArrayList<TouchKinComments>();
	// try {
	// for (int i = 0; i < comments.length(); i++) {
	//
	// JSONObject comment = comments.getJSONObject(i);
	// TouchKinComments item = new TouchKinComments();
	// item.setCommentText(comment.getString("text"));
	// String time = comment.getString("createdAt");
	// // Date date = new Date(time);
	// item.setCommentDay("");
	// item.setCommentTime("");
	// item.setUserImageUrl("");
	// item.setUserName("User");
	// list.add(item);
	// }
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// // TODO Auto-generated method stub
	// return list;
	// }

}
