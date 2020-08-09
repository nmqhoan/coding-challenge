package com.genesys.challenge.FiveInRow.domain;

import com.genesys.challenge.FiveInRow.domain.enumeration.GameStatus;
import com.genesys.challenge.FiveInRow.domain.enumeration.PlayerStatus;

public class Message {
    private Long fromId;
    private Long toId;
    private Long gameId;
    private PlayerStatus playerStatus;

    public Message(){}

    public Message(Long fromId, Long toId, Long gameId, PlayerStatus playerStatus) {
        this.fromId = fromId;
        this.toId = toId;
        this.gameId = gameId;
        this.playerStatus = playerStatus;
    }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "fromId=" + fromId +
                ", toId=" + toId +
                ", gameId=" + gameId +
                ", playerStatus=" + playerStatus +
                '}';
    }
}
