package it.polito.server.security


import io.micrometer.observation.annotation.Observed
import jakarta.validation.Valid
import org.keycloak.admin.client.Keycloak
import org.springframework.http.*
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

data class Response(val name: String?, val email: String?)

@RestController
@Observed
class SecurityController(private val securityService: ISecurityService, private val keycloak: Keycloak) {


    @PostMapping("/createExpert")
    fun createExpert(@RequestBody @Valid signUpRequestDTO: SignUpRequestDTO): ResponseEntity<Any> {

        var res =  securityService.createExpert(signUpRequestDTO)
        println(res)
        return res
    }

    @PostMapping("/signup")
    fun signUp(@RequestBody @Valid signUpRequestDTO: SignUpRequestDTO): ResponseEntity<Any> {
        return securityService.signUp(signUpRequestDTO)
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequestDTO: LoginRequestDTO) : ResponseEntity<Any> {
        return securityService.login(loginRequestDTO)
    }

    @PostMapping("/logout2")
    fun logout(@RequestBody logoutRequestDTO: LogoutRequestDTO) : ResponseEntity<Any> {
        return securityService.logout(logoutRequestDTO)
    }

    @GetMapping("/info")
    fun getInfo(principal: Principal): ResponseEntity<Any>  {
        val token = principal as JwtAuthenticationToken
        val userName = token.tokenAttributes["name"] as String?
        val userEmail = token.tokenAttributes["email"] as String?
        val response = Response(userName, userEmail)
        return ResponseEntity.ok(response)
    }
    @GetMapping("/manager")
    fun getManager(principal: Principal): ResponseEntity<Any> {
        val token = principal as JwtAuthenticationToken
        val userName = token.tokenAttributes["name"] as String?
        val userEmail = token.tokenAttributes["email"] as String?
        val response = Response(userName, userEmail)
        return ResponseEntity.ok(response)
        //return ResponseEntity.ok("Hello Manager \nUser Name : $userName\nUser Email : $userEmail")
    }

    @GetMapping("/expert")
    fun getExpert(principal: Principal): ResponseEntity<Any> {
        val token = principal as JwtAuthenticationToken
        val userName = token.tokenAttributes["name"] as String?
        val userEmail = token.tokenAttributes["email"] as String?
        val response = Response(userName, userEmail)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/customer")
    fun getUser(principal: Principal): ResponseEntity<Any> {
        val token = principal as JwtAuthenticationToken
        val userName = token.tokenAttributes["name"] as String?
        val userEmail = token.tokenAttributes["email"] as String?
        val response = Response(userName, userEmail)
        return ResponseEntity.ok(response)
        //return ResponseEntity.ok("Hello Customer \nUser Name : $userName\nUser Email : $userEmail")
    }

    @GetMapping("/test")
    fun getTest(principal: Principal) : ResponseEntity<String> {
        val token = principal as JwtAuthenticationToken
        val userEmail = token.tokenAttributes["email"] as String?
        return ResponseEntity.ok("EMAIL = $userEmail")
    }
}