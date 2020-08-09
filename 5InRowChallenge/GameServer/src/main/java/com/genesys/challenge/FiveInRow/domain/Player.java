package com.genesys.challenge.FiveInRow.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Player implements Serializable {

	public Player() {
	}

	public Player(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "user_name", unique = true, nullable = false)
	private String userName;

	@Column(name = "password", nullable = false)
	private String password;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Player)) {
			return false;
		}
		return id != null && id.equals(((Player) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "Player{" +
				"id=" + getId() +
				", userName='" + getUserName() + "'" +
				", password='" + getPassword() + "'" +
				"}";
	}
}
