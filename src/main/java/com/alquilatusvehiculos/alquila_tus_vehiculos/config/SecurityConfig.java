package com.alquilatusvehiculos.alquila_tus_vehiculos.config;

import com.alquilatusvehiculos.alquila_tus_vehiculos.security.AuthEntryPointJwt;
import com.alquilatusvehiculos.alquila_tus_vehiculos.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthEntryPointJwt authEntryPointJwt;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // ==========================================
    // 1. CONFIGURACIÓN PARA LA API (JWT)
    // ==========================================
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**") // Se activa SOLO para rutas que empiezan con /api/
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPointJwt))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ==========================================
    // 2. CONFIGURACIÓN WEB (CLON EXACTO DE TU ORIGINAL)
    // ==========================================
    @Bean
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

        // Tu mensaje original
        System.out.println("🔥 SECURITY CONFIG ACTIVA - SLEIPNIR 🔥");

        http
                .authorizeHttpRequests(request -> request
                        // ORDEN EXACTO 1: Bloque de ADMIN
                        .requestMatchers("/admin/**", "/vehiculos/**", "/sucursales/**", "/alquiler/**", "/clientes/**")
                        .hasRole("ADMIN")

                        // ORDEN EXACTO 2: Bloque de PermitAll
                        .requestMatchers("/", "/reservar", "/css/**", "/js/**", "/auth/**", "/alquiler/crear/**", "/clientes/nuevo/**")
                        .permitAll()

                        // ORDEN EXACTO 3: AnyRequest
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