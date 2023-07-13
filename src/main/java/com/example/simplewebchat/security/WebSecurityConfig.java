package com.example.simplewebchat.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final CustomOAuth2UserService customOauth2UserService;

    public WebSecurityConfig(CustomOAuth2UserService customOauth2UserService) {
        this.customOauth2UserService = customOauth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/").hasAuthority(CHAT_USER)
                        .requestMatchers("/login", "/oauth2/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login").defaultSuccessUrl("/")
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOauth2UserService)))
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .build();
    }

    public static final String CHAT_USER = "CHAT_USER";
}