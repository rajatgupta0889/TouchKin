package com.touchKin.touchkinapp.model;

public class ParentListModel {
	String imageUrl;
	Boolean isSelected;
	String parentName;
	String parentId;
	String parentUserId;

	public ParentListModel() {
		super();
	}

	public ParentListModel(String imageUrl, Boolean isSelected,
			String parentName, String parentId, String parentUserId) {
		super();
		this.imageUrl = imageUrl;
		this.isSelected = isSelected;
		this.parentName = parentName;
		this.parentId = parentId;
		this.parentUserId = parentUserId;
	}

	public String getParentUserId() {
		return parentUserId;
	}

	public void setParentUserId(String parentUserId) {
		this.parentUserId = parentUserId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getImageUrl() {
		return imageUrl;
	}
}
