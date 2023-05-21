package it.polito.server.security

data class LoginRequestDTO(
    val username: String,
    val password: String
)