package com.genesys.challenge.client.service;

import com.genesys.challenge.client.configuration.ServerConfiguration;
import com.genesys.challenge.client.domain.Game;
import com.genesys.challenge.client.domain.Move;
import com.genesys.challenge.client.dto.GameDTO;
import com.genesys.challenge.client.dto.MoveDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MoveService {
    private final Logger log = LoggerFactory.getLogger(MoveService.class);
    RestTemplate restTemplate = new RestTemplate();

    public Move create(MoveDTO moveDTO){
        try {
            URI uri = new URI(ServerConfiguration.baseUrl + "moves");

            Move result = restTemplate.postForObject(uri, moveDTO, Move.class);
            log.debug("REST request to save Move : {}", result);
            return result;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MoveDTO> findMovesByGameId(Long gameId) {
        try {
            String uri = ServerConfiguration.baseUrl + "moves/{gameId}";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("X-Request-Source", "Desktop");
            HttpEntity request = new HttpEntity(headers);
            ResponseEntity<MoveDTO[]> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    request,
                    MoveDTO[].class,
                    gameId
            );

            List<MoveDTO> moves = Arrays.asList(response.getBody());
            return moves;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
