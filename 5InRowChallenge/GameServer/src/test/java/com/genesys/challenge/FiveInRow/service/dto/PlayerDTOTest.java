package com.genesys.challenge.FiveInRow.service.dto;

import com.genesys.challenge.FiveInRow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerDTO.class);
        PlayerDTO playerDTO1 = new PlayerDTO();
        playerDTO1.setId(1L);
        PlayerDTO playerDTO2 = new PlayerDTO();
        assertThat(playerDTO1).isNotEqualTo(playerDTO2);
        playerDTO2.setId(playerDTO1.getId());
        assertThat(playerDTO1).isEqualTo(playerDTO2);
        playerDTO2.setId(2L);
        assertThat(playerDTO1).isNotEqualTo(playerDTO2);
        playerDTO1.setId(null);
        assertThat(playerDTO1).isNotEqualTo(playerDTO2);
    }
}
