package it.polito.server.security


import io.micrometer.observation.annotation.Observed
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.http.*
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.security.Principal


data class UserDetailsDTO(val username:String, val password:String)


@RestController
@Observed
class SecurityController(private val securityService: ISecurityService, private val keycloak: Keycloak) {



    @PostMapping("/signup")
    fun signup(@RequestBody user:UserDetailsDTO):String {
        val userRepresentation = UserRepresentation().apply {
            email = user.username
            isEnabled = true
            isEmailVerified = true
        }

        val credentialRepresentation = CredentialRepresentation().apply {
            type=CredentialRepresentation.PASSWORD
            value=user.password
        }

        userRepresentation.credentials = listOf(credentialRepresentation)

        try {
            keycloak.realm("SpringBootKeycloak").users().create(userRepresentation)
            return "User created succesfully!"
        } catch (e: Exception) {
            return "Failed to create user: ${e.message}"
        }
    }




    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest) : ResponseEntity<Any> {
        return securityService.login(loginRequest)
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