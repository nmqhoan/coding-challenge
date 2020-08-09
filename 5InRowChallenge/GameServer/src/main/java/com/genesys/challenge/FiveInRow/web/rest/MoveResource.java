package com.genesys.challenge.FiveInRow.web.rest;

import com.genesys.challenge.FiveInRow.domain.Game;
import com.genesys.challenge.FiveInRow.domain.Move;
import com.genesys.challenge.FiveInRow.service.GameService;
import com.genesys.challenge.FiveInRow.service.MoveService;
import com.genesys.challenge.FiveInRow.service.dto.MoveDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MoveResource {
	private final Logger log = LoggerFactory.getLogger(MoveResource.class);

	@Autowired
	private MoveService moveService;

	@Autowired
	private GameService gameService;

	Logger logger = LoggerFactory.getLogger(MoveResource.class);

	@PostMapping(value = "/moves")
	public ResponseEntity<Move> createMove(@RequestBody MoveDTO moveDTO) {
		log.debug("REST request to save Move : {}", moveDTO);
		if (moveDTO.getId() != null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if(moveDTO.getGridColumn()==null||moveDTO.getGridRow()==null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Move move = moveService.save(moveDTO);
		gameService.checkGameLogic(move.getGame());
		return new ResponseEntity<Move>(move,HttpStatus.CREATED);
	}

	@GetMapping(value = "/moves/{gameId}")
	public ResponseEntity<List<MoveDTO>> getMovesInGame(@PathVariable Long gameId) {
		log.debug("REST request to get of moves of game: {}", gameId);
		Optional<Game> game = gameService.findOne(gameId);
		if(game.isPresent())
			return new ResponseEntity<>(moveService.findMovesByGame(game.get()),HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}
}
