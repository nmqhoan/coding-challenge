package com.genesys.challenge.FiveInRow.domain;

import com.genesys.challenge.FiveInRow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Player.class);
        Player player1 = new Player();
        player1.setId(1L);
        Player player2 = new Player();
        player2.setId(player1.getId());
        assertThat(player1).isEqualTo(player2);
        player2.setId(2L);
        assertThat(player1).isNotEqualTo(player2);
        player1.setId(null);
        assertThat(player1).isNotEqualTo(player2);
    }
}
