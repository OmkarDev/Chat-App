package com.omkardixit.main.model;

import java.time.Instant;

public class ChatMessage {

	private String uid;
	private String user;
	private String content;
	private Instant timestamp;
	private String receiver;

	public ChatMessage(String uid, String user, String content, String receiver, Instant timestamp) {
		this.uid = uid;
		this.user = user;
		this.content = content;
		this.timestamp = timestamp;
		this.receiver = receiver;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	@Override
	public String toString() {
		return "ChatMessage [uid=" + uid + ", user=" + user + ", content=" + content + ", timestamp=" + timestamp
				+ ", receiver=" + receiver + "]";
	}


}
