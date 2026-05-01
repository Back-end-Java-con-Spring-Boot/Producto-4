package com.alquilatusvehiculos.alquila_tus_vehiculos.controller.rest;

import com.alquilatusvehiculos.alquila_tus_vehiculos.dto.LoginRequestDTO;
import com.alquilatusvehiculos.alquila_tus_vehiculos.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación API", description = "Endpoints para iniciar sesión y generar Tokens JWT")
public class AuthApiController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Recibe usuario y contraseña. Si son correctos, devuelve un Token JWT para acceder a las rutas protegidas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso, Token generado"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    })
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        String jwt = jwtUtils.generateJwtToken(authentication);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        response.put("type", "Bearer");

        return ResponseEntity.ok(response);
    }
}