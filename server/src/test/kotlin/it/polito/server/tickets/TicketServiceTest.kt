package it.polito.server.tickets

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
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

    @Test
    fun someTest() {
        //val response = restTemplate.getForEntity("/API/tickets/1", TicketDTO::class.java)
        stateRepository.save(State().apply{ id = 1; name="OPEN" })
        val response = restTemplate.postForEntity("/API/tickets/", TicketDTO(
            creationDate = Date(),
            lastModification = Date(),
        ), TicketDTO::class.java)
        //val response = restTemplate.getForEntity("/API/tickets/1", TicketDTO::class.java)
        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
        val response2 = restTemplate.getForEntity("/API/tickets/1", TicketDTO::class.java)
        Assertions.assertEquals(HttpStatus.OK, response2.statusCode)
        println(response2.body)
    }
}