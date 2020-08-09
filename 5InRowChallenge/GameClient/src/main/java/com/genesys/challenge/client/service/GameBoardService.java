package com.genesys.challenge.client.service;

import com.genesys.challenge.client.domain.Game;
import com.genesys.challenge.client.domain.GameBoard;
import com.genesys.challenge.client.dto.MoveDTO;
import com.genesys.challenge.client.enummeration.GameCode;
import com.genesys.challenge.client.util.GameUtils;

import java.util.List;

public class GameBoardService {

    private MoveService moveService;

    public GameBoardService() {
        this.moveService = new MoveService();
    }

    /**
     *
     * @param gameBoard
     * @param col
     * @param gameCode
     * @return return according row number of move, return -1 if column is full
     */
    public int createMove(GameBoard gameBoard, int col, GameCode gameCode){
        int rowNumber = -1;
        char[][] grid = gameBoard.getGrid();
        if (grid[0][col-1] != '-'){
            System.out.println("That column is already full.");
            return -1;
        }
        for (int row = grid.length-1; row >= 0; row--){
            if (grid[row][col-1] == '-'){
                grid[row][col-1] = gameCode.toString().charAt(0);
                rowNumber = row+1;
                return rowNumber;
            }
        }
        return rowNumber;
    }

    public GameBoard update(Game game, GameBoard gameBoard) {
        List<MoveDTO> moveList = moveService.findMovesByGameId(game.getId());
        char[][] grid = gameBoard.getGrid();
        moveList.stream().forEach(moveDTO -> {
            grid[moveDTO.getGridRow()-1][moveDTO.getGridColumn()-1] = moveDTO.getPlayerGameCode().toString().charAt(0);
        });
        return gameBoard;
    }

    public void checkGameLogic(GameBoard gameBoard) {
        boolean check = horizontalCheck(gameBoard.getGrid());
        if(check==false)
            check = verticalCheck(gameBoard.getGrid());
        if(check==false)
            check = diagonallyCheck(gameBoard.getGrid());
        System.out.println("check winner:" + check);
    }

    private boolean horizontalCheck(char[][] board){
        boolean check = true;
        char winChar;
        for (int row = 0; row < board.length; row++){
            check = true;
            for (int col = 0; col < board[row].length - GameUtils.count_win; col++){
                if (board[row][col] != '-'){
                    for(int i=1;i<=GameUtils.count_win;i++){
                        if(board[row][col]!=board[row][col+i]){
                            check = false;
                            break;
                        }
                    }
                    if(check){
                        winChar = board[row][col];
                        System.out.println(winChar);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean verticalCheck(char[][] board){
        boolean check = true;
        char winChar;
        for (int col = 0; col < board[0].length; col++){
            check = true;
            for (int row = 0; row < board.length - GameUtils.count_win; row++){
                if (board[row][col] != '-'){
                    for(int i=1;i<=GameUtils.count_win;i++){
                        if(board[row][col] != board[row+i][col]){
                            check = false;
                            continue;
                        }
                    }
                    if(check){
                        winChar = board[row][col];
                        System.out.println("Winner is:" + winChar);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean diagonallyCheck(char[][] board){
        boolean check = true;
        char winChar;

        for (int row = 0; row < board.length - GameUtils.count_win; row++){
            check = true;
            for (int col = 0; col < board[row].length - GameUtils.count_win; col++){
                if (board[row][col] != '-'){
                    for(int i=1;i<=GameUtils.count_win;i++){
                        if(board[row][col] != board[row+i][col+i]){
                            check = false;
                            continue;
                        }
                    }
                    if(check){
                        winChar = board[row][col];
                        System.out.println("Winner is:" + winChar);
                        return true;
                    }
                }
            }
        }

        for (int row = 0; row < board.length - GameUtils.count_win; row++){
            check = true;
            for (int col = GameUtils.count_win; col < board[row].length; col++){
                if (board[row][col] != '-'){
                    for(int i=1;i<=GameUtils.count_win;i++){
                        if(board[row][col] != board[row+i][col-i]){
                            check = false;
                            continue;
                        }
                    }
                    if(check){
                        winChar = board[row][col];
                        System.out.println("Winner is:" + winChar);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
