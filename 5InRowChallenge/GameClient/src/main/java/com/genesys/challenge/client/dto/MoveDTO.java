package com.genesys.challenge.client.dto;



import com.genesys.challenge.client.enummeration.GameCode;
import com.genesys.challenge.client.enummeration.GameStatus;

import java.time.Instant;


public class MoveDTO {

	private int gridColumn;
	private int gridRow;
	private Instant movedTime;
	private Long playerId;
	private Long gameId;
	private GameCode playerGameCode;

	public int getGridColumn() {
		return gridColumn;
	}

	public void setGridColumn(int gridColumn) {
		this.gridColumn = gridColumn;
	}

	public int getGridRow() {
		return gridRow;
	}

	public void setGridRow(int gridRow) {
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

	@Override
	public String toString() {
		return "MoveDTO{" +
				"gridColumn=" + gridColumn +
				", gridRow=" + gridRow +
				", movedTime=" + movedTime +
				", playerId=" + playerId +
				", gameId=" + gameId +
				", playerGameCode=" + playerGameCode +
				'}';
	}
}
