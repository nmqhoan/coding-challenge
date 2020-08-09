package com.genesys.challenge.client.util;

import com.genesys.challenge.client.configuration.ServerConfiguration;

public class GameUtils {
    public final static int row_size = Integer.parseInt(ServerConfiguration.prop.getProperty("game.rowSize"));
    public final static int col_size = Integer.parseInt(ServerConfiguration.prop.getProperty("game.colSize"));
    public final static int count_win = Integer.parseInt(ServerConfiguration.prop.getProperty("game.winSize"))-1;
}
