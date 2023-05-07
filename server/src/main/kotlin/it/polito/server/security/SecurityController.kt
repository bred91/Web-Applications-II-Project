package it.polito.server.security

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/test")
class SecurityController {
    @GetMapping("/anonymous")
    fun getAnonymous(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello Anonymous")
    }

    @GetMapping("/admin")
    fun getAdmin(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello Admin")
    }

    @GetMapping("/user")
    fun getUser(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello User")
    }
}