package com.genesys.challenge.FiveInRow.service.dto;

import com.genesys.challenge.FiveInRow.domain.enumeration.GameCode;

import com.genesys.challenge.FiveInRow.domain.enumeration.GameStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {

	private Long id;
	private GameCode firstPlayerGameCode;
	private Long firstPlayerId;
	private Long secondPlayerId;
	private GameStatus gameStatus;
	private Instant createdDate;

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

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof GameDTO)) {
			return false;
		}

		return id != null && id.equals(((GameDTO) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return "GameDTO{" +
				"id=" + id +
				", firstPlayerGameCode=" + firstPlayerGameCode +
				", firstPlayerId=" + firstPlayerId +
				", secondPlayerId=" + secondPlayerId +
				", gameStatus=" + gameStatus +
				", createdDate=" + createdDate +
				'}';
	}
}