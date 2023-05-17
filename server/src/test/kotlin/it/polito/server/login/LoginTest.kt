package it.polito.server.login

import it.polito.server.security.TokenResponse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.web.client.RestTemplate
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class LoginTest {
    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:latest")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }
        }

        private val customer = Pair("amanda@gmail.com", "amanda")
        private val expert = Pair("expert@mail.com","expert")
        private val admin = Pair("admin@gmail.com", "admin")
        private val manager = Pair("simmanager@gmail.com", "simran")
    }

    @LocalServerPort
    protected var port: Int = 8080


    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `Customer login test`() {
        val token = loginFun(customer)

        Assertions.assertNotNull(token)
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `wrong Customer login test`() {
        val token = loginFun(Pair("wronguser@gmail.com", "wrongpassword"))

        Assertions.assertNull(token)
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `wrong password Customer login test`() {
        val token = loginFun(Pair(customer.first, "wrongpassword"))

        Assertions.assertNull(token)
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `Expert login test`() {
        val token = loginFun(expert)

        Assertions.assertNotNull(token)
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `Manager login test`() {
        val token = loginFun(manager)

        Assertions.assertNotNull(token)
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `Admin login test`() {
        val token = loginFun(admin)

        Assertions.assertNotNull(token)
    }

    fun loginFun(user: Pair<String,String>): String? {

        // test the API for "login"
        val url = "http://144.24.191.138:8081/realms/SpringBootKeycloak/protocol/openid-connect/token"
        val restTemplate = RestTemplate()
        val headers = org.springframework.http.HttpHeaders()

        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val body =
            "grant_type=password&client_id=springboot-keycloak-client&username=${user.first}&password=${user.second}"

        val entity = HttpEntity(body, headers)

        return try {
            val response = restTemplate.exchange(url, HttpMethod.POST, entity, TokenResponse::class.java)
            response.body?.access_token
            //ResponseEntity.ok(response.body?.access_token)
        } catch (ex: Exception) {
            //ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials")
            null
        }
    }
}
