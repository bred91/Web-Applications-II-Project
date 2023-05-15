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



@RestController
class SecurityController(private val securityService: ISecurityService) {

        @PostMapping("/login")
        fun login(@RequestBody loginRequest: LoginRequest) : ResponseEntity<Any> {
            return securityService.login(loginRequest)
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

        @GetMapping("/test")
        fun getTest(principal: Principal) : ResponseEntity<String> {
            val token = principal as JwtAuthenticationToken
            val userEmail = token.tokenAttributes["email"] as String?
            return ResponseEntity.ok("EMAIL = $userEmail")

        }
    }