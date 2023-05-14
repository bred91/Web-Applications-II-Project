package it.polito.server.security

import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider
import org.springframework.context.annotation.Role
import org.springframework.http.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.security.Principal

data class LoginRequest(val username: String, val password: String)
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

@RestController
class TestController {

        /*@PostMapping("/login")
        fun login(@RequestBody loginRequest: LoginRequest) {
           val  kcProvider = KeycloakAuthenticationProvider()

        }*/

        @PostMapping("/login")
        fun login(@RequestBody loginRequest: LoginRequest) : ResponseEntity<Any> {
            val url = "http://144.24.191.138:8081/realms/SpringBootKeycloak/protocol/openid-connect/token"
            val restTemplate = RestTemplate()
            val headers = HttpHeaders()

            headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
            val body =
                "grant_type=password&client_id=springboot-keycloak-client&username=${loginRequest.username}&password=${loginRequest.password}"

            val entity = HttpEntity(body, headers)

            return try {
                val response = restTemplate.exchange(url, HttpMethod.POST, entity, TokenResponse::class.java)
                ResponseEntity.ok(response.body)
            } catch (ex: Exception) {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials")
            }
        }

        @GetMapping("/manager")
        fun getAdmin(principal: Principal): ResponseEntity<String> {
            val token = principal as JwtAuthenticationToken
            val userName = token.tokenAttributes["name"] as String?
            val userEmail = token.tokenAttributes["email"] as String?
            return ResponseEntity.ok("Hello Manager \nUser Name : $userName\nUser Email : $userEmail")
        }

        @GetMapping("/expert")
        fun getExpert(principal: Principal): ResponseEntity<String> {
            val token = principal as JwtAuthenticationToken
            val userName = token.tokenAttributes["name"] as String?
            val userEmail = token.tokenAttributes["email"] as String?
            return ResponseEntity.ok("Hello Expert \nUser Name : $userName\nUser Email : $userEmail")
        }

        @GetMapping("/customer")
        fun getUser(principal: Principal): ResponseEntity<String> {
            val token = principal as JwtAuthenticationToken
            val userName = token.tokenAttributes["name"] as String?
            val userEmail = token.tokenAttributes["email"] as String?
            return ResponseEntity.ok("Hello Customer \nUser Name : $userName\nUser Email : $userEmail")
        }
    }