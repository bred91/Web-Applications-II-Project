package it.polito.server.security

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignUpRequestDTO (

    @field:NotBlank(message="Not a valid username")
    val username: String,
    @field:Email(message="Not a valid email address")
    val email: String,
    @field:NotBlank(message="Not a valid lastName")
    val lastName: String,
    @field:NotBlank(message="Not a valid firstName")
    val firstName: String,
    @field:NotBlank(message="Not a valid password")
    val password: String
)
