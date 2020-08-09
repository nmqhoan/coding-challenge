package com.genesys.challenge.FiveInRow.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Check;

import com.genesys.challenge.FiveInRow.domain.enumeration.GameStatus;
import com.genesys.challenge.FiveInRow.domain.enumeration.GameCode;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@Check(constraints = "first_player_game_code = 'O' or first_player_game_code = 'X' "
		+ "and game_status = 'IN_PROGRESS' or game_status = 'FIRST_PLAYER_WON' or game_status = 'SECOND_PLAYER_WON'"
		+ "or game_status = 'TIE' or game_status = 'WAITS_FOR_PLAYER' ")
@NoArgsConstructor
@AllArgsConstructor
public class Game implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "second_player_id", nullable = true)
	private Player secondPlayer;

	@ManyToOne
	@JoinColumn(name = "first_player_id", nullable = false)
	private Player firstPlayer;

	@Enumerated(EnumType.STRING)
	private GameCode firstPlayerGameCode;

	@Enumerated(EnumType.STRING)
	private GameStatus gameStatus;

	@Column(name = "createdDate", nullable = false)
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
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Game)) {
			return false;
		}
		return id != null && id.equals(((Game) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
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
