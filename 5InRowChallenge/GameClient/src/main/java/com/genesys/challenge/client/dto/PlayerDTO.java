package com.genesys.challenge.client.dto;

public class PlayerDTO {

	private Long id;
	private String userName;
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "PlayerDTO{" +
				"userName='" + userName + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
