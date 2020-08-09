package com.genesys.challenge.client.dto;

import com.genesys.challenge.client.enummeration.GameCode;
import com.genesys.challenge.client.enummeration.GameStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {

	private Long id;
	private GameCode firstPlayerGameCode;
	private Long firstPlayerId;
	private Long secondPlayerId;
	private GameStatus gameStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GameCode getFirstPlayerGameCode() {
		return firstPlayerGameCode;
	}

	public void setFirstPlayerGameCode(GameCode firstPlayerGameCode) {
		this.firstPlayerGameCode = firstPlayerGameCode;
	}

	public Long getFirstPlayerId() {
		return firstPlayerId;
	}

	public void setFirstPlayerId(Long firstPlayerId) {
		this.firstPlayerId = firstPlayerId;
	}

	public Long getSecondPlayerId() {
		return secondPlayerId;
	}

	public void setSecondPlayerId(Long secondPlayerId) {
		this.secondPlayerId = secondPlayerId;
	}

	public GameStatus getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}
}