package it.polito.server.security

data class LoginRequest(
    val username: String,
    val password: String
)