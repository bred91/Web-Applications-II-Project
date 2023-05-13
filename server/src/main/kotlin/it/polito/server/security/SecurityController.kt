package it.polito.server.security

import org.springframework.context.annotation.Role
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal


data class LoginRequest(val username: String, val password: String)

@RestController
class TestController {



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