package com.genesys.challenge.FiveInRow.configuration;

import com.genesys.challenge.FiveInRow.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PlayerRepository playerRepository;

//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(new UserDetailsServiceImpl(playerRepository))
//				.passwordEncoder(new BCryptPasswordEncoder());
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.anyRequest()
		.permitAll()
//		.authenticated()
//		.and()
//		.formLogin()
//		.usernameParameter("username")
//		.passwordParameter("password")
		.and()
		.httpBasic()
		.and()
		.csrf()
		.disable();
		http.headers().frameOptions().disable();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowSemicolon(true);
		web.httpFirewall(firewall);
		super.configure(web);
	}
}
