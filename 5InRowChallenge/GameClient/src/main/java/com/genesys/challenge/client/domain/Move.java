package com.genesys.challenge.client.domain;


import com.genesys.challenge.client.enummeration.GameCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.Instant;


@NoArgsConstructor
@AllArgsConstructor
public class Move implements Serializable {

	private Long id;
	private Game game;
	private int gridRow;
	private int gridColumn;
	private Player player;
	private Instant movedTime;
	private GameCode gameCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getGridRow() {
		return gridRow;
	}

	public void setGridRow(int gridRow) {
		this.gridRow = gridRow;
	}

	public int getGridColumn() {
		return gridColumn;
	}

	public void setGridColumn(int gridColumn) {
		this.gridColumn = gridColumn;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Instant getMovedTime() {
		return movedTime;
	}

	public void setMovedTime(Instant movedTime) {
		this.movedTime = movedTime;
	}

	public GameCode getGameCode() {
		return gameCode;
	}

	public void setGameCode(GameCode gameCode) {
		this.gameCode = gameCode;
	}

	@Override
	public String toString() {
		return "Move{" +
				"id=" + id +
				", game=" + game +
				", gridRow=" + gridRow +
				", gridColumn=" + gridColumn +
				", player=" + player +
				", movedTime=" + movedTime +
				", gameCode=" + gameCode +
				'}';
	}
}
