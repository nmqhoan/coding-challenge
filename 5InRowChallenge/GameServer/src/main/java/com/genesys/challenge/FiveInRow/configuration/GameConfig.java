package com.genesys.challenge.FiveInRow.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "game")
public class GameConfig {
    private int rowSize;
    private int colSize;
    private int winSize;

    public int getRowSize() {
        return rowSize;
    }

    public void setRowSize(int rowSize) {
        this.rowSize = rowSize;
    }

    public int getColSize() {
        return colSize;
    }

    public void setColSize(int colSize) {
        this.colSize = colSize;
    }

    public int getWinSize() {
        return winSize;
    }

    public void setWinSize(int winSize) {
        this.winSize = winSize;
    }
}
