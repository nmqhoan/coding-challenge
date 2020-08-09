package com.genesys.challenge.client.service;

import com.genesys.challenge.client.configuration.ServerConfiguration;
import com.genesys.challenge.client.domain.Player;
import com.genesys.challenge.client.dto.PlayerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class PlayerService {
    private final Logger log = LoggerFactory.getLogger(PlayerService.class);

    RestTemplate restTemplate = new RestTemplate();

    public Player authenticate(String username) {
        Player result = null;
        try {
            URI uri = new URI(ServerConfiguration.baseUrl + "players");
            UriComponents builder = UriComponentsBuilder.fromUri(uri)
                    .queryParam("username",username)
                    .build();
            try{
                result = restTemplate.getForObject(builder.toUriString(), Player.class);
            } catch (Exception e) {
                log.debug("User not found. Create new user.");
            }
            if (result!=null){
                return result;
            }else{
                PlayerDTO playerDTO = new PlayerDTO();
                playerDTO.setUserName(username);
                playerDTO.setPassword(username);
                result = create(playerDTO);
                return result;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Player create(PlayerDTO playerDTO){
        try {
            URI uri = new URI(ServerConfiguration.baseUrl + "players");
            Player result = restTemplate.postForObject(uri, playerDTO, Player.class);
            log.debug("REST request to create Player : {}", result);
            return result;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
