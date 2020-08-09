package com.genesys.challenge.FiveInRow.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.*;

import com.genesys.challenge.FiveInRow.domain.enumeration.GameCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Move implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "game_id", nullable = false)
	private Game game;

	@Column(name = "gridRow", nullable = false)
	private Integer gridRow;

	@Column(name = "gridColumn", nullable = false)
	private Integer gridColumn;

	@ManyToOne
	@JoinColumn(name = "player_id", nullable = false)
	private Player player;

	@Column(name = "moved_time", nullable = false)
	private Instant movedTime;

	@Enumerated(EnumType.STRING)
	private GameCode gameCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getGridRow() {
		return gridRow;
	}

	public void setGridRow(Integer gridRow) {
		this.gridRow = gridRow;
	}

	public Integer getGridColumn() {
		return gridColumn;
	}

	public void setGridColumn(Integer gridColumn) {
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
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Move)) {
			return false;
		}
		return id != null && id.equals(((Move) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
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
