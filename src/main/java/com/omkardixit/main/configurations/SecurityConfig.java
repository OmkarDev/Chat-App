package com.omkardixit.main.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.omkardixit.main.services.UserService;

@Configuration
public class SecurityConfig {

	@Autowired
	private UserService userService;

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return userService.getUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
			}
		};
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.formLogin(formLogin -> formLogin.loginPage("/login"));
		http.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")));
		http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/login").permitAll()
				.requestMatchers("/style/**").permitAll().anyRequest().authenticated());
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

}
