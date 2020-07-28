package com.stockies.model;

public class UserCred {


	private String username;
	private String psswd;
	
	
	
	public UserCred(String username, String psswd) {
		this.username = username;
		this.psswd = psswd;
	}
	
	public UserCred() {
		
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPsswd() {
		return psswd;
	}
	public void setPsswd(String psswd) {
		this.psswd = psswd;
	}
	@Override
	public String toString() {
		return "UserCred [username=" + username + ", psswd=" + psswd + "]";
	}
	

}
