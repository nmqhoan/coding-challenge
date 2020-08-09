package com.genesys.challenge.FiveInRow;

import com.genesys.challenge.FiveInRow.configuration.ApplicationProperties;
import com.genesys.challenge.FiveInRow.domain.Player;
import com.genesys.challenge.FiveInRow.repository.PlayerRepository;
import com.genesys.challenge.FiveInRow.util.GameUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class FiveInRowServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FiveInRowServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(PlayerRepository playerRepository) {
		return (args) -> {

			// save a couple of players
			playerRepository.save(new Player("nmqhoan", "nmqhoan"));
			playerRepository.save(new Player("nmqhuy", "nmqhuy"));
		};
	}
}
