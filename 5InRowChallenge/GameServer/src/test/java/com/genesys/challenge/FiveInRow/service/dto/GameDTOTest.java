package com.genesys.challenge.FiveInRow.service.dto;

import com.genesys.challenge.FiveInRow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameDTO.class);
        GameDTO gameDTO1 = new GameDTO();
        gameDTO1.setId(1L);
        GameDTO gameDTO2 = new GameDTO();
        assertThat(gameDTO1).isNotEqualTo(gameDTO2);
        gameDTO2.setId(gameDTO1.getId());
        assertThat(gameDTO1).isEqualTo(gameDTO2);
        gameDTO2.setId(2L);
        assertThat(gameDTO1).isNotEqualTo(gameDTO2);
        gameDTO1.setId(null);
        assertThat(gameDTO1).isNotEqualTo(gameDTO2);
    }
}
