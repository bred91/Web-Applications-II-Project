package it.polito.server.profiles

import it.polito.server.products.IProductRepository
import it.polito.server.products.IPurchaseRepository
import it.polito.server.security.SignUpRequestDTO
import it.polito.server.security.TokenResponse
import it.polito.server.tickets.ITicketRepository
import it.polito.server.tickets.TicketService
import it.polito.server.tickets.states.IStateRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.web.client.RestTemplate
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class ProfileServiceTest {
    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:latest")
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate.ddl-auto") {"create-drop"}
        }
    }

    @LocalServerPort
    protected var port: Int = 8080
    @Autowired
    lateinit var restTemplate: TestRestTemplate
    @Autowired
    lateinit var ticketRepository: ITicketRepository
    @Autowired
    lateinit var stateRepository: IStateRepository
    @Autowired
    lateinit var profileRepository: IProfileRepository
    @Autowired
    lateinit var ticketService: TicketService
    @Autowired
    lateinit var productRepository: IProductRepository
    @Autowired
    lateinit var purchaseRepository: IPurchaseRepository

    /**
     * Test the API for "create profile"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `create profile`(){

        val responseCreateProfile = restTemplate.postForEntity("/API/profiles/",
            ProfileDTO(
                email = "baba@gmail.com",
                username = "asd",
                name = "John",
                surname = "Smith",
                phoneNumber = "1234567890"
            ), ProfileDTO::class.java);
        Assertions.assertEquals(HttpStatus.CREATED, responseCreateProfile.statusCode)

    }



    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `create a duplicate profile`(){


        `create profile`()
        val responseCreateProfile = restTemplate.postForEntity("/API/profiles/",
            ProfileDTO(
                email = "baba@gmail.com",
                username = "asd",
                name = "John",
                surname = "Smith",
                phoneNumber = "1234567890"
            ), ProblemDetail::class.java);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseCreateProfile.statusCode)

    }

    /**
     * Test the API for "get profile"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `get profile by email`(){

        `create profile`()
        var token = loginFun(Pair("asd", "john"))
        val responseGetProfile = restTemplate.exchange("/API/profiles/baba@gmail.com",HttpMethod.GET,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            ProfileDTO::class.java);
        Assertions.assertEquals(HttpStatus.OK, responseGetProfile.statusCode)
        Assertions.assertEquals("baba@gmail.com", responseGetProfile.body?.email)
        Assertions.assertEquals("asd", responseGetProfile.body?.username)
        Assertions.assertEquals("John", responseGetProfile.body?.name)
        Assertions.assertEquals("Smith", responseGetProfile.body?.surname)


    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `get profile by email non existing`(){

        var token = loginFun(Pair("asd", "john"))
        val responseGetProfile = restTemplate.exchange("/API/profiles/baba@gmail.com",HttpMethod.GET,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            ProblemDetail::class.java);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetProfile.statusCode)

    }

    /**
     * Test the API for "update profile"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `update profile`(){

        `create profile`()
        var token = loginFun(Pair("asd", "john"))
        val responseUpdateProfile = restTemplate.exchange("/API/profiles/baba@gmail.com",
            HttpMethod.PUT,
            HttpEntity(
                ProfileDTO(
                    email = "baba@gmail.com",
                    username = "asd",
                    name = "John",
                    surname = "Smith",
                    phoneNumber = "1234567890"),
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
                ),
                ProfileDTO::class.java
            )
        Assertions.assertEquals(HttpStatus.OK, responseUpdateProfile.statusCode)
        Assertions.assertEquals("baba@gmail.com", responseUpdateProfile.body?.email)
        Assertions.assertEquals("asd", responseUpdateProfile.body?.username)
        Assertions.assertEquals("John", responseUpdateProfile.body?.name)
        Assertions.assertEquals("Smith", responseUpdateProfile.body?.surname)
        Assertions.assertEquals("1234567890", responseUpdateProfile.body?.phoneNumber)



    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `update a non existing profile`(){
        var token = loginFun(Pair("asd", "john"))
        val responseUpdateProfile = restTemplate.exchange("/API/profiles/amanda@gmail.com",
            HttpMethod.PUT,
            HttpEntity(
                ProfileDTO(
                    email = "amanda@gmail.com",
                    username = "john",
                    name = "John",
                    surname = "Smith",
                    phoneNumber = "1234567890"),HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }),
            ProblemDetail::class.java
        )
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateProfile.statusCode)


    }











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