package it.polito.server.security

import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.http.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.keycloak.admin.client.Keycloak
import org.springframework.web.ErrorResponse
import org.springframework.web.ErrorResponseException


@Service
class SecurityService(private val keycloak: Keycloak) : ISecurityService {

    override fun login(loginRequestDTO: LoginRequestDTO): ResponseEntity<Any> {
        val url = "http://144.24.191.138:8081/realms/SpringBootKeycloak/protocol/openid-connect/token"
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()

        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val body =
            "grant_type=password&client_id=springboot-keycloak-client&username=${loginRequestDTO.username}&password=${loginRequestDTO.password}"

        val entity = HttpEntity(body, headers)

        return try {
            val response = restTemplate.exchange(url, HttpMethod.POST, entity, TokenResponse::class.java)
            ResponseEntity.ok(response.body?.access_token)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials")
        }
    }

    override fun signUp(signUpRequestDTO: SignUpRequestDTO): ResponseEntity<Any> {
        val userRepresentation = UserRepresentation().apply {
            this.username = signUpRequestDTO.username
            this.email = signUpRequestDTO.email
            this.firstName = signUpRequestDTO.firstName
            this.lastName = signUpRequestDTO.lastName
            isEnabled = true
            isEmailVerified = true
        }


        val credentialRepresentation = CredentialRepresentation().apply {
            type= CredentialRepresentation.PASSWORD
            value=signUpRequestDTO.password
        }

        userRepresentation.credentials = listOf(credentialRepresentation)

        val roleRepresentation = keycloak.realm("SpringBootKeycloak").roles().get("app_client").toRepresentation()

        val createUserResponse = keycloak.realm("SpringBootKeycloak").users().create(userRepresentation)
        if(createUserResponse.status!=HttpStatus.CREATED.value()){
            val statusCode = createUserResponse.status
            val responseBody = createUserResponse.readEntity(String::class.java)
            return ResponseEntity.status(statusCode).body(responseBody)
        }


        return try {
            val id = keycloak.realm("SpringBootKeycloak").users().search(signUpRequestDTO.username).first().id
            val userResource =  keycloak.realm("SpringBootKeycloak").users().get(id)
            userResource.roles().realmLevel().add(mutableListOf(roleRepresentation).toList())
            ResponseEntity.status(HttpStatus.CREATED).body("User created successfully!")
        }catch (e: Exception){
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message!!)
        }
    }

    override fun logout(accessToken: String): ResponseEntity<Any> {
        val url = "http://144.24.191.138:8081/realms/SpringBootKeycloak/protocol/openid-connect/revoke"
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()

        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val body =
            "token=${accessToken}&token_type_hint=access_token"

        val entity = HttpEntity(body, headers)

        return try {
            restTemplate.postForEntity(url, entity, Object::class.java)
            ResponseEntity.ok(Unit)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials")
        }
    }


    @PreAuthorize("hasRole('ROLE_Manager')")
    override fun createExpert(signUpRequestDTO: SignUpRequestDTO): ResponseEntity<Any> {

        val expertRepresentation = UserRepresentation().apply {
            this.username = signUpRequestDTO.username
            this.email = signUpRequestDTO.email
            this.firstName = signUpRequestDTO.firstName
            this.lastName = signUpRequestDTO.lastName
            isEnabled = true
            isEmailVerified = true
        }


        val credentialRepresentation = CredentialRepresentation().apply {
            type= CredentialRepresentation.PASSWORD
            value=signUpRequestDTO.password
        }

        expertRepresentation.credentials = listOf(credentialRepresentation)

        val roleRepresentation = keycloak.realm("SpringBootKeycloak").roles().get("app_expert").toRepresentation()

        val createUserResponse = keycloak.realm("SpringBootKeycloak").users().create(expertRepresentation)
        if(createUserResponse.status!=HttpStatus.CREATED.value()){
            val statusCode = createUserResponse.status
            val responseBody = createUserResponse.readEntity(String::class.java)
            return ResponseEntity.status(statusCode).body(responseBody)
        }

        return try {
            val id = keycloak.realm("SpringBootKeycloak").users().search(signUpRequestDTO.username).first().id
            val userResource =  keycloak.realm("SpringBootKeycloak").users().get(id)
            userResource.roles().realmLevel().add(mutableListOf(roleRepresentation).toList())
            ResponseEntity.status(HttpStatus.CREATED).body("Expert created successfully!")
        }catch (e: Exception){
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message!!)
        }


    }

}