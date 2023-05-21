package it.polito.server.security


import it.polito.server.Exception.NotFoundException
import it.polito.server.profiles.ProfileDTO
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.RoleRepresentation
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


data class SignupDTO(val password:String, val profileDTO:ProfileDTO)



@RestController
class SecurityController(private val securityService: ISecurityService, private val keycloak: Keycloak) {

@PreAuthorize("hasRole('ROLE_Manager')")
    @PostMapping("/createExpert")
    fun createExpert(@RequestBody expert:SignupDTO):String {

    val expertRepresentation = UserRepresentation().apply {
        val ( username, email, firstName, lastName ) = expert.profileDTO
        this.username = username
        this.email = email
        this.firstName = firstName
        this.lastName = lastName
        isEnabled = true
        isEmailVerified = true
    }



    val credentialRepresentation = CredentialRepresentation().apply {
        type=CredentialRepresentation.PASSWORD
        value=expert.password
    }

    expertRepresentation.credentials = listOf(credentialRepresentation)

    val roleRepresentation = keycloak.realm("SpringBootKeycloak").roles().get("app_expert").toRepresentation()



    try {
        when(keycloak.realm("SpringBootKeycloak").users().search(expert.profileDTO.username).firstOrNull()) {
            null -> keycloak.realm("SpringBootKeycloak").users().create(expertRepresentation)
            else -> return "Expert already exist"
        }
        println("${expert.profileDTO.username}")
        val id = keycloak.realm("SpringBootKeycloak").users().search(expert.profileDTO.username).first().id
        val userResource =  keycloak.realm("SpringBootKeycloak").users().get(id)
        userResource.roles().realmLevel().add(mutableListOf(roleRepresentation).toList())
        return "Expert crated succesfully!"
    } catch (e: Exception) {
        return "Failed to create expert: ${e}\n ${e.message}"
    }
    }

    @PostMapping("/signup")
    fun signup(@RequestBody user:SignupDTO):String {

        val userRepresentation = UserRepresentation().apply {
            val ( username, email, firstName, lastName ) = user.profileDTO
            this.username = username
            this.email = email
            this.firstName = firstName
            this.lastName = lastName
            isEnabled = true
            isEmailVerified = true
        }



        val credentialRepresentation = CredentialRepresentation().apply {
            type=CredentialRepresentation.PASSWORD
            value=user.password
        }

        userRepresentation.credentials = listOf(credentialRepresentation)

        println("${user.profileDTO}")

        val roleRepresentation = keycloak.realm("SpringBootKeycloak").roles().get("app_client").toRepresentation()

        try {
            when(keycloak.realm("SpringBootKeycloak").users().search(user.profileDTO.username).firstOrNull() ){
                null -> keycloak.realm("SpringBootKeycloak").users().create(userRepresentation)
                else -> return "User already exist"
            }

            val id = keycloak.realm("SpringBootKeycloak").users().search(user.profileDTO.username).firstOrNull { it.email == user.profileDTO.email }?.id
            println("ID = ${id}")
            val userResource =  keycloak.realm("SpringBootKeycloak").users().get(id)
            userResource.roles().realmLevel().add(mutableListOf(roleRepresentation).toList())
            return "User crated succesfully!"
        } catch (e: Exception) {
            return "Failed to create user: ${e}\n ${e.message}"
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