package it.polito.server.security

data class LogoutRequestDTO(
    val accessToken: String,
    val refreshToken: String,
)
