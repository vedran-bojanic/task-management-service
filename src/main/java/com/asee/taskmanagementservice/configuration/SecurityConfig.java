package com.asee.taskmanagementservice.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            .authorizeHttpRequests(requests -> requests
                .requestMatchers(
                    "/h2-console/**",              // H2 Console
                    "/swagger-ui/**",              // Swagger UI assets
                    "/api-docs/swagger-config" ,   // Swagger configuration
                    "/v3/api-docs/**",             // OpenAPI spec
                    "/health",                     // Health checks
                    "/users/register",             // Public registration
                    "/tasks/**"                    // Public task-related endpoints
                ).permitAll()
                .anyRequest().authenticated());    // Secure all other endpoints
        return http.build();
    }

}
