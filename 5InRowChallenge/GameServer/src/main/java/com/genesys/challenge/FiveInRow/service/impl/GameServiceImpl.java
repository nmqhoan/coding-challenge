package com.genesys.challenge.FiveInRow.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.genesys.challenge.FiveInRow.configuration.GameConfig;
import com.genesys.challenge.FiveInRow.domain.enumeration.GameCode;
import com.genesys.challenge.FiveInRow.service.GameService;
import com.genesys.challenge.FiveInRow.service.MoveService;
import com.genesys.challenge.FiveInRow.service.PlayerService;
import com.genesys.challenge.FiveInRow.service.dto.MoveDTO;
import com.genesys.challenge.FiveInRow.util.GameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.genesys.challenge.FiveInRow.service.dto.GameDTO;
import com.genesys.challenge.FiveInRow.domain.enumeration.GameStatus;
import com.genesys.challenge.FiveInRow.domain.Game;
import com.genesys.challenge.FiveInRow.domain.Player;
import com.genesys.challenge.FiveInRow.repository.GameRepository;

@Service
@Transactional
public class GameServiceImpl implements GameService {

	private final GameRepository gameRepository;
	@Autowired
	PlayerService playerService;

	@Autowired
	private MoveService moveService;

	@Autowired
	public GameServiceImpl(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	@Autowired
	private GameConfig gameConfig;

	@Override
    public Game save(GameDTO gameDTO) {
		Game game = new Game();
		if(gameDTO.getFirstPlayerId()!=null) {
			Optional<Player> firstPlayer = playerService.findOne(gameDTO.getFirstPlayerId());
			if (firstPlayer.isPresent())
				game.setFirstPlayer(firstPlayer.get());
		}
		game.setFirstPlayerGameCode(GameCode.X);
		game.setGameStatus(GameStatus.WAITS_FOR_PLAYER);
		if(gameDTO.getCreatedDate()==null)
			game.setCreatedDate(Instant.now());
		else game.setCreatedDate(gameDTO.getCreatedDate());
		game = gameRepository.save(game);
		return game;
	}

	@Override
	public Game update(GameDTO gameDTO) {
		Game game = findOne(gameDTO.getId()).get();
		if(gameDTO.getSecondPlayerId()!=null){
			Optional<Player> secondPlayer = playerService.findOne(gameDTO.getSecondPlayerId());
			if(secondPlayer.isPresent())
				game.setSecondPlayer(secondPlayer.get());
		}
		game.setFirstPlayerGameCode(gameDTO.getFirstPlayerGameCode());
		if(gameDTO.getGameStatus()==null)
			game.setGameStatus(GameStatus.WAITS_FOR_PLAYER);
		else game.setGameStatus(gameDTO.getGameStatus());
		game = gameRepository.save(game);
		return game;
	}

	@Override
    public Game updateGameStatus(Game game, GameStatus gameStatus) {
		Game g = findOne(game.getId()).get();
		g.setGameStatus(gameStatus);
		g = gameRepository.save(g);
		return g;
	}

	@Override
    public Game joinGame(Player player, GameDTO gameDTO) {
		Game game = findOne((long) gameDTO.getId()).get();
		game.setSecondPlayer(player);
		gameRepository.save(game);
		updateGameStatus(game, GameStatus.IN_PROGRESS);
		return game;

	}

	@Override
    public Optional<Game> findOne(Long id) {
		return gameRepository.findById(id);
	}

	@Override
	public List<Game> getAvailableGamesToJoin(Player player) {
		return gameRepository.findByGameStatus(GameStatus.WAITS_FOR_PLAYER).stream()
				.filter(game -> game.getFirstPlayer() != player).collect(Collectors.toList());
	}

	@Override
	public void checkGameLogic(Game game) {
		char[][] board = GameUtils.generateGameGrid(gameConfig.getRowSize(),gameConfig.getColSize());
		List<MoveDTO> moveList = moveService.findMovesByGame(game);
		moveList.stream().forEach(moveDTO -> {
			board[moveDTO.getGridRow()-1][moveDTO.getGridColumn()-1] = moveDTO.getPlayerGameCode().toString().charAt(0);
		});

		boolean check = horizontalCheck(game,board);
		if(check==false)
			check = verticalCheck(game,board);
		if(check==false)
			check = diagonallyCheck(game,board);
		if(check==false)
			check = drawCheck(game,board);
	}

	private boolean drawCheck(Game game, char[][] board) {
		for (int row = 0; row < board.length; row++){
			for (int col = 0; col < board[row].length; col++)
				if(board[row][col]=='-')
					return false;
		}
		updateGameStatus(game,GameStatus.TIE);
		return true;
	}

	private boolean horizontalCheck(Game game,char[][] board){
		boolean check = true;
		int stepWin = gameConfig.getWinSize()-1;
		char winChar;
		for (int row = 0; row < board.length; row++){
			check = true;
			for (int col = 0; col < board[row].length - stepWin; col++){
				if (board[row][col] != '-'){
					for(int i=1;i<=stepWin;i++){
						if(board[row][col]!=board[row][col+i]){
							check = false;
							break;
						}
					}
					if(check){
						winChar = board[row][col];
						findWinnerAndUpdateGameStatus(game,winChar);
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean verticalCheck(Game game,char[][] board){
		boolean check = true;
		char winChar;
		int stepWin = gameConfig.getWinSize()-1;
		for (int col = 0; col < board[0].length; col++){
			check = true;
			for (int row = 0; row < board.length - stepWin; row++){
				if (board[row][col] != '-'){
					for(int i=1;i<=stepWin;i++){
						if(board[row][col] != board[row+i][col]){
							check = false;
							continue;
						}
					}
					if(check){
						winChar = board[row][col];
						findWinnerAndUpdateGameStatus(game,winChar);
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean diagonallyCheck(Game game,char[][] board){
		boolean check = true;
		char winChar;
		int stepWin = gameConfig.getWinSize()-1;
		for (int row = 0; row < board.length - stepWin; row++){
			check = true;
			for (int col = 0; col < board[row].length - stepWin; col++){
				if (board[row][col] != '-'){
					for(int i=1;i<=stepWin;i++){
						if(board[row][col] != board[row+i][col+i]){
							check = false;
							continue;
						}
					}
					if(check){
						winChar = board[row][col];
						findWinnerAndUpdateGameStatus(game,winChar);
						return true;
					}
				}
			}
		}

		for (int row = 0; row < board.length - stepWin; row++){
			check = true;
			for (int col = stepWin; col < board[row].length; col++){
				if (board[row][col] != '-'){
					for(int i=1;i<=stepWin;i++){
						if(board[row][col] != board[row+i][col-i]){
							check = false;
							continue;
						}
					}
					if(check){
						winChar = board[row][col];
						findWinnerAndUpdateGameStatus(game,winChar);
						return true;
					}
				}
			}
		}
		return false;
	}

	private void findWinnerAndUpdateGameStatus(Game game, char winChar) {
		if(game.getFirstPlayerGameCode().toString().charAt(0)==winChar)
			updateGameStatus(game,GameStatus.FIRST_PLAYER_WON);
		else updateGameStatus(game,GameStatus.SECOND_PLAYER_WON);

	}
}
