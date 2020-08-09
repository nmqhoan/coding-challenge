package com.genesys.challenge.FiveInRow.service.dto;

import javax.validation.constraints.NotNull;

public class PlayerDTO {

	@NotNull
	private String userName;
	@NotNull
	private String password;

	private Long id;

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
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PlayerDTO)) {
			return false;
		}

		return id != null && id.equals(((PlayerDTO) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "PlayerDTO{" +
				"id=" + getId() +
				", userName='" + getUserName() + "'" +
				", password='" + getPassword() + "'" +
				"}";
	}
}
