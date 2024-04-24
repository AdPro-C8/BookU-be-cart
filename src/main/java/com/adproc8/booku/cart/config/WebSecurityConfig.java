package com.adproc8.booku.cart.config;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.adproc8.booku.cart.model.User;
import com.adproc8.booku.cart.repository.UserRepository;

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

	private final UserRepository userRepository;
	
	@Autowired
	WebSecurityConfig(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers("/cart/**").permitAll()
						.anyRequest().authenticated())
				.formLogin((form) -> form
						.loginPage("/login")
						.permitAll())
				.logout((logout) -> logout.permitAll());

		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8();
	}

	@Bean
	public UserDetailsService userDetailsService() throws NoSuchElementException {
		return new UserDetailsService() {
			public UserDetails loadUserByUsername(String username) {
				User user = userRepository.findByUsername(username).orElseThrow();
				return org.springframework.security.core.userdetails.User.builder()
						.username(user.getUsername())
						.password(passwordEncoder().encode(user.getPassword()))
						.roles(user.getRole())
						.build();
			}
		};
	}
}