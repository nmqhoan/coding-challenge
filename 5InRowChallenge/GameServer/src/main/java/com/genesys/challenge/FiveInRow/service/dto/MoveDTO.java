package com.genesys.challenge.FiveInRow.service.dto;

import java.time.Instant;

import com.genesys.challenge.FiveInRow.domain.enumeration.GameCode;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class MoveDTO {

	private Long id;
	private Integer gridColumn;
	private Integer gridRow;
	private Instant movedTime;
	private Long playerId;
	private Long gameId;
	private GameCode playerGameCode;

	public Integer getGridColumn() {
		return gridColumn;
	}

	public void setGridColumn(Integer gridColumn) {
		this.gridColumn = gridColumn;
	}

	public Integer getGridRow() {
		return gridRow;
	}

	public void setGridRow(Integer gridRow) {
		this.gridRow = gridRow;
	}

	public Instant getMovedTime() {
		return movedTime;
	}

	public void setMovedTime(Instant movedTime) {
		this.movedTime = movedTime;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public GameCode getPlayerGameCode() {
		return playerGameCode;
	}

	public void setPlayerGameCode(GameCode playerGameCode) {
		this.playerGameCode = playerGameCode;
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
		if (!(o instanceof MoveDTO)) {
			return false;
		}

		return id != null && id.equals(((MoveDTO) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return "MoveDTO{" +
				"id=" + id +
				", gridColumn=" + gridColumn +
				", gridRow=" + gridRow +
				", movedTime=" + movedTime +
				", playerId=" + playerId +
				", gameId=" + gameId +
				", playerGameCode=" + playerGameCode +
				'}';
	}
}
