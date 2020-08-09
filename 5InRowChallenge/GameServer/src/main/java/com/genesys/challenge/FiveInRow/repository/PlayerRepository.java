package com.genesys.challenge.FiveInRow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.genesys.challenge.FiveInRow.domain.Player;

import java.util.Optional;


@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findOneByUserName(String userName);
}
