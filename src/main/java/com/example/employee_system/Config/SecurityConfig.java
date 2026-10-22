package com.example.employee_system.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.
                cors(AbstractHttpConfigurer::disable).
                csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(auth -> {
                    auth.requestMatchers(

                                    "/auth/signup"
                                    , "/auth/login",
                                    "/auth/forgot-password/{username}",
                                    "/auth/reset-password").permitAll()
                            .requestMatchers(HttpMethod.GET, "/employee").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/employee/{employeeId}").hasAnyRole("USER", "ADMIN")
                            .requestMatchers(HttpMethod.POST, "/employee").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/employee/{employeeId}").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/employee/{employeeId}").hasAnyRole("USER", "ADMIN")
                            .requestMatchers(HttpMethod.POST, "/employee/{employeeId}/leave-requst").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/employee/{employeeId}/leave-requst").hasRole("ADMIN")
                            .anyRequest()
                            .authenticated();
                })
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManager(http));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) {
        var authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

}
