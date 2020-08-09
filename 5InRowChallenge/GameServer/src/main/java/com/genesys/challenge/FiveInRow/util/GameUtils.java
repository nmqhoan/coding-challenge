package com.genesys.challenge.FiveInRow.util;

public class GameUtils {
//    public static int count_win = 2;

    public static char[][] generateGameGrid(int row_size, int col_size) {
        char[][] grid = new char[row_size][col_size];
        for(int i=0;i<row_size;i++)
            for(int j = 0; j< grid[i].length; j++)
                grid[i][j] = '-';
         return grid;
    }
}
