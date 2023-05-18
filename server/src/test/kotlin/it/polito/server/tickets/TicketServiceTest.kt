package it.polito.server.tickets

import it.polito.server.employees.Employee
import it.polito.server.employees.IEmployeeRepository
import it.polito.server.products.IProductRepository
import it.polito.server.products.IPurchaseRepository
import it.polito.server.products.Product
import it.polito.server.products.Purchase
import it.polito.server.profiles.IProfileRepository
import it.polito.server.profiles.Profile
import it.polito.server.security.TokenResponse
import it.polito.server.tickets.priorities.IPriorityRepository
import it.polito.server.tickets.priorities.Priority
import it.polito.server.tickets.priorities.PriorityDTO
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
class TicketServiceTest {
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

        private val customer = Pair("amanda@gmail.com", "amanda")
        private val customer2 = Pair("customer1@mail.com", "customer")
        private val expert = Pair("expert@mail.com","expert")
        //private val admin = Pair("admin@gmail.com", "admin")
        private val manager = Pair("simmanager@gmail.com", "simran")
    }

    @LocalServerPort
    protected var port: Int = 8080
    @Autowired
    lateinit var restTemplate: TestRestTemplate
    @Autowired
    lateinit var stateRepository: IStateRepository
    @Autowired
    lateinit var profileRepository: IProfileRepository
    @Autowired
    lateinit var productRepository: IProductRepository
    @Autowired
    lateinit var purchaseRepository: IPurchaseRepository
    @Autowired
    lateinit var priorityRepository: IPriorityRepository
    @Autowired
    lateinit var employeeRepository: IEmployeeRepository


    /**
     * Test the API for "create issue
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `create issue`() {

        dataInsert()

        // login
        val token = loginFun(customer)
        Assertions.assertNotNull(token)

        // test the API for "create issue"
        val responseCreateIssue = restTemplate.exchange(
            "/API/tickets/createIssue?purchaseId=1",
            HttpMethod.POST,
            HttpEntity(
                null,
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

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `create issue with invalid customer`() {

        dataInsert()

        // login
        val token = loginFun(customer2)
        Assertions.assertNotNull(token)

        // test the API for "create issue"
        val responseCreateIssue = restTemplate.exchange(
            "/API/tickets/createIssue?purchaseId=1",
            HttpMethod.POST,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            ProblemDetail::class.java)
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseCreateIssue.statusCode)
        Assertions.assertEquals("Profile with email customer1@mail.com not found", responseCreateIssue.body?.detail)
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `create issue with invalid purchaseId`() {

        dataInsert()

        // login
        val token = loginFun(customer)
        Assertions.assertNotNull(token)

        // test the API for "create issue"
        val responseCreateIssue = restTemplate.exchange(
            "/API/tickets/createIssue?purchaseId=100",
            HttpMethod.POST,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            ProblemDetail::class.java)
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseCreateIssue.statusCode)
        Assertions.assertEquals("Purchase with id 100 does not exist", responseCreateIssue.body?.detail)
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `create issue with purchase not belonging to customer`() {

        dataInsert()

        profileRepository.save(Profile().apply {
            email = "customer1@mail.com";
            name = "John";
            phoneNumber = "123456789";
            surname = "Smith";
            username = "johns_mith";})

        // login
        val token = loginFun(customer2)
        Assertions.assertNotNull(token)

        // test the API for "create issue"
        val responseCreateIssue = restTemplate.exchange(
            "/API/tickets/createIssue?purchaseId=1",
            HttpMethod.POST,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            ProblemDetail::class.java)
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseCreateIssue.statusCode)
        Assertions.assertEquals("The purchase 1 does not belong to customer1@mail.com", responseCreateIssue.body?.detail)

    }



    /**
     * Test the API for "start progress"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `start progress`(){

        dataInsert()

        `create issue`() // create the ticket

        // login
        val token = loginFun(manager)
        Assertions.assertNotNull(token)

        // test the API for "start progress"
        val responseStartProgress = restTemplate.exchange(
            "/API/tickets/startProgress/1",
            HttpMethod.PUT,
            HttpEntity(
                StartProgressRequestDTO(
                    employee_id = 1,
                    priorityLevel = PriorityDTO(
                        id = 1,
                        name = "LOW"
                    )
                ),
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            TicketDTO::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, responseStartProgress.statusCode)
        Assertions.assertEquals("IN PROGRESS", responseStartProgress.body?.state?.name)
        Assertions.assertEquals(1, responseStartProgress.body?.actualExpert?.id)
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `start progress from a reopened state`(){

        dataInsert()

        employeeRepository.save(
            Employee().apply {
                id=2;
                name="sim";
                surname="rana";
                email="sim@gmail.com"
            }
        )

        `reopen issue from closed state`() //it brings the ticket in state REOPENED

        // login
        val token = loginFun(manager)
        Assertions.assertNotNull(token)

        // test the API for "start progress"
        val responseStartProgress = restTemplate.exchange(
            "/API/tickets/startProgress/1",
            HttpMethod.PUT,
            HttpEntity(
                StartProgressRequestDTO(
                    employee_id = 2,
                    priorityLevel = PriorityDTO(
                        id = 1,
                        name = "LOW"
                    )
                ),
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            TicketDTO::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, responseStartProgress.statusCode)
        Assertions.assertEquals("IN PROGRESS", responseStartProgress.body?.state?.name)
        Assertions.assertEquals(2, responseStartProgress.body?.actualExpert?.id)
    }


    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `start progress with invalid employee`(){

        dataInsert()

        // create the ticket
        `create issue`()

        // login
        val token = loginFun(manager)
        Assertions.assertNotNull(token)

        // test the API for "start progress"
        val responseStartProgress = restTemplate.exchange(
            "/API/tickets/startProgress/1",
            HttpMethod.PUT,
            HttpEntity(
                StartProgressRequestDTO(
                    employee_id = 100,
                    priorityLevel = PriorityDTO(
                        id = 1,
                        name = "LOW"
                    )
                ),
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            ProblemDetail::class.java
        )
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseStartProgress.statusCode)
        Assertions.assertEquals("Employee with id 100 doesn't exist!", responseStartProgress.body?.detail)
    }


    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `start progress with invalid ticket id`(){

        dataInsert()

        // create the ticket
        `create issue`()

        // login
        val token = loginFun(manager)
        Assertions.assertNotNull(token)

        // test the API for "start progress"
        val responseStartProgress = restTemplate.exchange(
            "/API/tickets/startProgress/100",
            HttpMethod.PUT,
            HttpEntity(
                StartProgressRequestDTO(
                    employee_id = 1,
                    priorityLevel = PriorityDTO(
                        id = 1,
                        name = "LOW"
                    )
                ),
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            ProblemDetail::class.java
        )
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseStartProgress.statusCode)
        Assertions.assertEquals("Ticket with id 100 not found!", responseStartProgress.body?.detail)
    }


    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `start progress with invalid priority level`(){

        dataInsert()

        // create the ticket
        `create issue`()

        // login
        val token = loginFun(manager)
        Assertions.assertNotNull(token)

        // test the API for "start progress"
        val responseStartProgress = restTemplate.exchange(
            "/API/tickets/startProgress/1",
            HttpMethod.PUT,
            HttpEntity(
                StartProgressRequestDTO(
                    employee_id = 1,
                    priorityLevel = PriorityDTO(
                        id = 58,
                        name = "VERY_LOW"
                    )
                ),
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            ProblemDetail::class.java
        )
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseStartProgress.statusCode)
        Assertions.assertEquals("Priority Level with id 58 doesn't exist!", responseStartProgress.body?.detail)
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `start progress from an invalid state of ticket`(){

        `close issue`() //it brings the ticket in state CLOSED

        // login
        val token = loginFun(manager)
        Assertions.assertNotNull(token)

        // test the API for "start progress"
        val responseStartProgress = restTemplate.exchange(
            "/API/tickets/startProgress/1",
            HttpMethod.PUT,
            HttpEntity(
                StartProgressRequestDTO(
                    employee_id = 1,
                    priorityLevel = PriorityDTO(
                        id = 1,
                        name = "LOW"
                    )
                ),
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            ProblemDetail::class.java
        )
        Assertions.assertEquals(HttpStatus.CONFLICT, responseStartProgress.statusCode)
        Assertions.assertEquals("Invalid Request: The ticket is in state CLOSED", responseStartProgress.body?.detail)
    }



    /**
     * Test the API for "stop progress"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `stop progress by Expert`(){

        `start progress`() // create the ticket and start the progress

        // login
        val token = loginFun(expert)
        Assertions.assertNotNull(token)

        // test the API for "stop progress"
        val responseStopProgress = restTemplate.exchange(
            "/API/tickets/stopProgress/1",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            TicketDTO::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, responseStopProgress.statusCode)
        Assertions.assertEquals("OPEN", responseStopProgress.body?.state?.name)

    }

    /**
     * Test the API for "stop progress"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `stop progress by Manager`(){

        `start progress`() // create the ticket and start the progress

        // login
        val token = loginFun(manager)
        Assertions.assertNotNull(token)

        // test the API for "stop progress"
        val responseStopProgress = restTemplate.exchange(
            "/API/tickets/stopProgress/1",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            TicketDTO::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, responseStopProgress.statusCode)
        Assertions.assertEquals("OPEN", responseStopProgress.body?.state?.name)

    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `stop progress with invalid ticket id`(){

        `start progress`() // create the ticket and start the progress

        // login
        val token = loginFun(expert)
        Assertions.assertNotNull(token)

        // test the API for "stop progress"
        val responseStopProgress = restTemplate.exchange(
            "/API/tickets/stopProgress/100",
            HttpMethod.PUT,
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
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseStopProgress.statusCode)
        Assertions.assertEquals("Ticket with id 100 not found!", responseStopProgress.body?.detail)

    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `stop progress from an invalid ticket state`(){


        `close issue`() //it brings the ticket in state CLOSED

        // login
        val token = loginFun(expert)
        Assertions.assertNotNull(token)

        // test the API for "stop progress"
        val responseStopProgress = restTemplate.exchange(
            "/API/tickets/stopProgress/1",
            HttpMethod.PUT,
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
        Assertions.assertEquals(HttpStatus.CONFLICT, responseStopProgress.statusCode)
        Assertions.assertEquals("Invalid Request: The ticket is in state CLOSED", responseStopProgress.body?.detail)
    }

    /**
     * Test the API for "resolve issue"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `resolve issue by Client`(){

        `start progress`() // create the ticket and start the progress

        val token = loginFun(customer)

        Assertions.assertNotNull(token)

        // test the API for "resolve Issue"
        val responseResolveIssue = restTemplate.exchange(
            "/API/tickets/resolveIssue/1",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            TicketDTO::class.java)

        Assertions.assertEquals(HttpStatus.OK, responseResolveIssue.statusCode)
        Assertions.assertEquals("RESOLVED", responseResolveIssue.body?.state?.name)
    }

    /**
     * Test the API for "resolve issue"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `resolve issue by Expert`(){

        `start progress`() // create the ticket and start the progress

        val token = loginFun(expert)

        Assertions.assertNotNull(token)

        // test the API for "resolve Issue"
        val responseResolveIssue = restTemplate.exchange(
            "/API/tickets/resolveIssue/1",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            TicketDTO::class.java)

        Assertions.assertEquals(HttpStatus.OK, responseResolveIssue.statusCode)
        Assertions.assertEquals("RESOLVED", responseResolveIssue.body?.state?.name)
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `resolve issue from a reopened state ticket`(){

        `reopen issue from closed state`() //it brings the ticket in state REOPENED

        val token = loginFun(customer)

        Assertions.assertNotNull(token)

        // test the API for "resolve Issue"
        val responseResolveIssue = restTemplate.exchange(
            "/API/tickets/resolveIssue/1",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            TicketDTO::class.java)

        Assertions.assertEquals(HttpStatus.OK, responseResolveIssue.statusCode)
        Assertions.assertEquals("RESOLVED", responseResolveIssue.body?.state?.name)
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `resolve issue from an invalid ticket state`(){

        `close issue`() // it brings the state in CLOSED state

        val token = loginFun(customer)

        Assertions.assertNotNull(token)

        // test the API for "resolve Issue"
        val responseResolveIssue = restTemplate.exchange(
            "/API/tickets/resolveIssue/1",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            ProblemDetail::class.java)

        Assertions.assertEquals(HttpStatus.CONFLICT, responseResolveIssue.statusCode)
        Assertions.assertEquals("Invalid Request: The ticket is in state CLOSED", responseResolveIssue.body?.detail)
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `resolve issue with invalid ticket id`(){

        `start progress`() // create the ticket and start the progress

        val token = loginFun(customer)

        Assertions.assertNotNull(token)

        // test the API for "resolve Issue"
        val responseResolveIssue = restTemplate.exchange(
            "/API/tickets/resolveIssue/100",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            ProblemDetail::class.java)

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseResolveIssue.statusCode)
        Assertions.assertEquals("Ticket with id 100 not found!", responseResolveIssue.body?.detail)
    }

    /**
     * Test the API for "close issue"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `close issue`(){

        `resolve issue by Client`() // create the ticket, start the progress and resolve the issue

        val token = loginFun(customer)

        Assertions.assertNotNull(token)

        // test the API for "close issue"
        val responseCloseIssue = restTemplate.exchange(
            "/API/tickets/closeIssue/1",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            TicketDTO::class.java)

        Assertions.assertEquals(HttpStatus.OK, responseCloseIssue.statusCode)
        Assertions.assertEquals("CLOSED", responseCloseIssue.body?.state?.name)
    }

    /**
     * Test the API for "close issue"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `close issue by Manager`(){

        `resolve issue by Client`() // create the ticket, start the progress and resolve the issue

        val token = loginFun(manager)

        Assertions.assertNotNull(token)

        // test the API for "close issue"
        val responseCloseIssue = restTemplate.exchange(
            "/API/tickets/closeIssue/1",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            TicketDTO::class.java)

        Assertions.assertEquals(HttpStatus.OK, responseCloseIssue.statusCode)
        Assertions.assertEquals("CLOSED", responseCloseIssue.body?.state?.name)
    }

    /**
     * Test the API for "close issue"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `close issue by Expert`(){

        `resolve issue by Client`() // create the ticket, start the progress and resolve the issue

        val token = loginFun(expert)

        Assertions.assertNotNull(token)

        // test the API for "close issue"
        val responseCloseIssue = restTemplate.exchange(
            "/API/tickets/closeIssue/1",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            TicketDTO::class.java)

        Assertions.assertEquals(HttpStatus.OK, responseCloseIssue.statusCode)
        Assertions.assertEquals("CLOSED", responseCloseIssue.body?.state?.name)
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `close issue from open state`(){

        `create issue`()

        val token = loginFun(customer)

        Assertions.assertNotNull(token)

        // test the API for "close issue"
        val responseCloseIssue = restTemplate.exchange(
            "/API/tickets/closeIssue/1",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            TicketDTO::class.java)

        Assertions.assertEquals(HttpStatus.OK, responseCloseIssue.statusCode)
        Assertions.assertEquals("CLOSED", responseCloseIssue.body?.state?.name)

    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `close issue from in progress state`(){

        `start progress`()

        val token = loginFun(customer)

        Assertions.assertNotNull(token)

        // test the API for "close issue"
        val responseCloseIssue = restTemplate.exchange(
            "/API/tickets/closeIssue/1",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            TicketDTO::class.java)

        Assertions.assertEquals(HttpStatus.OK, responseCloseIssue.statusCode)
        Assertions.assertEquals("CLOSED", responseCloseIssue.body?.state?.name)

    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `close issue from reopened state`(){

        `reopen issue from resolved state`()

        val token = loginFun(customer)

        Assertions.assertNotNull(token)

        // test the API for "close issue"
        val responseCloseIssue = restTemplate.exchange(
            "/API/tickets/closeIssue/1",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            TicketDTO::class.java)

        Assertions.assertEquals(HttpStatus.OK, responseCloseIssue.statusCode)
        Assertions.assertEquals("CLOSED", responseCloseIssue.body?.state?.name)

    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `close issue with invalid ticket id`(){

        `reopen issue from resolved state`()

        val token = loginFun(customer)

        Assertions.assertNotNull(token)

        // test the API for "close issue"
        val responseCloseIssue = restTemplate.exchange(
            "/API/tickets/closeIssue/100",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            ProblemDetail::class.java)

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseCloseIssue.statusCode)
        Assertions.assertEquals("Ticket with id 100 not found!", responseCloseIssue.body?.detail)

    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `close issue for a closed ticket`(){

        `close issue`()

        val token = loginFun(customer)

        Assertions.assertNotNull(token)

        // test the API for "close issue"
        val responseCloseIssue = restTemplate.exchange(
            "/API/tickets/closeIssue/1",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            ProblemDetail::class.java)

        Assertions.assertEquals(HttpStatus.CONFLICT, responseCloseIssue.statusCode)
        Assertions.assertEquals("Invalid Request: The ticket is in state CLOSED", responseCloseIssue.body?.detail)

    }



    /**
     * Test the API for "reopen issue
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `reopen issue from closed state`(){


        `close issue`() // create the ticket, start the progress, resolve the issue and close the issue

        val token = loginFun(customer)

        Assertions.assertNotNull(token)

        // test the API for "reopen issue"
        val responseReopenIssue = restTemplate.exchange(
            "/API/tickets/reopenIssue/1",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            TicketDTO::class.java)
        Assertions.assertEquals(HttpStatus.OK, responseReopenIssue.statusCode)
        Assertions.assertEquals("REOPENED", responseReopenIssue.body?.state?.name)

    }
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `reopen issue from resolved state`(){


        `resolve issue by Client`() // create the ticket, start the progress, resolve the issue and close the issue

        val token = loginFun(customer)

        Assertions.assertNotNull(token)

        // test the API for "reopen issue"
        val responseReopenIssue = restTemplate.exchange(
            "/API/tickets/reopenIssue/1",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            TicketDTO::class.java)

        Assertions.assertEquals(HttpStatus.OK, responseReopenIssue.statusCode)
        Assertions.assertEquals("REOPENED", responseReopenIssue.body?.state?.name)

    }
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `reopen issue with invalid ticket id`(){

        `close issue`() // create the ticket, start the progress, resolve the issue and close the issue

        val token = loginFun(customer)

        Assertions.assertNotNull(token)

        // test the API for "reopen issue"
        val responseReopenIssue = restTemplate.exchange(
            "/API/tickets/reopenIssue/100",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            ProblemDetail::class.java)

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseReopenIssue.statusCode)
        Assertions.assertEquals("Ticket with id 100 not found!", responseReopenIssue.body?.detail)

    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `reopen issue from an invalid ticket state`(){

        `create issue`() // it creates a ticket

        val token = loginFun(customer)

        Assertions.assertNotNull(token)

        // test the API for "reopen issue"
        val responseReopenIssue = restTemplate.exchange(
            "/API/tickets/reopenIssue/1",
            HttpMethod.PUT,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            ProblemDetail::class.java)

        Assertions.assertEquals(HttpStatus.CONFLICT, responseReopenIssue.statusCode)
        Assertions.assertEquals("Invalid Request: The ticket is in state OPEN", responseReopenIssue.body?.detail)

    }

    /**
     * Test for the API for getting the ticket
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `get ticket by id`() {

        `create issue`()

        val token = loginFun(manager)

        Assertions.assertNotNull(token)

        // test the API for getting the ticket
        val responseGetTicket = restTemplate.exchange(
            "/API/tickets/1",
            HttpMethod.GET,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            TicketDTO::class.java)

        Assertions.assertEquals(HttpStatus.OK, responseGetTicket.statusCode)
        Assertions.assertEquals("OPEN", responseGetTicket.body?.state?.name)
        Assertions.assertEquals(customer.first, responseGetTicket.body?.customer?.email)

    }

    /**
     * Test for the API for getting the ticket
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `get ticket by id not found`() {

        `create issue`()

        val token = loginFun(manager)

        Assertions.assertNotNull(token)

        // test the API for getting the ticket
        val responseGetTicket = restTemplate.exchange(
            "/API/tickets/100",
            HttpMethod.GET,
            HttpEntity(
                null,
                HttpHeaders().apply {
                    if (token != null) {
                        setBearerAuth(token)
                    }
                }
            ),
            ProblemDetail::class.java)

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetTicket.statusCode)
        Assertions.assertEquals("Ticket with id 100 not found", responseGetTicket.body?.detail)

    }







    /**
     * Test for the API for login through keycloak
     * This function is very similar to the one into the Security Package,
     * but with a different return type that makes it easier to test
     */
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

        insertCustomer()

        productRepository.save(Product().apply{
            id = 1.toString();
            brand = "Apple";
            name = "iPhone 12";
        })

        purchaseRepository.save(Purchase().apply{
            id = 1;
            customer= profileRepository.findByIdOrNull("amanda@gmail.com")!!;
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
                name="Expert";
                surname="Very";
                email="expert@mail.com"
            }
        )
    }

    fun insertCustomer(){
        profileRepository.save(Profile().apply {
            email = "amanda@gmail.com";
            name = "Amanda";
            phoneNumber = "123456789";
            surname = "Smith";
            username = "johnsmith";})
    }
}