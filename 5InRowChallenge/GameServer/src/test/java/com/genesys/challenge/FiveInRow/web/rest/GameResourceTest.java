package com.genesys.challenge.FiveInRow.web.rest;

import com.genesys.challenge.FiveInRow.FiveInRowServerApplication;
import com.genesys.challenge.FiveInRow.configuration.ApplicationProperties;
import com.genesys.challenge.FiveInRow.domain.Game;
import com.genesys.challenge.FiveInRow.domain.Player;
import com.genesys.challenge.FiveInRow.domain.enumeration.GameCode;
import com.genesys.challenge.FiveInRow.domain.enumeration.GameStatus;
import com.genesys.challenge.FiveInRow.repository.GameRepository;
import com.genesys.challenge.FiveInRow.service.GameService;
import com.genesys.challenge.FiveInRow.service.dto.GameDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Integration tests for the {@link GameResource} REST controller.
 */
@SpringBootTest(classes = FiveInRowServerApplication.class)
@AutoConfigureMockMvc
@WithMockUser
@EnableConfigurationProperties({ApplicationProperties.class})
@WebAppConfiguration
public class GameResourceTest {

    private static final Instant DEFAULT_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final GameStatus DEFAULT_GAME_STATUS = GameStatus.WAITS_FOR_PLAYER;
    private static final GameStatus UPDATED_GAME_STATUS = GameStatus.IN_PROGRESS;

    private static final GameCode DEFAULT_FIRST_PLAYER_CODE = GameCode.X;
    private static final GameCode UPDATED_FIRST_PLAYER_CODE = GameCode.O;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameMockMvc;

    private Game game;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Game createEntity(EntityManager em) {
        Game game = new Game();
        game.setCreatedDate(DEFAULT_CREATED_TIME);
        game.setGameStatus(DEFAULT_GAME_STATUS);
        game.setFirstPlayerGameCode(DEFAULT_FIRST_PLAYER_CODE);

        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceTest.createUpdatedEntity(em);
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        game.setFirstPlayer(player);
        return game;
     }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Game createUpdatedEntity(EntityManager em) {
        Game game = new Game();
        game.setCreatedDate(UPDATED_CREATED_TIME);
        game.setGameStatus(UPDATED_GAME_STATUS);
        game.setFirstPlayerGameCode(UPDATED_FIRST_PLAYER_CODE);
        return game;
    }

    @BeforeEach
    public void initTest() {
        game = createEntity(em);
    }

    @Test
    @Transactional
    public void createGame() throws Exception {
        int databaseSizeBeforeCreate = gameRepository.findAll().size();
        // Create the Game
        GameDTO gameDTO = new GameDTO();
        gameDTO.setFirstPlayerGameCode(game.getFirstPlayerGameCode());
        gameDTO.setGameStatus(game.getGameStatus());
        gameDTO.setCreatedDate(game.getCreatedDate());
        gameDTO.setFirstPlayerId(game.getFirstPlayer().getId());
        gameDTO.setCreatedDate(game.getCreatedDate());
        restGameMockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(gameDTO)))
                .andExpect(status().isCreated());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeCreate + 1);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getGameStatus()).isEqualTo(DEFAULT_GAME_STATUS);
        assertThat(testGame.getFirstPlayerGameCode()).isEqualTo(DEFAULT_FIRST_PLAYER_CODE);
    }

    @Test
    @Transactional
    public void createGameWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gameRepository.findAll().size();

        // Create the Game with an existing ID
        game.setId(1L);
        GameDTO gameDTO = new GameDTO();
        gameDTO.setFirstPlayerGameCode(game.getFirstPlayerGameCode());
        gameDTO.setId(game.getId());
        gameDTO.setGameStatus(game.getGameStatus());
        gameDTO.setCreatedDate(game.getCreatedDate());
        gameDTO.setFirstPlayerId(game.getFirstPlayer().getId());
        // An entity with an existing ID cannot be created, so this API call must fail
        restGameMockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(gameDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get the game
        restGameMockMvc.perform(get("/api/games/{id}", game.getId()))
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(game.getId().intValue()))
                .andExpect(jsonPath("$.gameStatus").value(DEFAULT_GAME_STATUS.toString()))
                .andExpect(jsonPath("$.firstPlayerGameCode").value(DEFAULT_FIRST_PLAYER_CODE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingGame() throws Exception {
        // Get the game
        restGameMockMvc.perform(get("/api/games/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Update the game
        Game updatedGame = gameRepository.findById(game.getId()).get();
        // Disconnect from session so that the updates on updatedGame are not directly saved in db
        em.detach(updatedGame);
        updatedGame.setCreatedDate(UPDATED_CREATED_TIME);
        updatedGame.setGameStatus(UPDATED_GAME_STATUS);
        updatedGame.setFirstPlayerGameCode(UPDATED_FIRST_PLAYER_CODE);

        GameDTO gameDTO = new GameDTO();
        gameDTO.setFirstPlayerGameCode(updatedGame.getFirstPlayerGameCode());
        gameDTO.setId(updatedGame.getId());
        gameDTO.setGameStatus(updatedGame.getGameStatus());
        gameDTO.setCreatedDate(updatedGame.getCreatedDate());

        restGameMockMvc.perform(put("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(gameDTO)))
                .andExpect(status().isOk());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getCreatedDate()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testGame.getGameStatus()).isEqualTo(UPDATED_GAME_STATUS);
        assertThat(testGame.getFirstPlayerGameCode()).isEqualTo(UPDATED_FIRST_PLAYER_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Create the Game
        GameDTO gameDTO = new GameDTO();
        gameDTO.setFirstPlayerGameCode(game.getFirstPlayerGameCode());
        gameDTO.setId(game.getId());
        gameDTO.setGameStatus(game.getGameStatus());
        gameDTO.setCreatedDate(game.getCreatedDate());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameMockMvc.perform(put("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(gameDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void getGamesToJoin() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        //create second player
        Player secondPlayer = PlayerResourceTest.createSecondPlayer(em);
        em.persist(secondPlayer);
        em.flush();

        // Get all the gameList
        restGameMockMvc.perform(get("/api/games/join/{secondPlayerId}",secondPlayer.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(game.getId().intValue())))
                .andExpect(jsonPath("$.[*].gameStatus").value(hasItem(DEFAULT_GAME_STATUS.toString())))
                .andExpect(jsonPath("$.[*].firstPlayerGameCode").value(hasItem(DEFAULT_FIRST_PLAYER_CODE.toString())));
    }


}