package com.genesys.challenge.FiveInRow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.genesys.challenge.FiveInRow.domain.Game;
import com.genesys.challenge.FiveInRow.domain.Move;
import com.genesys.challenge.FiveInRow.domain.Player;


@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {

    List<Move> findByGame(Game game);
    List<Move> findByGameAndPlayer(Game game, Player player);
    int countByGameAndPlayer(Game game, Player player);
}
