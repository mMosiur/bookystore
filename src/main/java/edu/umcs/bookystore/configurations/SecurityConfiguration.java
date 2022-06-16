package edu.umcs.bookystore.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.cors((cors) -> cors.disable())
				.csrf((csrf) -> csrf.disable())
				.authorizeRequests((authorizeRequests) -> authorizeRequests
						.anyRequest().permitAll())
				.formLogin((login) -> login
						.loginPage("/user/login")
						.failureUrl("/user/login?error")
						.defaultSuccessUrl("/"))
				.logout((logout) -> logout
						.logoutUrl("/user/logout")
						.logoutSuccessUrl("/user/login?logout"))
				.httpBasic((httpBasic) -> httpBasic.and())
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
