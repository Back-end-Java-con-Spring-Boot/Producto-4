package com.alquilatusvehiculos.alquila_tus_vehiculos.config;

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

import com.alquilatusvehiculos.alquila_tus_vehiculos.security.AuthEntryPointJwt;
import com.alquilatusvehiculos.alquila_tus_vehiculos.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

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
    // CONFIGURACIÓN PARA LA API (JWT)
    // ==========================================
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**", "/swagger-ui/**", "/v3/api-docs/**","/swagger-ui.html")
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPointJwt))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**","/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ==========================================
    //  CONFIGURACIÓN WEB
    // ==========================================
    @Bean
    @Order(2)  
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

        System.out.println("🔥 SECURITY CONFIG ACTIVA - SLEIPNIR 🔥");

        http
                .authorizeHttpRequests(request -> request
                        //ADMIN
                        .requestMatchers("/admin/**", "/vehiculos/**", "/sucursales/**", "/alquiler/**", "/clientes/**")
                        .hasRole("ADMIN")
                        //USER
                        .requestMatchers("/user/**")
                        .hasRole("USER")
                        //TODOS
                        .requestMatchers("/", "/reservar", "/css/**", "/js/**", "/auth/**", "/alquiler/crear/**", "/clientes/nuevo/**")
                        .permitAll()
                        //OTROS
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