package it.polito.server.employees

import it.polito.server.products.IProductRepository
import it.polito.server.products.IPurchaseRepository
import it.polito.server.products.ProductDTO
import it.polito.server.products.ProductServiceTest
import it.polito.server.profiles.IProfileRepository
import it.polito.server.profiles.ProfileDTO
import it.polito.server.security.TokenResponse
import it.polito.server.tickets.ITicketRepository
import it.polito.server.tickets.TicketService
import it.polito.server.tickets.TicketServiceTest
import it.polito.server.tickets.priorities.Priority
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
class EmployeeServiceTest {
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
        private val manager = Pair("simmanager@gmail.com", "simran")
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
    @Autowired
    lateinit var roleRepository: IRoleRepository

    /**
     * Test the API for "create employee"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `create employee`(){

        val token = loginFun(manager)

        roleRepository.save(Role().apply{ id = 1; name="EXPERT" })


        val responseCreateEmployee = restTemplate.exchange("/API/employees/",
            HttpMethod.POST,
            HttpEntity(
                EmployeeDTO(
                    email = "baba@gmail.com",
                    name = "John",
                    surname = "Smith",
                    username = "johnny",
                    role=RoleDTO(id = 1, name = "EXPERT")
                ),
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            )
            , EmployeeDTO::class.java);
        Assertions.assertEquals(HttpStatus.CREATED, responseCreateEmployee.statusCode)

    }

    /**
     * Test the API for "get employee"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `get employee`(){

        `create employee`()
        val token = loginFun(manager)
        Assertions.assertNotNull(token)

        val responseGetEmployee = restTemplate.exchange(
            "/API/employees/1",
            HttpMethod.GET,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            EmployeeDTO::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, responseGetEmployee.statusCode)
        Assertions.assertEquals("baba@gmail.com", responseGetEmployee.body?.email)
        Assertions.assertEquals("John", responseGetEmployee.body?.name)
        Assertions.assertEquals("Smith", responseGetEmployee.body?.surname)
        Assertions.assertEquals("johnny", responseGetEmployee.body?.username)
        Assertions.assertEquals("EXPERT", responseGetEmployee.body?.role?.name)


    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `get a non existing employee`(){

        `create employee`()
        val token = loginFun(manager)
        Assertions.assertNotNull(token)

        val responseGetEmployee = restTemplate.exchange(
            "/API/employees/1000",
            HttpMethod.GET,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            ProblemDetail::class.java
        )

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetEmployee.statusCode)

    }


    /**
     * Test the API for "update employee"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `update employee`(){

        `create employee`()
        var token = loginFun(manager)
        val responseUpdateEmployee = restTemplate.exchange("/API/employees/1",
            HttpMethod.PUT,
            HttpEntity(
                EmployeeDTO(
                    email = "john@gmail.com",
                    name = "John",
                    surname = "Smith",
                    username = "john",
                    role=RoleDTO(id = 1, name = "EXPERT")
                ),HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
                ),
            EmployeeDTO::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, responseUpdateEmployee.statusCode)
        Assertions.assertEquals("john@gmail.com", responseUpdateEmployee.body?.email)
        Assertions.assertEquals("John", responseUpdateEmployee.body?.name)
        Assertions.assertEquals("Smith", responseUpdateEmployee.body?.surname)
        Assertions.assertEquals("john", responseUpdateEmployee.body?.username)
        Assertions.assertEquals("EXPERT", responseUpdateEmployee.body?.role?.name)


    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `update a non existing employee`(){

        `create employee`()
        var token = loginFun(manager)
        val responseUpdateEmployee = restTemplate.exchange("/API/employees/100",
            HttpMethod.PUT,
            HttpEntity(
                EmployeeDTO(
                    email = "john@gmail.com",
                    name = "John",
                    surname = "Smith",
                    username = "john",
                    role=RoleDTO(id = 1, name = "EXPERT")
                ),HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }),
            ProblemDetail::class.java
        )
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateEmployee.statusCode)


    }








}

fun loginFun(user: Pair<String,String>): String? {

    // test the API for "login"
    val url = "http://TODO_IP:8081/realms/SpringBootKeycloak/protocol/openid-connect/token"
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
