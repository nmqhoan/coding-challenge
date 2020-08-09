package com.genesys.challenge.client.domain;

import com.genesys.challenge.client.enummeration.GameCode;
import com.genesys.challenge.client.enummeration.GameStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;


@NoArgsConstructor
@AllArgsConstructor
public class Game implements Serializable {

	private Long id;
	private Player secondPlayer;
	private Player firstPlayer;
	private GameCode firstPlayerGameCode;
	private GameStatus gameStatus;
	private Instant createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public void setSecondPlayer(Player secondPlayer) {
		this.secondPlayer = secondPlayer;
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public void setFirstPlayer(Player firstPlayer) {
		this.firstPlayer = firstPlayer;
	}

	public GameCode getFirstPlayerGameCode() {
		return firstPlayerGameCode;
	}

	public void setFirstPlayerGameCode(GameCode firstPlayerGameCode) {
		this.firstPlayerGameCode = firstPlayerGameCode;
	}

	public GameStatus getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "Game{" +
				"id=" + id +
				", secondPlayer=" + secondPlayer +
				", firstPlayer=" + firstPlayer +
				", firstPlayerGameCode=" + firstPlayerGameCode +
				", gameStatus=" + gameStatus +
				", createdDate=" + createdDate +
				'}';
	}
}
