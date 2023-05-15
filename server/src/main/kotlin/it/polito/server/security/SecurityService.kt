package it.polito.server.security

import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class SecurityService : ISecurityService {
    override fun login(loginRequest: LoginRequest): ResponseEntity<Any> {
        val url = "http://144.24.191.138:8081/realms/SpringBootKeycloak/protocol/openid-connect/token"
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()

        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val body =
            "grant_type=password&client_id=springboot-keycloak-client&username=${loginRequest.username}&password=${loginRequest.password}"

        val entity = HttpEntity(body, headers)

        return try {
            val response = restTemplate.exchange(url, HttpMethod.POST, entity, TokenResponse::class.java)
            ResponseEntity.ok(response.body?.access_token)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials")
        }
    }

}