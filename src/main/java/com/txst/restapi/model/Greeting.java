package com.txst.restapi.model;

import com.txst.restapi.lib.DBModel;

public class Greeting {

	private static final String template = "Hello, %s! Your email is %s.";
	private final int id;
	private final String content;
	private final DBModel dbManager = new DBModel();

	public Greeting(int id) {
		this.id = id;
		User user = new User(id);
		this.content = String.format(template, user.getUserName(), user.getEmailAddress());
	}

	public Greeting(int id, String content) {
		this.id = id;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}