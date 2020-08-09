package com.genesys.challenge.FiveInRow.web.rest;

import com.genesys.challenge.FiveInRow.FiveInRowServerApplication;
import com.genesys.challenge.FiveInRow.domain.Game;
import com.genesys.challenge.FiveInRow.domain.Move;
import com.genesys.challenge.FiveInRow.domain.Player;
import com.genesys.challenge.FiveInRow.domain.enumeration.GameCode;
import com.genesys.challenge.FiveInRow.repository.MoveRepository;
import com.genesys.challenge.FiveInRow.service.MoveService;
import com.genesys.challenge.FiveInRow.service.dto.MoveDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FiveInRowServerApplication.class)
@AutoConfigureMockMvc
@WithMockUser
@WebAppConfiguration
class MoveResourceTest {

    private static final Integer DEFAULT_GRID_ROW = 1;
    private static final Integer UPDATED_GRID_ROW = 2;

    private static final Integer DEFAULT_GRID_COLUMN = 1;
    private static final Integer UPDATED_GRID_COLUMN = 2;

    private static final Instant DEFAULT_MOVED_TIME = Instant.now();
    private static final Instant UPDATED_MOVED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final GameCode DEFAULT_GAME_CODE = GameCode.X;
    private static final GameCode UPDATED_GAME_CODE = GameCode.O;

    @Autowired
    private MoveRepository moveRepository;

    @Autowired
    private MoveService moveService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoveMockMvc;

