package it.polito.server.security


import io.micrometer.observation.annotation.Observed
import it.polito.server.profiles.ProfileDTO
import jakarta.validation.Valid
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.http.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.util.*


@RestController
@Observed
class SecurityController(private val securityService: ISecurityService, private val keycloak: Keycloak) {


    @PostMapping("/createExpert")
    fun createExpert(@RequestBody @Valid signUpRequestDTO: SignUpRequestDTO): ResponseEntity<Any> {
        return securityService.createExpert(signUpRequestDTO)
    }

    @PostMapping("/signup")
    fun signUp(@RequestBody @Valid signUpRequestDTO: SignUpRequestDTO): ResponseEntity<Any> {
        return securityService.signUp(signUpRequestDTO)
    }




    @PostMapping("/login")
    fun login(@RequestBody loginRequestDTO: LoginRequestDTO) : ResponseEntity<Any> {
        return securityService.login(loginRequestDTO)
    }

    @GetMapping("/manager")
    fun getManager(principal: Principal): ResponseEntity<String> {
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