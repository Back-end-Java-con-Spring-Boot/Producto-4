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
                        .requestMatchers("/admin/**", "/vehiculos/**", "/sucursales/**", "/alquiler/**", "/clientes/**")
                        .hasRole("ADMIN")
                        .requestMatchers("/", "/reservar", "/css/**", "/js/**", "/auth/**", "/alquiler/crear/**", "/clientes/nuevo/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/?loginSuccess=true", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}