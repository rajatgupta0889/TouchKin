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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aphidmobile.flip.FlipViewController;
import com.touchKin.touchkinapp.Interface.ButtonClickListener;
import com.touchKin.touchkinapp.adapter.FlipViewAdapter;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touchkinapp.model.TouchKinBookModel;
import com.touchKin.touckinapp.R;

public class TouchKinBookFragment extends Fragment implements
		ButtonClickListener {

	// List<TouchKinComments> commentList;
	// CommentListAdapter adapter;
	private FlipViewController flipView;
	private List<TouchKinBookModel> touchKinBook;
	FlipViewAdapter flipViewAdapter;
	FragmentTabHost host;
	TextView tv;

	String baseImageUrl = "https://s3-ap-southeast-1.amazonaws.com/touchkin-dev/kinbook-messages";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// host.getTabWidget().setVisibility(View.GONE);
		touchKinBook = new ArrayList<TouchKinBookModel>();

		getKinMessages();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.tochkin_book_fragment, null);

		init(v);

		Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar);
		TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
		tv = (TextView) v.findViewById(R.id.msg);
		mTitle.setText("Kinbook");
		host = ((DashBoardActivity) getActivity()).getTabHost();
		host.setVisibility(View.GONE);

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
		// touchKinBook.add(new TouchKinBookModel(
		// "ksnn_compilation_master_the_internet_512kb.mp4",
		// "Hi this is the video related to server handling", "12:00 Am",
		// "today", "", "Rajat", "0", ""));
		// touchKinBook
		// .add(new TouchKinBookModel(
		// "ksnn_compilation_master_the_internet.mp4",
		// "Hi this is the video related to server handling in .mov format",
		// "12:10 Am", "Monday", "", "Praf", "10", ""));
		// touchKinBook.add(new TouchKinBookModel("", "Hi this is the video",
		// "12:00 Am", "today", "", "Rajat", "0", ""));
		// touchKinBook.add(new TouchKinBookModel("", "Hi this is the video",
		// "12:00 Am", "today", "", "Prag", "0", ""));
		// touchKinBook.add(new TouchKinBookModel("", "Hi this is the video",
		// "12:00 Am", "today", "", "Rajat", "0", ""));

		// flipViewAdapter = new FlipViewAdapter(touchKinBook, getActivity());
		// flipView.setAdapter(flipViewAdapter);
		// flipView.invalidate();

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
						touchKinBook.add(new TouchKinBookModel());
						for (int i = 0; i < responseArray.length(); i++) {
							try {
								JSONObject obj = responseArray.getJSONObject(i);
								// // JSONArray comments = obj
								// // .getJSONArray("comments");
								// // commentList = getCommentList(comments);
								TouchKinBookModel item = new TouchKinBookModel();
								// item.setTouchKinComments(commentList);
								// if
								// (obj.getString("type").equals("image/jpeg"))
								// item.setVideoUrl("https://s3-ap-southeast-1.amazonaws.com/touchkin-dev/"
								// + obj.getString("id") + ".jpg");
								// else
								item.setVideoId(obj.getString("id"));
								if (obj.getString("type").equals("video/mp4"))
									item.setVideoUrl(obj.optString("hd_media"));
								// else
								// item.setVideoUrl("https://s3-ap-southeast-1.amazonaws.com/touchkin-dev/"
								// + obj.getString("id") + ".mov");
								item.setVideoText(obj.optString("message"));
								String time = obj.getString("createdAt");
								String[] date = time.split("T");
								String date1 = date[0];

								String[] time1 = date[1].split(":");

								String hours = time1[0] + ":" + time1[1];
								Log.d("time", time + " " + date1 + " " + hours);
								// Date date = new Date(time);
								item.setVideoDay(date1);
								item.setVideoDate(hours);
								// item.setVideoSenderName(obj.getJSONObject(
								// "owner").getString("first_name"));
								// item.setVideoText("");
								//

								item.setUserId(obj.getJSONObject("owner")
										.getString("id"));
								// item.setMessageId(obj.getString("id"));
								//
								touchKinBook.add(item);
							} catch (JSONException e) {
								// // TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						flipView.setAdapter(flipViewAdapter);
						flipViewAdapter.notifyDataSetChanged();
						if (touchKinBook.size() < 2) {
							tv.setVisibility(View.VISIBLE);
							tv.setText("You donot have any Kibook messages !!");
						}
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
		flipView = (FlipViewController) v.findViewById(R.id.flipView);
		flipViewAdapter = new FlipViewAdapter(touchKinBook, getActivity());
		flipViewAdapter.setCustomButtonListner(TouchKinBookFragment.this);

	}

	@Override
	public void onButtonClickListner(int position, String value,
			Boolean isAccept) {
		// TODO Auto-generated method stub
		if (!isAccept) {
			deleteKinBook(value, position);
		}

	}

	private void deleteKinBook(String value, final int position) {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();
		try {
			params.put("message_id", value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonObjectRequest req = new JsonObjectRequest(
				"http://54.69.183.186:1340/kinbook/message/delete/",
				params, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// Display the first 500 characters of the response
						// string.
						Log.d("kinBook", "Delete the kinbook");
						touchKinBook.remove(position);
						flipViewAdapter.notifyDataSetChanged();

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});

		AppController.getInstance().addToRequestQueue(req);

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
