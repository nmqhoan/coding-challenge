package com.genesys.challenge.client.util;

import com.genesys.challenge.client.domain.GameBoard;

public class StringUtil {
    public static final String WAIT_FOR_PLAYER_JOINT = "WAIT FOR OTHER PLAYER TO JOIN";
    public static final String IN_PROGRESS = "GAME IN PROGRESS";
    public static final String WAIT_FOR_PLAYER_TURN = "WAIT FOR PLAYER TURN ";
    public static final String GAME_CODE = "YOUR PLAY GAME CODE IS";
    static String mainMenu;

    public static StringBuilder newGame(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("**********************");
        stringBuilder.append("\n");
        stringBuilder.append("Welcome to Five In Row Challenge");
        stringBuilder.append("\n");
        stringBuilder.append("~-~-~-~-~-~-~-~-~-~-~-");
        stringBuilder.append("\n");
        return  stringBuilder;
    }

    public static StringBuilder drawBoard(GameBoard gameBoard) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        for (int i = 1; i <= gameBoard.getCol(); i++)
            stringBuilder.append("   " + i +" ");
        stringBuilder.append("\n");

        for (int i = 0; i < gameBoard.getGrid().length; i++) {
            stringBuilder.append(i+1+" ");
            for (int j = 0; j < gameBoard.getGrid()[i].length; j++) {
               stringBuilder.append("[ " + gameBoard.getGrid()[i][j] + " ]");
            }
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n");
        return stringBuilder;
    }
}
