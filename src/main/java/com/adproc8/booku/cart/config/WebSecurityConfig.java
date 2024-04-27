package com.adproc8.booku.cart.config;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
						.anyRequest().authenticated())
				.formLogin(Customizer.withDefaults())
				.logout(Customizer.withDefaults())
				.csrf((csrf) -> csrf.disable());

		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			public UserDetails loadUserByUsername(String username)
				throws NoSuchElementException
			{
				User user = userRepository.findByUsername(username).orElseThrow();
				UserDetails userDetails =
					org.springframework.security.core.userdetails.User.builder()
						.username(user.getUsername())
						.password(user.getPassword())
						.roles(user.getRole())
						.build();

				return userDetails;
			}
		};
	}
}