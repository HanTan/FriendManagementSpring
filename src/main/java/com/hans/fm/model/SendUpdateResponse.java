package com.hans.fm.model;

import java.util.List;

public class SendUpdateResponse extends BasicResponse {

	private List<String> recipients;

	public List<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}

}
