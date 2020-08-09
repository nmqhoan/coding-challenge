package com.genesys.challenge.FiveInRow.service;

import com.genesys.challenge.FiveInRow.domain.Player;
import com.genesys.challenge.FiveInRow.service.dto.PlayerDTO;

import java.util.List;
import java.util.Optional;

public interface PlayerService {

    /**
     * Save a player.
     *
     * @param playerDTO the entity to save.
     * @return the persisted entity.
     */
    Player save(PlayerDTO playerDTO);

    /**
     * Get all the players.
     *
     * @return the list of entities.
     */
    List<Player> findAll();

    /**
     * Get the "id" player.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Player> findOne(Long id);

    /**
     * Delete the "id" player.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get the "username" player.
     *
     * @param username the Username of the entity.
     * @return the entity.
     */
    Optional<Player> findByUsername(String username);

    /**
     * update a player
     *
     * @param playerDTO the entity to update
     * @return the persisted entity
     */
    public Player update(PlayerDTO playerDTO);
}
