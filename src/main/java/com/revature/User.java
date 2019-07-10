package com.revature;

public class User {
	private String firstName;
	private String lastName;
	private String userType;
	
	public User(String firstName, String lastName, String userType) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userType = userType;
		
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	

}
