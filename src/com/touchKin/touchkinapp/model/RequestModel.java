package com.touchKin.touchkinapp.model;

public class RequestModel {

	String userName, userId, userImage, care_reciever_name;

	public RequestModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RequestModel(String userName, String userId, String userImage,
			String care_reciever_name) {
		super();
		this.userName = userName;
		this.userId = userId;
		this.userImage = userImage;
		this.care_reciever_name = care_reciever_name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public String getCare_reciever_name() {
		return care_reciever_name;
	}

	public void setCare_reciever_name(String care_reciever_name) {
		this.care_reciever_name = care_reciever_name;
	}

}
