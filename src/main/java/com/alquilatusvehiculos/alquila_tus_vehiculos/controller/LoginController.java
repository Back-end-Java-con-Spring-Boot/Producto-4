package com.alquilatusvehiculos.alquila_tus_vehiculos.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class LoginController {

        @GetMapping("/login")
        public String login() {
            return "auth/login";
        }
}
