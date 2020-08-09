package com.genesys.challenge.FiveInRow.service;

import com.genesys.challenge.FiveInRow.domain.Game;
import com.genesys.challenge.FiveInRow.domain.Move;
import com.genesys.challenge.FiveInRow.domain.Player;
import com.genesys.challenge.FiveInRow.domain.enumeration.GameStatus;
import com.genesys.challenge.FiveInRow.service.dto.MoveDTO;

import java.util.List;
import java.util.Optional;

public interface MoveService {
    Move save(MoveDTO moveDTO);

    Optional<Move> findOne(Long id);

    List<MoveDTO> findMovesByGame(Game game);

}
