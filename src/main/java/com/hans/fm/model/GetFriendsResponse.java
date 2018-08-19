package com.hans.fm.model;

import java.util.List;

public class GetFriendsResponse extends BasicResponse{
	private List<String> friends;
	
	private int count;
	
	public GetFriendsResponse() {
		this.setSuccess(false);
		this.setCount(0);
	}

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
