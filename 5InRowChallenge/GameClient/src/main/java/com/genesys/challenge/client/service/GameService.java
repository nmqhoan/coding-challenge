package com.genesys.challenge.client.service;


import com.genesys.challenge.client.configuration.ServerConfiguration;
import com.genesys.challenge.client.domain.Game;
import com.genesys.challenge.client.dto.GameDTO;
import com.genesys.challenge.client.enummeration.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class GameService {
    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private RestTemplate restTemplate ;

    public GameService(){
        this.restTemplate = new RestTemplate();
    }

    public Game createGame(Long playerId){
        try {
            URI uri = new URI(ServerConfiguration.baseUrl + "games");
            GameDTO gameDTO = new GameDTO();
            gameDTO.setFirstPlayerId(playerId);
            gameDTO.setGameStatus(GameStatus.WAITS_FOR_PLAYER);
            Game result = restTemplate.postForObject(uri, gameDTO, Game.class);
            log.debug("REST request to save Game : {}", result);
            return result;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Game> getGamesToJoin(Long playerId){
        try {
            String uri = ServerConfiguration.baseUrl + "games/join/{secondPlayerId}";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("X-Request-Source", "Desktop");
            HttpEntity request = new HttpEntity(headers);
            ResponseEntity<Game[]> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    request,
                    Game[].class,
                    playerId
            );

            List<Game> games = Arrays.asList(response.getBody());
            return games;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Game update(Game currentGame) {
        try {
            URI uri = new URI(ServerConfiguration.baseUrl + "games");
            GameDTO gameDTO = new GameDTO();
            gameDTO.setId(currentGame.getId());
            gameDTO.setFirstPlayerId(currentGame.getId());
            if(currentGame.getSecondPlayer()!=null)
                gameDTO.setSecondPlayerId(currentGame.getSecondPlayer().getId());
            gameDTO.setGameStatus(currentGame.getGameStatus());
            gameDTO.setFirstPlayerGameCode(currentGame.getFirstPlayerGameCode());

            restTemplate.put(uri, gameDTO);
            log.debug("REST request to update Game : {}", currentGame);
            currentGame = getGame(currentGame.getId());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return currentGame;
    }

    public Game getGame(Long gameId) {
        try {
            String uri = ServerConfiguration.baseUrl + "games/{id}";
            Map<String, Long> params = new HashMap<String, Long>();
            params.put("id", gameId);
            Game result = restTemplate.getForObject(uri, Game.class, params);
            log.debug("REST request to get Game : {}", result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
