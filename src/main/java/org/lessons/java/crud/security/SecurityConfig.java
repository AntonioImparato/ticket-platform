package org.lessons.java.crud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	http.authorizeHttpRequests()
	.requestMatchers(HttpMethod.POST,"tickets/**").hasAnyAuthority("USER", "ADMIN")
	.requestMatchers("tickets/admin/**").hasAuthority("ADMIN")
	.requestMatchers("/tickets/operator/**").hasAnyAuthority("USER")
	.requestMatchers("notes/admin/**").hasAuthority("ADMIN")
	.requestMatchers("/notes/operator/**").hasAnyAuthority("USER")
	.requestMatchers("/notes/**").hasAnyAuthority("USER", "ADMIN")
//	.requestMatchers("/operator").hasAuthority("USER")
//	.requestMatchers("/admin/**").hasAuthority("ADMIN")
	.requestMatchers("/**").permitAll() 
	.and().formLogin()
	.and().logout()
	.and().exceptionHandling();
	
	return http.build();
	}
	
	@Bean
	DatabaseUserDetailsService userDetailsService() {
	return new DatabaseUserDetailsService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	authProvider.setUserDetailsService(userDetailsService());
	authProvider.setPasswordEncoder(passwordEncoder());
	return authProvider;
	}
}

