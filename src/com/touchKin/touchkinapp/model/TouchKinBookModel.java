package com.touchKin.touchkinapp.model;

import java.util.List;

public class TouchKinBookModel {

	private String videoUrl, videoText, videoDate, videoDay, videoSenderImage,
			videoSenderName, videoViewCount, userImage;
	private List<TouchKinComments> touchKinComments;

	public TouchKinBookModel(String videoUrl, String videoText,
			String videoDate, String videoDay, String videoSenderImage,
			String videoSenderName, String videoViewCount, String userImage,
			List<TouchKinComments> touchKinComments) {
		super();
		this.videoUrl = videoUrl;
		this.videoText = videoText;
		this.videoDate = videoDate;
		this.videoDay = videoDay;
		this.videoSenderImage = videoSenderImage;
		this.videoSenderName = videoSenderName;
		this.videoViewCount = videoViewCount;
		this.userImage = userImage;
		this.touchKinComments = touchKinComments;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVideoText() {
		return videoText;
	}

	public void setVideoText(String videoText) {
		this.videoText = videoText;
	}

	public String getVideoDate() {
		return videoDate;
	}

	public void setVideoDate(String videoDate) {
		this.videoDate = videoDate;
	}

	public String getVideoDay() {
		return videoDay;
	}

	public void setVideoDay(String videoDay) {
		this.videoDay = videoDay;
	}

	public String getVideoSenderImage() {
		return videoSenderImage;
	}

	public void setVideoSenderImage(String videoSenderImage) {
		this.videoSenderImage = videoSenderImage;
	}

	public String getVideoSenderName() {
		return videoSenderName;
	}

	public void setVideoSenderName(String videoSenderName) {
		this.videoSenderName = videoSenderName;
	}

	public String getVideoViewCount() {
		return videoViewCount;
	}

	public void setVideoViewCount(String videoViewCount) {
		this.videoViewCount = videoViewCount;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String uderImage) {
		this.userImage = uderImage;
	}

	public List<TouchKinComments> getTouchKinComments() {
		return touchKinComments;
	}

	public void setTouchKinComments(List<TouchKinComments> touchKinComments) {
		this.touchKinComments = touchKinComments;
	}

}
