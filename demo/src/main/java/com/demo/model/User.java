package com.demo.model;

public class User {
	private String UserID;
	private String RoleID;
	private String Name;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}
	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getRoleID() {
		return RoleID;
	}

	public void setRoleID(String roleID) {
		RoleID = roleID;
	}
	public User(String userID, String roleID, String name) {
		setUserID(userID);
		setRoleID(roleID);
		setName(name);
	}
}