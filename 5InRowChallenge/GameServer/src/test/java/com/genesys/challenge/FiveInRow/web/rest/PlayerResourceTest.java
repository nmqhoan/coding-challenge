package com.genesys.challenge.FiveInRow.web.rest;

import com.genesys.challenge.FiveInRow.FiveInRowServerApplication;
import com.genesys.challenge.FiveInRow.configuration.ApplicationProperties;
import com.genesys.challenge.FiveInRow.domain.Player;
import com.genesys.challenge.FiveInRow.domain.enumeration.GameCode;
import com.genesys.challenge.FiveInRow.domain.enumeration.GameStatus;
import com.genesys.challenge.FiveInRow.repository.GameRepository;
import com.genesys.challenge.FiveInRow.repository.PlayerRepository;
import com.genesys.challenge.FiveInRow.service.PlayerService;
import com.genesys.challenge.FiveInRow.service.dto.PlayerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
class PlayerResourceTest {
    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";
    private static final String SECOND_PLAYER_USER_NAME = "CCCCCCCCC";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";
    private static final String SECOND_PLAYER_PASSWORD = "CCCCCCCCC";



    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlayerMockMvc;

    private Player player;


    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Player createEntity(EntityManager em) {
        Player player = new Player();
        player.setUserName(DEFAULT_USER_NAME);
        player.setPassword(DEFAULT_PASSWORD);
        return player;
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Player createSecondPlayer(EntityManager em) {
        Player player = new Player();
        player.setUserName(SECOND_PLAYER_USER_NAME);
        player.setPassword(SECOND_PLAYER_PASSWORD);
        return player;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Player createUpdatedEntity(EntityManager em) {
        Player player = new Player();
        player.setUserName(UPDATED_USER_NAME);
        player.setPassword(DEFAULT_PASSWORD);
        return player;
    }

    @BeforeEach
    public void initTest() {
        player = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlayer() throws Exception {
        int databaseSizeBeforeCreate = playerRepository.findAll().size();
        // Create the Player
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setPassword(player.getPassword());
        playerDTO.setUserName(player.getUserName());
        restPlayerMockMvc.perform(post("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
                .andExpect(status().isCreated());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeCreate + 1);
        Player testPlayer = playerList.get(playerList.size() - 1);
        assertThat(testPlayer.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testPlayer.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    public void createPlayerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = playerRepository.findAll().size();

        // Create the Player with an existing ID
        player.setId(1L);
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setPassword(player.getPassword());
        playerDTO.setUserName(player.getUserName());
        playerDTO.setId(player.getId());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerMockMvc.perform(post("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUserNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = playerRepository.findAll().size();
        // set the field null
        player.setUserName(null);

        // Create the Player, which fails.
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setPassword(player.getPassword());
        playerDTO.setUserName(player.getUserName());
        playerDTO.setId(player.getId());

        restPlayerMockMvc.perform(post("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
                .andExpect(status().isBadRequest());

        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = playerRepository.findAll().size();
        // set the field null
        player.setPassword(null);

        // Create the Player, which fails.
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setPassword(player.getPassword());
        playerDTO.setUserName(player.getUserName());
        playerDTO.setId(player.getId());

        restPlayerMockMvc.perform(post("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
                .andExpect(status().isBadRequest());

        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlayers() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList
        restPlayerMockMvc.perform(get("/api/allPlayers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(player.getId().intValue())))
                .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
                .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    public void getPlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", player.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(player.getId().intValue()))
                .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
                .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    public void getNonExistingPlayer() throws Exception {
        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        int databaseSizeBeforeUpdate = playerRepository.findAll().size();

        // Update the player
        Player updatedPlayer = playerRepository.findById(player.getId()).get();
        // Disconnect from session so that the updates on updatedPlayer are not directly saved in db
        em.detach(updatedPlayer);
        updatedPlayer.setUserName(UPDATED_USER_NAME);
        updatedPlayer.setPassword(UPDATED_PASSWORD);

        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setPassword(updatedPlayer.getPassword());
        playerDTO.setUserName(updatedPlayer.getUserName());
        playerDTO.setId(updatedPlayer.getId());

        restPlayerMockMvc.perform(put("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
                .andExpect(status().isOk());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeUpdate);
        Player testPlayer = playerList.get(playerList.size() - 1);
        assertThat(testPlayer.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testPlayer.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getPlayerByUsername() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get the player
        restPlayerMockMvc.perform(get("/api/players").param("username", player.getUserName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(player.getId().intValue()))
                .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
                .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

}