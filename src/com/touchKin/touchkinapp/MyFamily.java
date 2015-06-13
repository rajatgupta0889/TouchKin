package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.touchKin.touchkinapp.Interface.ButtonClickListener;
import com.touchKin.touchkinapp.adapter.ExpandableListAdapter;
import com.touchKin.touchkinapp.custom.CustomRequest;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touchkinapp.model.ExpandableListGroupItem;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touchkinapp.model.RequestModel;
import com.touchKin.touckinapp.R;

public class MyFamily extends AppCompatActivity implements OnClickListener,
		ButtonClickListener {
	ExpandableListAdapter adapter;
	HashMap<String, ArrayList<ParentListModel>> careGiver;
	ArrayList<RequestModel> requests;
	ArrayList<ExpandableListGroupItem> CareReciever;
	ArrayList<ExpandableListGroupItem> pendingReq;
	ExpandableListView expandListView;
	List<String> item;
	LinearLayout footerView;
	ExpandableListGroupItem me;
	JSONObject mySelf;
	private Toolbar toolbar;
	TextView mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_family);
		init();
		mTitle.setText("My Family");
		SharedPreferences userPref = getApplicationContext()
				.getSharedPreferences("userPref", 0);

		String user = userPref.getString("user", null);
		getConnectionRequest();
		fetchMyFamily();
		try {
			mySelf = new JSONObject(user);
			CareReciever.add(new ExpandableListGroupItem(
					mySelf.getString("id"), mySelf.getString("first_name"), "",
					""));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// parents.add(new ParentListModel("", false, "", "", ""));
		// parents.add(new ParentListModel("", false, "", "", ""));
		// parents.add(new ParentListModel("", false, "", "", ""));
		// CareReciever.add(new ExpandableListGroupItem("1", "", "", ""));
		// CareReciever.add(new ExpandableListGroupItem("2", "", "", ""));
		// careGiver.put(CareReciever.get(0).getUserId(), parents);
		// careGiver.put(CareReciever.get(1).getUserId(), parents);
		// careGiver.put(CareReciever.get(2).getUserId(), parents);
		// requests.add(new RequestModel("", "", "", ""));
		// requests.add(new RequestModel("", "", "", ""));

		expandListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				if (groupPosition > 0 && groupPosition < CareReciever.size()) {
					ExpandableListGroupItem item = CareReciever
							.get(groupPosition);
					fetchMyCRFamily(item.getUserId(), groupPosition);
				}
				return false;
			}
		});
		pendingReq.add(new ExpandableListGroupItem());
		pendingReq.add(new ExpandableListGroupItem());
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerView = (LinearLayout) inflater.inflate(
				R.layout.expand_list_footer, null);
		expandListView.addFooterView(footerView);
		footerView.setOnClickListener(this);
		adapter = new ExpandableListAdapter(MyFamily.this);
		adapter.setButtonListener(this);
		expandListView.setAdapter(adapter);
	}

	private void getConnectionRequest() {
		// TODO Auto-generated method stub
		JsonArrayRequest req = new JsonArrayRequest(
				"http://54.69.183.186:1340/user/connection-requests",
				new Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray responseArray) {
						// TODO Auto-generated method stub
						Log.d("Response Array", " " + responseArray);
						if (responseArray.length() > 0) {
							for (int i = 0; i < responseArray.length(); i++) {
								try {
									RequestModel item = new RequestModel();
									JSONObject careRequest = responseArray
											.getJSONObject(i);
									JSONObject careInitiator = careRequest
											.getJSONObject("initiator");
									JSONObject careGiver = careRequest
											.getJSONObject("care_giver");
									JSONObject careReciever = careRequest
											.getJSONObject("care_receiver");
									if (careInitiator.getString("id").equals(
											careGiver.get("id"))) {
										if (careInitiator.has("nickname")) {
											item.setCare_reciever_name(careInitiator
													.getString("nickname"));
										} else if (careInitiator
												.has("first_name")) {
											item.setCare_reciever_name(careInitiator
													.getString("first_name"));
										} else {
											item.setCare_reciever_name(careInitiator
													.getString("mobile"));
										}
										item.setReqMsg(item
												.getCare_reciever_name()
												+ " wants to care for you");
									}
									if (careInitiator.get("id").equals(
											careReciever.get("id"))) {
										if (careInitiator.has("nickname")) {
											item.setCare_reciever_name(careInitiator
													.getString("nickname"));
										} else if (careInitiator
												.has("first_name")) {
											item.setCare_reciever_name(careInitiator
													.getString("first_name"));
										} else {
											item.setCare_reciever_name(careInitiator
													.getString("mobile"));
										}
										item.setReqMsg(item
												.getCare_reciever_name()
												+ " wants you to care for them");

									}
									if (!careInitiator.get("id").equals(
											careReciever.get("id"))
											&& !careInitiator
													.getString("id")
													.equals(careGiver.get("id"))) {
										if (careInitiator.has("nickname")) {
											item.setCare_reciever_name(careInitiator
													.getString("nickname"));
										} else if (careInitiator
												.has("first_name")) {
											item.setCare_reciever_name(careInitiator
													.getString("first_name"));
										} else {
											item.setCare_reciever_name(careInitiator
													.getString("mobile"));
										}
										item.setReqMsg(item
												.getCare_reciever_name()
												+ " wants you to care for"
												+ careReciever
														.getInt("nickname"));
									}
									// RequestModel item = new RequestModel();
									//
									item.setRequestID(careRequest
											.getString("id"));
									requests.add(item);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}
						CareReciever.get(0).setReqCount(
								responseArray.length() + "");

						adapter.notifyDataSetChanged();
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());

					}

				});

		AppController.getInstance().addToRequestQueue(req);

	}

	private void fetchMyFamily() {
		// TODO Auto-generated method stub
		CustomRequest req = new CustomRequest(
				"http://54.69.183.186:1340/user/family",
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject responseObject) {
						// TODO Auto-generated method stub
						Log.d("Response Array", " " + responseObject);
						try {
							JSONArray careGivers = responseObject
									.getJSONArray("care_givers");
							ArrayList<ParentListModel> parents = new ArrayList<ParentListModel>();

							JSONArray careRecievers = responseObject
									.getJSONArray("care_receivers");
							int crCount = careRecievers.length();
							for (int i = 0; i < crCount; i++) {
								JSONObject cr = careRecievers.getJSONObject(i);
								ExpandableListGroupItem item = new ExpandableListGroupItem();
								item.setUserId(cr.getString("id"));
								item.setUserName(cr.optString("first_name"));
								CareReciever.add(item);
							}
							int cgCount = careGivers.length();
							for (int i = 0; i < cgCount; i++) {
								JSONObject cg = careGivers.getJSONObject(i);
								ParentListModel item = new ParentListModel();
								item.setParentId(cg.getString("id"));
								item.setParentName(cg.optString("first_name"));
								parents.add(item);
							}
							CareReciever.get(0).setKinCount(cgCount + "");
							Log.d("Care reciever Length",
									"" + CareReciever.size());
							careGiver.put(CareReciever.get(0).getUserId(),
									parents);
							adapter.setupTrips(careGiver, requests,
									CareReciever, pendingReq);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());
						Toast.makeText(MyFamily.this, error.getMessage(),
								Toast.LENGTH_SHORT).show();

					}

				});

		AppController.getInstance().addToRequestQueue(req);

	}

	private void init() {
		// TODO Auto-generated method stub
		expandListView = (ExpandableListView) findViewById(R.id.familyList);
		careGiver = new HashMap<String, ArrayList<ParentListModel>>();
		CareReciever = new ArrayList<ExpandableListGroupItem>();
		pendingReq = new ArrayList<ExpandableListGroupItem>();
		requests = new ArrayList<RequestModel>();

		me = new ExpandableListGroupItem();
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.footerView:
			addContact();
			break;

		default:
			break;
		}
	}

	@Override
	public void onButtonClickListner(int position, String value,
			Boolean isAccept) {
		// TODO Auto-generated method stub
		if (position == ExpandableListAdapter.ADD_FOR_ME) {
			addContact();
		}
		if (position == ExpandableListAdapter.CONN_REQ) {
			if (isAccept) {
				acceptRequest(value);
			} else {
				rejectRequest(value);
			}
		}
		if (position == ExpandableListAdapter.ADD_FOR_CR) {
			addContact();
		}

	}

	public void addContact() {
		DialogFragment newFragment = new ContactDialogFragment();
		newFragment.setCancelable(true);
		newFragment.show(getSupportFragmentManager(), "TAG");
		((ContactDialogFragment) newFragment).SetButtonListener(MyFamily.this);
	}

	private void rejectRequest(String requestID) {
		// TODO Auto-generated method stub
		JSONObject param = new JSONObject();
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
				"http://54.69.183.186:1340/user/connection-request/"
						+ requestID + "/reject", param,

				new Response.Listener<JSONObject>() {
					@SuppressLint("NewApi")
					@Override
					public void onResponse(JSONObject response) {

						Log.d("Response", "" + response);
						// requests.remove(position);
						adapter.notifyDataSetInvalidated();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// String json = null;

						NetworkResponse response = error.networkResponse;

						if (response != null && response.data != null) {
							// int code = response.statusCode;
							// json = new String(response.data);
							// json = trimMessage(json, "message");
							// if (json != null)
							// displayMessage(json, code);

						}
						// hidepDialog();
					}

				});

		AppController.getInstance().addToRequestQueue(req);

	}

	private void acceptRequest(String requestID) {
		// TODO Auto-generated method stub
		JSONObject param = new JSONObject();
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
				"http://54.69.183.186:1340/user/connection-request/"
						+ requestID + "/accept", param,

				new Response.Listener<JSONObject>() {
					@SuppressLint("NewApi")
					@Override
					public void onResponse(JSONObject response) {

						Log.d("Response", "" + response);
						// requests.remove(position);
						// adapter.notifyDataSetChanged();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// String json = null;

						NetworkResponse response = error.networkResponse;

						if (response != null && response.data != null) {
							// int code = response.statusCode;
							// json = new String(response.data);
							// json = trimMessage(json, "message");
							// if (json != null)
							// displayMessage(json, code);

						}
						// hidepDialog();
					}

				});

		AppController.getInstance().addToRequestQueue(req);
	}

	private void fetchMyCRFamily(String id, final int position) {
		// TODO Auto-generated method stub
		CustomRequest req = new CustomRequest(
				"http://54.69.183.186:1340/user/family/" + id,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject responseObject) {
						// TODO Auto-generated method stub
						Log.d("Response Array", " " + responseObject);
						try {
							JSONArray careGivers = responseObject

							.getJSONArray("care_givers");
							ArrayList<ParentListModel> parents = new ArrayList<ParentListModel>();
							int cgCount = careGivers.length();
							for (int i = 0; i < cgCount; i++) {
								JSONObject cg = careGivers.getJSONObject(i);
								ParentListModel item = new ParentListModel();
								item.setParentId(cg.getString("id"));
								item.setParentName(cg.optString("first_name"));
								parents.add(item);
							}
							CareReciever.get(position)
									.setKinCount(cgCount + "");

							careGiver.put(CareReciever.get(position)
									.getUserId(), parents);
							adapter.setupTrips(careGiver, requests,
									CareReciever, pendingReq);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());
						Toast.makeText(MyFamily.this, error.getMessage(),
								Toast.LENGTH_SHORT).show();

					}

				});

		AppController.getInstance().addToRequestQueue(req);

	}

}
