package it.polito.server.security

import org.springframework.http.ResponseEntity

interface ISecurityService {
    fun login(loginRequestDTO: LoginRequestDTO) : ResponseEntity<Any>

    fun signUp(signUpRequestDTO: SignUpRequestDTO) : ResponseEntity<Any>

    fun logout(logoutRequestDTO: LogoutRequestDTO): ResponseEntity<Any>

    fun createExpert(signUpRequestDTO: SignUpRequestDTO) : ResponseEntity<Any>
}