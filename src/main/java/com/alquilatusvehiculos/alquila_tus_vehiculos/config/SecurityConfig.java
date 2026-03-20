package com.alquilatusvehiculos.alquila_tus_vehiculos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        System.out.println("🔥 SECURITY CONFIG ACTIVA 🔥");

        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/vehiculos/**", "/sucursales/**", "/alquiler/**", "/clientes/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/", "/reservar", "/css/**", "/js/**", "/alquiler/crear/**", "/clientes/nuevo/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}