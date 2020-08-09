package com.genesys.challenge.FiveInRow.repository;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.genesys.challenge.FiveInRow.domain.enumeration.GameStatus;
import com.genesys.challenge.FiveInRow.domain.Game;


@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByGameStatus(GameStatus gameStatus);
}
