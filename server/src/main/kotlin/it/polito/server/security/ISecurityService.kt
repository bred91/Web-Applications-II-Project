package it.polito.server.security

import org.springframework.http.ResponseEntity

interface ISecurityService {
    fun login(loginRequest: LoginRequest) : ResponseEntity<Any>
}