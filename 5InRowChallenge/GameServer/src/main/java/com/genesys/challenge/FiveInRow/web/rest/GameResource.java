package com.genesys.challenge.FiveInRow.web.rest;

import com.genesys.challenge.FiveInRow.domain.Game;
import com.genesys.challenge.FiveInRow.domain.Player;
import com.genesys.challenge.FiveInRow.service.GameService;
import com.genesys.challenge.FiveInRow.service.PlayerService;
import com.genesys.challenge.FiveInRow.service.dto.GameDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class GameResource {

	private final Logger log = LoggerFactory.getLogger(GameResource.class);

	private static final String ENTITY_NAME = "game";

	@Autowired
	GameService gameService;

	@Autowired
	PlayerService playerService;

	/**
	 * {@code POST  /games} : Create a new game.
	 *
	 * @param gameDTO the gameDTO to create.
	 * @return the Game entity
	 */
	@PostMapping("/games")
	public ResponseEntity<Game> createGame(@RequestBody GameDTO gameDTO) throws URISyntaxException{
		log.debug("REST request to save Game : {}", gameDTO);
		if (gameDTO.getId() != null) {
			return new ResponseEntity<Game>(HttpStatus.BAD_REQUEST);
		}
		Optional<Player> firstPlayer = playerService.findOne(gameDTO.getFirstPlayerId());
		if(!firstPlayer.isPresent()){
			return new ResponseEntity<Game>(HttpStatus.BAD_REQUEST);
		}
		Game result = gameService.save(gameDTO);
		return new ResponseEntity<Game>(result, HttpStatus.CREATED);
	}

	@GetMapping("/games/join/{secondPlayerId}")
	public List<Game> getGamesToJoin(@PathVariable Long secondPlayerId) {
		Optional<Player> player = playerService.findOne(secondPlayerId);
		return gameService.getAvailableGamesToJoin(player.get());
	}

	@PutMapping(value = "/games")
	public ResponseEntity<Game> updateGame(@RequestBody GameDTO gameDTO) {
		log.debug("REST request to update Game : {}", gameDTO);
		if (gameDTO.getId() == null) {
			return new ResponseEntity<Game>(HttpStatus.BAD_REQUEST);
		}
		Game game = gameService.update(gameDTO);
		return new ResponseEntity<Game>(game,HttpStatus.OK);
	}

	/**
	 * {@code GET  /games/:id} : get the "id" game.
	 *
	 * @param id the id of the gameDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping(value = "/games/{id}")
	public ResponseEntity<Game> getGame(@PathVariable Long id) {
		log.debug("REST request to get Game : {}", id);
		Optional<Game> game = gameService.findOne(id);
		if(game.isPresent())
			return new ResponseEntity<Game>(game.get(),HttpStatus.FOUND);
		else
			return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
	}

}
