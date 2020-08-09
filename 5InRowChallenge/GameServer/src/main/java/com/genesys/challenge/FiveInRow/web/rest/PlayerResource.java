package com.genesys.challenge.FiveInRow.web.rest;

import com.genesys.challenge.FiveInRow.web.rest.errors.BadRequestAlertException;
import com.genesys.challenge.FiveInRow.web.rest.errors.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.genesys.challenge.FiveInRow.service.dto.PlayerDTO;
import com.genesys.challenge.FiveInRow.domain.Player;
import com.genesys.challenge.FiveInRow.service.PlayerService;
import com.genesys.challenge.FiveInRow.service.Response;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PlayerResource {
	private final Logger log = LoggerFactory.getLogger(PlayerResource.class);

	private static final String ENTITY_NAME = "player";

	@Autowired
	PlayerService playerService;

	/**
	 * {@code POST  /players} : Create a new player.
	 *
	 * @param newPlayerDTO the playerDTO to create.
	 * @return the Player entity
	 **/
	@PostMapping("/players")
	public ResponseEntity<Player> createPlayer(@RequestBody PlayerDTO newPlayerDTO)  throws URISyntaxException {
		log.debug("REST request to save Player : {}", newPlayerDTO);
		if (newPlayerDTO.getId() != null) {
			return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
		}
		if(playerService.findByUsername(newPlayerDTO.getUserName()).isPresent()){
			return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
		}
		if(newPlayerDTO.getUserName()==null||newPlayerDTO.getPassword()==null)
			return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
		Player newPlayer = playerService.save(newPlayerDTO);
		return new ResponseEntity<Player>(newPlayer, HttpStatus.CREATED);
	}


	/**
	 * {@code GET  /players/:id} : get the "id" player.
	 *
	 * @param id the id of the player to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the player, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/players/{id}")
	public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
		log.debug("REST request to get Player : {}", id);
		Optional<Player> player = playerService.findOne(id);
		return ResponseUtil.wrapOrNotFound(player);
	}

	/**
	 * {@code PUT  /players} : Updates an existing player.
	 *
	 * @param playerDTO the playerDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerDTO,
	 * or with status {@code 400 (Bad Request)} if the playerDTO is not valid,
	 * or with status {@code 500 (Internal Server Error)} if the playerDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/players")
	public ResponseEntity<Player> updatePlayer(@Valid @RequestBody PlayerDTO playerDTO) throws URISyntaxException {
		log.debug("REST request to update Player : {}", playerDTO);
		if (playerDTO.getId() == null) {
			return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
		}
		Player result = playerService.update(playerDTO);
		return new ResponseEntity<Player>(result,HttpStatus.OK);
	}

	/**
	 * {@code GET  /players} : get all the players.
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of players in body.
	 */
	@GetMapping("/allPlayers")
	public List<Player> getAllPlayers() {
		log.debug("REST request to get all Players");
		return playerService.findAll();
	}

	/**
	 *
	 * @param username
	 * @return Player
	 */
	@GetMapping("/players")
	public ResponseEntity<Player> getPlayerByUsername(@RequestParam String username) {
		log.debug("REST request to get Player : {}", username);
		Optional<Player> player = playerService.findByUsername(username);
		return ResponseUtil.wrapOrNotFound(player);
	}

}
