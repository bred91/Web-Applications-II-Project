package it.polito.server.security

data class TokenResponse(
    val access_token: String,
    val expires_in: Long,
    val refresh_expires_in: Long,
    val refresh_token: String,
    val token_type: String,
    val not_before_policy: Long,
    val session_state: String,
    val scope: String
)
