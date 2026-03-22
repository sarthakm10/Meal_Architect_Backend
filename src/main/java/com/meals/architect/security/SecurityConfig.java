package com.meals.architect.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Enable CORS so React can talk to Spring Boot
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // 2. Disable CSRF (Cross-Site Request Forgery)
            // We disable this because we are using stateless JWTs, not session cookies.
            .csrf(csrf -> csrf.disable())

            // 3. Define the Access Rules for our API Endpoints
            .authorizeHttpRequests(auth -> auth
                // Allow anyone to view the public ingredient catalog and tags without logging in
                .requestMatchers("/api/ingredients/**", "/api/tags/**").permitAll()
                
                // ALL other requests (saving meals, viewing profiles) require a valid token
                .anyRequest().authenticated()
            )

            // 4. Tell Spring Security to validate tokens using the URL in application.properties
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow requests from your local React development server
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
        
        // Allow the standard HTTP methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Allow React to send the "Authorization" header containing the JWT token
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
