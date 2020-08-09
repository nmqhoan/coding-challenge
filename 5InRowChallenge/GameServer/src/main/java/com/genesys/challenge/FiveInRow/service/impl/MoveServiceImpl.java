package com.genesys.challenge.FiveInRow.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import com.genesys.challenge.FiveInRow.service.GameService;
import com.genesys.challenge.FiveInRow.service.MoveService;
import com.genesys.challenge.FiveInRow.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.genesys.challenge.FiveInRow.service.dto.MoveDTO;
import com.genesys.challenge.FiveInRow.domain.Game;
import com.genesys.challenge.FiveInRow.domain.Move;
import com.genesys.challenge.FiveInRow.domain.Player;
import com.genesys.challenge.FiveInRow.repository.MoveRepository;

@Service
@Transactional
public class MoveServiceImpl implements MoveService {

	private final MoveRepository moveRepository;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private GameService gameService;

	@Autowired
	public MoveServiceImpl(MoveRepository moveRepository) {
		this.moveRepository = moveRepository;
	}

	@Override
	public Move save(MoveDTO moveDTO) {
		Game game = gameService.findOne(moveDTO.getGameId()).get();
		Optional<Player> player = playerService.findOne(moveDTO.getPlayerId());
		Move move = new Move();
		move.setGridColumn(moveDTO.getGridColumn());
		move.setGridRow(moveDTO.getGridRow());
		if(moveDTO.getMovedTime()==null)
			move.setMovedTime(Instant.now());
		else move.setMovedTime(moveDTO.getMovedTime());
		move.setPlayer(player.get());
		move.setGame(game);
		move.setGameCode(moveDTO.getPlayerGameCode());
		move = moveRepository.save(move);
		return move;
	}

	@Override
	public Optional<Move> findOne(Long id) {
		return moveRepository.findById(id);
	}

	@Override
	public List<MoveDTO> findMovesByGame(Game game) {
		List<Move> movesInGame = moveRepository.findByGame(game);
		List<MoveDTO> moves = new ArrayList<>();
		for (Move move : movesInGame) {
			MoveDTO moveDTO = new MoveDTO();
			moveDTO.setGridColumn(move.getGridColumn());
			moveDTO.setGridRow(move.getGridRow());
			moveDTO.setMovedTime(move.getMovedTime());
			moveDTO.setPlayerId(move.getPlayer().getId());
			moveDTO.setPlayerGameCode(move.getGameCode());
			moveDTO.setId(move.getId());
			moveDTO.setPlayerGameCode(move.getGameCode());
			moves.add(moveDTO);
		}
		return moves;
	}
}