    private Move move;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Move createEntity(EntityManager em) {
        Move move = new Move();
        move.setGridRow(DEFAULT_GRID_ROW);
        move.setGridColumn(DEFAULT_GRID_COLUMN);
        move.setMovedTime(DEFAULT_MOVED_TIME);
        move.setGameCode(DEFAULT_GAME_CODE);
        // Add required entity
        Game game;
        if (TestUtil.findAll(em, Game.class).isEmpty()) {
            game = GameResourceTest.createEntity(em);
            em.persist(game);
            em.flush();
        } else {
            game = TestUtil.findAll(em, Game.class).get(0);
        }
        move.setGame(game);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceTest.createEntity(em);
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        move.setPlayer(player);
        return move;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Move createUpdatedEntity(EntityManager em) {
        Move move = new Move();
        move.setGridRow(UPDATED_GRID_ROW);
        move.setGridColumn(UPDATED_GRID_COLUMN);
        move.setMovedTime(UPDATED_MOVED_TIME);
        move.setGameCode(UPDATED_GAME_CODE);
        // Add required entity
        Game game;
        if (TestUtil.findAll(em, Game.class).isEmpty()) {
            game = GameResourceTest.createUpdatedEntity(em);
            em.persist(game);
            em.flush();
        } else {
            game = TestUtil.findAll(em, Game.class).get(0);
        }
        move.setGame(game);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceTest.createUpdatedEntity(em);
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        move.setPlayer(player);
        return move;
    }

    @BeforeEach
    public void initTest() {
        move = createEntity(em);
    }

    @Test
    @Transactional
    public void createMove() throws Exception {
        int databaseSizeBeforeCreate = moveRepository.findAll().size();
        // Create the Move
        MoveDTO moveDTO = new MoveDTO();
        moveDTO.setPlayerGameCode(move.getGameCode());
        moveDTO.setMovedTime(move.getMovedTime());
        moveDTO.setGridColumn(move.getGridColumn());
        moveDTO.setGridRow(move.getGridRow());
        moveDTO.setGameId(move.getGame().getId());
        moveDTO.setPlayerId(move.getPlayer().getId());

        restMoveMockMvc.perform(post("/api/moves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(moveDTO)))
                .andExpect(status().isCreated());

        // Validate the Move in the database
        List<Move> moveList = moveRepository.findAll();
        assertThat(moveList).hasSize(databaseSizeBeforeCreate + 1);
        Move testMove = moveList.get(moveList.size() - 1);
        assertThat(testMove.getGridRow()).isEqualTo(DEFAULT_GRID_ROW);
        assertThat(testMove.getGridColumn()).isEqualTo(DEFAULT_GRID_COLUMN);
        assertThat(testMove.getMovedTime()).isEqualTo(DEFAULT_MOVED_TIME);
        assertThat(testMove.getGameCode()).isEqualTo(DEFAULT_GAME_CODE);
    }

    @Test
    @Transactional
    public void createMoveWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = moveRepository.findAll().size();

        // Create the Move with an existing ID
        move.setId(1L);
        MoveDTO moveDTO = new MoveDTO();
        moveDTO.setId(move.getId());
        moveDTO.setPlayerGameCode(move.getGameCode());
        moveDTO.setMovedTime(move.getMovedTime());
        moveDTO.setGridColumn(move.getGridColumn());
        moveDTO.setGridRow(move.getGridRow());
        moveDTO.setGameId(move.getGame().getId());
        moveDTO.setPlayerId(move.getPlayer().getId());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoveMockMvc.perform(post("/api/moves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(moveDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Move in the database
        List<Move> moveList = moveRepository.findAll();
        assertThat(moveList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkGridRowIsRequired() throws Exception {
        int databaseSizeBeforeTest = moveRepository.findAll().size();
        // set the field null
        move.setGridRow(null);

        // Create the Move, which fails.
        MoveDTO moveDTO = new MoveDTO();
        moveDTO.setId(move.getId());
        moveDTO.setPlayerGameCode(move.getGameCode());
        moveDTO.setMovedTime(move.getMovedTime());
        moveDTO.setGridColumn(move.getGridColumn());
        moveDTO.setGridRow(move.getGridRow());
        moveDTO.setGameId(move.getGame().getId());
        moveDTO.setPlayerId(move.getPlayer().getId());


        restMoveMockMvc.perform(post("/api/moves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(moveDTO)))
                .andExpect(status().isBadRequest());

        List<Move> moveList = moveRepository.findAll();
        assertThat(moveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGridColumnIsRequired() throws Exception {
        int databaseSizeBeforeTest = moveRepository.findAll().size();
        // set the field null
        move.setGridColumn(null);

        // Create the Move, which fails.
        MoveDTO moveDTO = new MoveDTO();
        moveDTO.setId(move.getId());
        moveDTO.setPlayerGameCode(move.getGameCode());
        moveDTO.setMovedTime(move.getMovedTime());
        moveDTO.setGridColumn(move.getGridColumn());
        moveDTO.setGridRow(move.getGridRow());
        moveDTO.setGameId(move.getGame().getId());
        moveDTO.setPlayerId(move.getPlayer().getId());


        restMoveMockMvc.perform(post("/api/moves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(moveDTO)))
                .andExpect(status().isBadRequest());

        List<Move> moveList = moveRepository.findAll();
        assertThat(moveList).hasSize(databaseSizeBeforeTest);
    }

//    @Test
//    @Transactional
//    public void getAllMoves() throws Exception {
//        // Initialize the database
//        moveRepository.saveAndFlush(move);
//
//        // Get all the moveList
//        restMoveMockMvc.perform(get("/api/moves?sort=id,desc"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.[*].id").value(hasItem(move.getId().intValue())))
//                .andExpect(jsonPath("$.[*].gridRow").value(hasItem(DEFAULT_GRID_ROW)))
//                .andExpect(jsonPath("$.[*].gridColumn").value(hasItem(DEFAULT_GRID_COLUMN)))
//                .andExpect(jsonPath("$.[*].movedTime").value(hasItem(DEFAULT_MOVED_TIME.toString())))
//                .andExpect(jsonPath("$.[*].gameCode").value(hasItem(DEFAULT_GAME_CODE.toString())));
//    }

//    @Test
//    @Transactional
//    public void getMove() throws Exception {
//        // Initialize the database
//        moveRepository.saveAndFlush(move);
//
//        // Get the move
//        restMoveMockMvc.perform(get("/api/moves/{id}", move.getId()))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.id").value(move.getId().intValue()))
//                .andExpect(jsonPath("$.gridRow").value(DEFAULT_GRID_ROW))
//                .andExpect(jsonPath("$.gridColumn").value(DEFAULT_GRID_COLUMN))
//                .andExpect(jsonPath("$.movedTime").value(DEFAULT_MOVED_TIME.toString()))
//                .andExpect(jsonPath("$.gameCode").value(DEFAULT_GAME_CODE.toString()));
//    }

//    @Test
//    @Transactional
//    public void updateMove() throws Exception {
//        // Initialize the database
//        moveRepository.saveAndFlush(move);
//
//        int databaseSizeBeforeUpdate = moveRepository.findAll().size();
//
//        // Update the move
//        Move updatedMove = moveRepository.findById(move.getId()).get();
//        // Disconnect from session so that the updates on updatedMove are not directly saved in db
//        em.detach(updatedMove);
//        updatedMove.setGridRow(UPDATED_GRID_ROW);
//        updatedMove.setGridColumn(UPDATED_GRID_COLUMN);
//        updatedMove.setMovedTime(UPDATED_MOVED_TIME);
//        updatedMove.setGameCode(UPDATED_GAME_CODE);
//
//        MoveDTO moveDTO = new MoveDTO();
//        moveDTO.setId(updatedMove.getId());
//        moveDTO.setPlayerGameCode(updatedMove.getGameCode());
//        moveDTO.setMovedTime(updatedMove.getMovedTime());
//        moveDTO.setGridColumn(updatedMove.getGridColumn());
//        moveDTO.setGridRow(updatedMove.getGridRow());
//        moveDTO.setGameId(updatedMove.getGame().getId());
//        moveDTO.setPlayerId(updatedMove.getPlayer().getId());
//
//        restMoveMockMvc.perform(put("/api/moves")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(TestUtil.convertObjectToJsonBytes(moveDTO)))
//                .andExpect(status().isOk());
//
//        // Validate the Move in the database
//        List<Move> moveList = moveRepository.findAll();
//        assertThat(moveList).hasSize(databaseSizeBeforeUpdate);
//        Move testMove = moveList.get(moveList.size() - 1);
//        assertThat(testMove.getGridRow()).isEqualTo(UPDATED_GRID_ROW);
//        assertThat(testMove.getGridColumn()).isEqualTo(UPDATED_GRID_COLUMN);
//        assertThat(testMove.getMovedTime()).isEqualTo(UPDATED_MOVED_TIME);
//        assertThat(testMove.getGameCode()).isEqualTo(UPDATED_GAME_CODE);
//    }

    @Test
    @Transactional
    void getMovesInGame() throws Exception {
        moveRepository.saveAndFlush(move);

        // Get all the moveList
        restMoveMockMvc.perform(get("/api/moves/{gameId}", move.getGame().getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(move.getId().intValue())))
                .andExpect(jsonPath("$.[*].gridRow").value(hasItem(DEFAULT_GRID_ROW)))
                .andExpect(jsonPath("$.[*].gridColumn").value(hasItem(DEFAULT_GRID_COLUMN)))
                .andExpect(jsonPath("$.[*].movedTime").value(hasItem(DEFAULT_MOVED_TIME.toString())))
                .andExpect(jsonPath("$.[*].playerGameCode").value(hasItem(DEFAULT_GAME_CODE.toString())));
    }
}