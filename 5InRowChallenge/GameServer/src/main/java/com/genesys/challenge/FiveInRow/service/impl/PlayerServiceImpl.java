package com.genesys.challenge.FiveInRow.service.impl;

import java.util.List;
import java.util.Optional;

import com.genesys.challenge.FiveInRow.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.genesys.challenge.FiveInRow.service.dto.PlayerDTO;
import com.genesys.challenge.FiveInRow.domain.Player;
import com.genesys.challenge.FiveInRow.repository.PlayerRepository;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {
	private final Logger log = LoggerFactory.getLogger(PlayerServiceImpl.class);

	private final PlayerRepository playerRepository;

	@Autowired
	public PlayerServiceImpl(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	/**
	 * save a player
	 *
	 * @param playerDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public Player save(PlayerDTO playerDTO) {
		Player newPlayer = new Player();
		newPlayer.setUserName(playerDTO.getUserName());
		newPlayer.setPassword(playerDTO.getPassword());
		newPlayer = playerRepository.save(newPlayer);
		return newPlayer;
	}

	/**
	 * update a player
	 *
	 * @param playerDTO the entity to update
	 * @return the persisted entity
	 */
	@Override
	public Player update(PlayerDTO playerDTO) {
		Player player = playerRepository.findById(playerDTO.getId()).get();
		player.setUserName(playerDTO.getUserName());
		player.setPassword(playerDTO.getPassword());
		player = playerRepository.save(player);
		return player;
	}

	/**
	 * get all the players
	 *
	 * @return the list of entities
	 */
	@Override
	public List<Player> findAll() {
		List<Player> players = (List<Player>) playerRepository.findAll();
		return players;
	}

	/**
	 * @param id the id of the entity.
	 * @return
	 */
	@Override
	public Optional<Player> findOne(Long id) {
		log.debug("Request to get Player : {}", id);
		return playerRepository.findById(id);
	}

	/**
	 * @param username the username of the entity.
	 * @return
	 */
	@Override
	public Optional<Player> findByUsername(String username) {
		log.debug("Request to get Player : {}", username);
		return playerRepository.findOneByUserName(username);
	}

	/**
	 * Delete the player by id.
	 *
	 * @param id the id of the entity.
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete Player : {}", id);
		playerRepository.deleteById(id);
	}

}
