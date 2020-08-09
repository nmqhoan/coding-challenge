package com.genesys.challenge.client.domain;

import com.genesys.challenge.client.enummeration.GameCode;
import com.genesys.challenge.client.util.GameUtils;

public class GameBoard {
    int row;
    int col;
    char[][] grid;

    public GameBoard(char[][] grid) {
        this.grid = grid;
    }

    public GameBoard(int row, int col, char[][] grid) {
        this.row = row;
        this.col = col;
        this.grid = grid;
    }

    public GameBoard(int row, int col){
        this.row = row;
        this.col = col;
        grid = new char[row][col];
        initBoard();
    }

    public GameBoard() {
        this.row = GameUtils.row_size;
        this.col = GameUtils.col_size;
        grid = new char[row][col];
        initBoard();
    }

    private void initBoard(){
        for(int i=0;i<row;i++)
            for(int j = 0; j< grid[i].length; j++)
                grid[i][j] = '-';
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public char[][] getGrid() {
        return grid;
    }

    public void setGrid(char[][] grid) {
        this.grid = grid;
    }

    public void printBoard(){
        for (int i = 1; i <= col; i++)
            System.out.print("   " + i + " ");
        System.out.println();

        for (int i = 0; i < grid.length; i++) {
            System.out.print(i+1+" ");
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print("[ " + grid[i][j] + " ]");
            }
            System.out.println();
        }
    }
}
