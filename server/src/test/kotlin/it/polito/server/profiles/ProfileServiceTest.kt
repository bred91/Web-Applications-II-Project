package it.polito.server.profiles

import it.polito.server.products.IProductRepository
import it.polito.server.products.IPurchaseRepository
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
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
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
        val responseGetProfile = restTemplate.getForEntity("/API/profiles/baba@gmail.com",
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

        val responseGetProfile = restTemplate.getForEntity("/API/profiles/baba@gmail.com",
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
        val responseUpdateProfile = restTemplate.exchange("/API/profiles/baba@gmail.com",
            HttpMethod.PUT,
            HttpEntity(
                ProfileDTO(
                    email = "baba@gmail.com",
                    username = "john",
                    name = "John",
                    surname = "Smith",
                    phoneNumber = "1234567890")),
                ProfileDTO::class.java
            )
        Assertions.assertEquals(HttpStatus.OK, responseUpdateProfile.statusCode)
        Assertions.assertEquals("baba@gmail.com", responseUpdateProfile.body?.email)
        Assertions.assertEquals("john", responseUpdateProfile.body?.username)
        Assertions.assertEquals("John", responseUpdateProfile.body?.name)
        Assertions.assertEquals("Smith", responseUpdateProfile.body?.surname)


    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `update a non existing profile`(){
        val responseUpdateProfile = restTemplate.exchange("/API/profiles/amanda@gmail.com",
            HttpMethod.PUT,
            HttpEntity(
                ProfileDTO(
                    email = "amanda@gmail.com",
                    username = "john",
                    name = "John",
                    surname = "Smith",
                    phoneNumber = "1234567890")),
            ProblemDetail::class.java
        )
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateProfile.statusCode)


    }











}