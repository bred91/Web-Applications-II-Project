package it.polito.server.tickets

import it.polito.server.employees.Employee
import it.polito.server.employees.IEmployeeRepository
import it.polito.server.products.IProductRepository
import it.polito.server.products.IPurchaseRepository
import it.polito.server.products.Product
import it.polito.server.products.Purchase
import it.polito.server.profiles.IProfileRepository
import it.polito.server.profiles.Profile
import it.polito.server.profiles.ProfileDTO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
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


    /**
     * Test the API for "create issue
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `create issue`() {

        dataInsert()

        // test the API for "create issue"
        val responseCreateIssue = restTemplate.postForEntity("/API/tickets/createIssue?purchaseId=1",
            ProfileDTO(
                email = "baba@gmail.com",
                username = "asd",
                name = "asd",
                surname = "ejhfkj"), TicketDTO::class.java, 1)
        Assertions.assertEquals(HttpStatus.CREATED, responseCreateIssue.statusCode)
        Assertions.assertEquals("OPEN", responseCreateIssue.body?.state?.name)

        println(responseCreateIssue.body)
    }

    /**
     * Test the API for "start progress"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `start progress`(){

        dataInsert()

        // create the ticket
        val responseCreateIssue = restTemplate.postForEntity("/API/tickets/createIssue?purchaseId=1",
            ProfileDTO(
                email = "baba@gmail.com",
                username = "asd",
                name = "asd",
                surname = "ejhfkj"), TicketDTO::class.java, 1)
        Assertions.assertEquals(HttpStatus.CREATED, responseCreateIssue.statusCode)

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
                )
            ),
            TicketDTO::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, responseStartProgress.statusCode)
        Assertions.assertEquals("IN PROGRESS", responseStartProgress.body?.state?.name)

        println(responseStartProgress.body)
    }

    /**
     * Test the API for "stop progress"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `stop progress`(){

        dataInsert()

        `start progress`() // create the ticket and start the progress

        // test the API for "stop progress"
        val responseStopProgress = restTemplate.exchange(
            "/API/tickets/stopProgress/1",
            HttpMethod.PUT,
            null,
            TicketDTO::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, responseStopProgress.statusCode)
        Assertions.assertEquals("OPEN", responseStopProgress.body?.state?.name)

        println(responseStopProgress.body)
    }

    /**
     * Test the API for "resolve issue"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `resolve issue`(){

        dataInsert()

        `start progress`() // create the ticket and start the progress

        // test the API for "resolve Issue"
        val responseResolveIssue = restTemplate.exchange(
            "/API/tickets/resolveIssue/1",
            HttpMethod.PUT,
            null,
            TicketDTO::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, responseResolveIssue.statusCode)
        Assertions.assertEquals("RESOLVED", responseResolveIssue.body?.state?.name)

        println(responseResolveIssue.body)
    }

    /**
     * Test the API for "close issue"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `close issue`(){

        dataInsert()

        `resolve issue`() // create the ticket, start the progress and resolve the issue

        // test the API for "close issue"
        val responseCloseIssue = restTemplate.exchange(
            "/API/tickets/closeIssue/1",
            HttpMethod.PUT,
            null,
            TicketDTO::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, responseCloseIssue.statusCode)
        Assertions.assertEquals("CLOSED", responseCloseIssue.body?.state?.name)

        println(responseCloseIssue.body)
    }

    /**
     * Test the API for "reopen issue
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `reopen issue`(){

        dataInsert()

        `close issue`() // create the ticket, start the progress, resolve the issue and close the issue

        // test the API for "reopen issue"
        val responseReopenIssue = restTemplate.exchange(
            "/API/tickets/reopenIssue/1",
            HttpMethod.PUT,
            null,
            TicketDTO::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, responseReopenIssue.statusCode)
        Assertions.assertEquals("REOPENED", responseReopenIssue.body?.state?.name)

        println(responseReopenIssue.body)
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