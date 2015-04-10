package com.touchKin.touchkinapp.model;

public class ParentSendTouchModel {
	String userName, userImageUrl;
	Boolean isSelected;

	public ParentSendTouchModel(String userName, String userImageUrl,
			Boolean isSelected) {
		super();
		this.userName = userName;
		this.userImageUrl = userImageUrl;
		this.isSelected = isSelected;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserImageUrl() {
		return userImageUrl;
	}

	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

}
