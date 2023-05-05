package it.polito.server.employees

import it.polito.server.products.IProductRepository
import it.polito.server.products.IPurchaseRepository
import it.polito.server.profiles.IProfileRepository
import it.polito.server.profiles.ProfileDTO
import it.polito.server.tickets.ITicketRepository
import it.polito.server.tickets.TicketService
import it.polito.server.tickets.priorities.Priority
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

        roleRepository.save(Role().apply{ id = 1; name="EXPERT" })


        val responseCreateEmployee = restTemplate.postForEntity("/API/employees/",
            EmployeeDTO(
                email = "baba@gmail.com",
                name = "John",
                surname = "Smith",
                role=RoleDTO(id = 1, name = "EXPERT")
            ), EmployeeDTO::class.java);
        Assertions.assertEquals(HttpStatus.CREATED, responseCreateEmployee.statusCode)

    }

    /**
     * Test the API for "get employee"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `get employee`(){

        `create employee`()
        val responseGetEmployee = restTemplate.getForEntity("/API/employees/1",
            EmployeeDTO::class.java);
        Assertions.assertEquals(HttpStatus.OK, responseGetEmployee.statusCode)
        Assertions.assertEquals("baba@gmail.com", responseGetEmployee.body?.email)
        Assertions.assertEquals("John", responseGetEmployee.body?.name)
        Assertions.assertEquals("Smith", responseGetEmployee.body?.surname)
        Assertions.assertEquals("EXPERT", responseGetEmployee.body?.role?.name)


    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `get a non existing employee`(){

        `create employee`()
        val responseGetEmployee = restTemplate.getForEntity("/API/employees/100",
            ProblemDetail::class.java);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetEmployee.statusCode)

    }


    /**
     * Test the API for "update employee"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `update employee`(){

        `create employee`()
        val responseUpdateEmployee = restTemplate.exchange("/API/employees/1",
            HttpMethod.PUT,
            HttpEntity(
                EmployeeDTO(
                    email = "john@gmail.com",
                    name = "John",
                    surname = "Smith",
                    role=RoleDTO(id = 1, name = "EXPERT")
                )),
            EmployeeDTO::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, responseUpdateEmployee.statusCode)
        Assertions.assertEquals("john@gmail.com", responseUpdateEmployee.body?.email)
        Assertions.assertEquals("John", responseUpdateEmployee.body?.name)
        Assertions.assertEquals("Smith", responseUpdateEmployee.body?.surname)
        Assertions.assertEquals("EXPERT", responseUpdateEmployee.body?.role?.name)


    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `update a non existing employee`(){

        `create employee`()
        val responseUpdateEmployee = restTemplate.exchange("/API/employees/100",
            HttpMethod.PUT,
            HttpEntity(
                EmployeeDTO(
                    email = "john@gmail.com",
                    name = "John",
                    surname = "Smith",
                    role=RoleDTO(id = 1, name = "EXPERT")
                )),
            ProblemDetail::class.java
        )
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateEmployee.statusCode)


    }








}