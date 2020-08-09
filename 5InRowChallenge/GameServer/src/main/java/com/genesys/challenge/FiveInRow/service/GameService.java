package com.genesys.challenge.FiveInRow.service;

import com.genesys.challenge.FiveInRow.domain.Game;
import com.genesys.challenge.FiveInRow.domain.Player;
import com.genesys.challenge.FiveInRow.domain.enumeration.GameStatus;
import com.genesys.challenge.FiveInRow.service.dto.GameDTO;

import java.util.List;
import java.util.Optional;

public interface GameService {
    Game save(GameDTO gameDTO);

    Game update(GameDTO gameDTO);

    Game updateGameStatus(Game game, GameStatus gameStatus);

    Game joinGame(Player player, GameDTO gameDTO);

    Optional<Game> findOne(Long id);

    List<Game> getAvailableGamesToJoin(Player player);

    void checkGameLogic(Game game);
}
