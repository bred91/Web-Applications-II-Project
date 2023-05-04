package it.polito.server.tickets

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
    @Autowired
    lateinit var profileRepository: IProfileRepository
    @Autowired
    lateinit var ticketService: TicketService
    @Autowired
    lateinit var productRepository: IProductRepository
    @Autowired
    lateinit var purchaseRepository: IPurchaseRepository

    @Test
    fun someTest() {
        // insert database data
        stateRepository.save(State().apply{ id = 1; name="OPEN" })
        stateRepository.save(State().apply{ id = 2; name="IN PROGRESS" })
        stateRepository.save(State().apply{ id = 3; name="RESOLVED" })
        stateRepository.save(State().apply{ id = 4; name="CLOSED" })
        stateRepository.save(State().apply{ id = 5; name="REOPENED" })


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

        val response = restTemplate.postForEntity("/API/tickets/createIssue",
            ProfileDTO(
                email = "baba@gmail.com",
                username = "asd",
                name = "asd",
                surname = ""), TicketDTO::class.java, 1)
        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)

        val response2 = restTemplate.getForEntity("/API/tickets/1", TicketDTO::class.java)
        Assertions.assertEquals(HttpStatus.OK, response2.statusCode)
        println(response2.body)
    }
}