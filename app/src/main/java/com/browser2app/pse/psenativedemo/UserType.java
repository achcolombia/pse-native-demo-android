package com.browser2app.pse.psenativedemo;

public class UserType {
	private String id;
	private String type;

	public UserType(String id, String type) {
		this.id = id;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return type;
	}
}
