package com.alquilatusvehiculos.alquila_tus_vehiculos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        System.out.println("🔥 SECURITY CONFIG ACTIVA - SLEIPNIR 🔥");

        http
                .authorizeHttpRequests(request -> request
                        // 1. Rutas protegidas para ADMIN
                        .requestMatchers("/vehiculos/**", "/sucursales/**", "/alquiler/**", "/clientes/**")
                        .hasRole("ADMIN")

                        // 2. Rutas públicas (Añadimos /auth/** para registro y login)
                        .requestMatchers("/", "/reservar", "/css/**", "/js/**", "/auth/**", "/alquiler/crear/**", "/clientes/nuevo/**")
                        .permitAll()

                        // 3. Cualquier otra ruta requiere estar logueado
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login") // <--- ¡CORREGIDO! Coincide con AuthController
                        .loginProcessingUrl("/auth/login") // Donde Spring escucha el POST del formulario
                        .defaultSuccessUrl("/", true) // Dónde va el usuario al entrar bien
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout") // Al salir, vuelve al login
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()); // Mantener deshabilitado para facilitar pruebas en local

        return http.build();
    }
}