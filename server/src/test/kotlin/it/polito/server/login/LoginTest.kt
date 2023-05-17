package it.polito.server.login

import it.polito.server.employees.Employee
import it.polito.server.employees.IEmployeeRepository
import it.polito.server.products.IProductRepository
import it.polito.server.products.IPurchaseRepository
import it.polito.server.products.Product
import it.polito.server.products.Purchase
import it.polito.server.profiles.IProfileRepository
import it.polito.server.profiles.Profile
import it.polito.server.profiles.ProfileDTO
import it.polito.server.security.TokenResponse
import it.polito.server.tickets.ITicketRepository
import it.polito.server.tickets.TicketDTO
import it.polito.server.tickets.TicketService
import it.polito.server.tickets.priorities.IPriorityRepository
import it.polito.server.tickets.priorities.Priority
import it.polito.server.tickets.states.IStateRepository
import it.polito.server.tickets.states.State
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.*
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.web.client.RestTemplate
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*

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
    lateinit var priorityRepository: IPriorityRepository

    @Autowired
    lateinit var employeeRepository: IEmployeeRepository


    fun loginFun(): String? {

        // test the API for "login"
        val url = "http://144.24.191.138:8081/realms/SpringBootKeycloak/protocol/openid-connect/token"
        val restTemplate = RestTemplate()
        val headers = org.springframework.http.HttpHeaders()

        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val body =
            "grant_type=password&client_id=springboot-keycloak-client&username=${"amanda@gmail.com"}&password=${"amanda"}"

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

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `searc issue22`() {

        dataInsert()

        //val token = loginFun()

        val response = restTemplate.exchange(
            "/API/tickets",
            HttpMethod.GET,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (true) {
                        setBearerAuth("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJybWIteHN4NTVkRzV6eXVHSVJ0WjBjMmVscVp1bXpvNWFoZUx5LW1UMi1vIn0.eyJleHAiOjE2ODQzMzI3MjcsImlhdCI6MTY4NDMzMjQyNywianRpIjoiOTJiNGQ3ZGUtOGQzZS00OGZiLTk0OTUtMDI3YTVlNjYwNzQ2IiwiaXNzIjoiaHR0cDovLzE0NC4yNC4xOTEuMTM4OjgwODEvcmVhbG1zL1NwcmluZ0Jvb3RLZXljbG9hayIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJkODY5NmEyNS00MmQ2LTQ1OGYtOWQ4YS0wZTg1NWNmYzEyYWMiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJzcHJpbmdib290LWtleWNsb2FrLWNsaWVudCIsInNlc3Npb25fc3RhdGUiOiJiMGFmOGRhOC1lNmRjLTQxYzQtODQ1NC1mYjRmNmNiNWFkZDMiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIi8qIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtc3ByaW5nYm9vdGtleWNsb2FrIiwidW1hX2F1dGhvcml6YXRpb24iLCJhcHBfYWRtaW4iXX0sInJlc291cmNlX2FjY2VzcyI6eyJzcHJpbmdib290LWtleWNsb2FrLWNsaWVudCI6eyJyb2xlcyI6WyJBZG1pbiJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiYjBhZjhkYTgtZTZkYy00MWM0LTg0NTQtZmI0ZjZjYjVhZGQzIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJhZG1pbiBhZG1pbiIsInByZWZlcnJlZF91c2VybmFtZSI6ImFkbWluQGdtYWlsLmNvbSIsImdpdmVuX25hbWUiOiJhZG1pbiIsImZhbWlseV9uYW1lIjoiYWRtaW4iLCJlbWFpbCI6ImFkbWluQGdtYWlsLmNvbSJ9.X_IY2sR__3K_EGJS7KILfLIERU114fg8kgIzi2qUL5lGZHJAloZcxPKiy8tkpYjq_lr-aG1IDuemxReflpOwfJFTeyOEtHzD0NrV0rWgJujB4PBioTskFPySP1TVdlrn8sSSk-4PUu_mSwV8cXiOReykT6yEr9fDaSXaa3wDeT7bNYgNaXYJ_8a_pnPtvLH1aFZWN83J1p26Wi8IPKctI6HsUfdMcV3Dpd-ArjkU5Ku83sY7xnW-uWdxBMQRg1HzCFJJM6RKT-bYBlmSYHpvbMUdTpt7E1awTg5g5pL7Q-R-74-Dtoc8XZ7oHwfREVQyUCpva6wieUFxsxffVXD53A")
                    }
                }
            ),
            List::class.java)
        print(response.body)
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)

    }
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `create issue22`() {

        dataInsert()

        val token = loginFun()

        //print(purchaseRepository.findAll())

        // test the API for "create issue"
        val responseCreateIssue = restTemplate.exchange(
            "/API/tickets/createIssue?purchaseId=1",
            HttpMethod.POST,
            HttpEntity(
                ProfileDTO(
                    email = "baba@gmail.com",
                    username = "asd",
                    name = "John",
                    surname = "Smith"
                ),
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            TicketDTO::class.java)
        Assertions.assertEquals(HttpStatus.CREATED, responseCreateIssue.statusCode)
        Assertions.assertEquals("OPEN", responseCreateIssue.body?.state?.name)
    }

    /**
     * Insert data into the database
     */
    fun dataInsert(){
        // insert database data
        stateRepository.save(State().apply{ id = 1; name="OPEN" })
        stateRepository.save(State().apply{ id = 2; name="IN PROGRESS" })
        stateRepository.save(State().apply{ id = 3; name="RESOLVED" })
        stateRepository.save(State().apply{ id = 4; name="CLOSED" })
        stateRepository.save(State().apply{ id = 5; name="REOPENED" })

        priorityRepository.save(Priority().apply{ id = 1; name="LOW" })
        priorityRepository.save(Priority().apply{ id = 2; name="MEDIUM" })
        priorityRepository.save(Priority().apply{ id = 3; name="HIGH" })

        insertEmployee()

        profileRepository.save(Profile().apply {
            email = "baba@gmail.com";
            name = "John";
            phoneNumber = "123456789";
            surname = "Smith";
            username = "johnsmith";})

        productRepository.save(Product().apply{
            id = 1.toString();
            brand = "Apple";
            name = "iPhone 12";
        })

        purchaseRepository.save(Purchase().apply{
            id = 1;
            customer= profileRepository.findByIdOrNull("baba@gmail.com")!!;
            product = productRepository.findByIdOrNull("1")!!;
            purchaseDate = Date();
            warrantyCode = "123456789";
            expiringDate = Date("2024/12/31");
        })
    }
    fun insertEmployee(){
        employeeRepository.save(
            Employee().apply {
                id=1;
                name="bob";
                surname="Wilson";
                email="bob@gmail.com"
            }
        )
    }
}
