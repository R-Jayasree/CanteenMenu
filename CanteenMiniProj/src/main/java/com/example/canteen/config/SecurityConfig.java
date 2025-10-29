package com.example.canteen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/api/public/**").permitAll()
	            .requestMatchers("/api/menu/**").permitAll()      // Allow unauthenticated access for menu
	            .requestMatchers("/api/orders/**").permitAll()    // Similarly for orders if needed
	            .anyRequest().authenticated()
	        )
	        .httpBasic(httpBasic -> httpBasic.disable());        // Disable HTTP Basic Auth

	    return http.build();
	}

}
