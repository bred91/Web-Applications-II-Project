package it.polito.server.tickets

import java.io.File
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import it.polito.server.employees.Employee
import it.polito.server.employees.IEmployeeRepository
import it.polito.server.products.IProductRepository
import it.polito.server.products.IPurchaseRepository
import it.polito.server.products.Product
import it.polito.server.products.Purchase
import it.polito.server.profiles.IProfileRepository
import it.polito.server.profiles.Profile
import it.polito.server.security.TokenResponse
import it.polito.server.tickets.messages.MessageDTO
import it.polito.server.tickets.priorities.IPriorityRepository
import it.polito.server.tickets.priorities.Priority
import it.polito.server.tickets.priorities.PriorityDTO
import it.polito.server.tickets.states.IStateRepository
import it.polito.server.tickets.states.State
import org.junit.experimental.theories.internal.ParameterizedAssertionError
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.core.io.FileSystemResource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.*
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.lang.reflect.Array
import java.util.*

@Testcontainers
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class MessageServiceTest {
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
    fun `create message`() {
        dataInsert()
        `create issue`()
        // login
        val token = loginFun(customer)
        Assertions.assertNotNull(token)

        // Create a random file
        val fileContent = "Random file content"
        val tempFile = File.createTempFile("test", ".txt")
        tempFile.deleteOnExit()
        Files.write(tempFile.toPath(), fileContent.toByteArray(), StandardOpenOption.WRITE)

        // Create form data with the file and text parts
        val filePart = FileSystemResource(tempFile)
        val textPart = "Some message text"
        val formData = LinkedMultiValueMap<String, Any>()
        formData.add("file", filePart)
        formData.add("text", textPart)

        // Create request headers and include the bearer token
        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA
        if (token != null) {
            headers.setBearerAuth(token)
        }

        // Test the API for "create message"
        val responseCreateMessage = restTemplate.exchange(
            "/chat/1/messages",
            HttpMethod.POST,
            HttpEntity(formData, headers),
            MessageDTO::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, responseCreateMessage.statusCode)
    }


    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `create message unauthorized user`() {
        dataInsert()
        `create issue`()

        // Create a random file
        val fileContent = "Random file content"
        val tempFile = File.createTempFile("test", ".txt")
        tempFile.deleteOnExit()
        Files.write(tempFile.toPath(), fileContent.toByteArray(), StandardOpenOption.WRITE)

        // Create form data with the file and text parts
        val filePart = FileSystemResource(tempFile)
        val textPart = "Some message text"
        val formData = LinkedMultiValueMap<String, Any>()
        formData.add("file", filePart)
        formData.add("text", textPart)

        // Create request headers and include the bearer token
        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA


        // Test the API for "create message"
        val responseCreateMessage = restTemplate.exchange(
            "/chat/1/messages",
            HttpMethod.POST,
            HttpEntity(formData, headers),
            ProblemDetail::class.java
        )
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseCreateMessage.statusCode)
    }







    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `create message by a forbidden user`() {
        dataInsert()
        `create issue`()
        // login
        val token = loginFun(customer2)
        Assertions.assertNotNull(token)

        // Create a random file
        val fileContent = "Random file content"
        val tempFile = File.createTempFile("test", ".txt")
        tempFile.deleteOnExit()
        Files.write(tempFile.toPath(), fileContent.toByteArray(), StandardOpenOption.WRITE)

        // Create form data with the file and text parts
        val filePart = FileSystemResource(tempFile)
        val textPart = "Some message text"
        val formData = LinkedMultiValueMap<String, Any>()
        formData.add("file", filePart)
        formData.add("text", textPart)

        // Create request headers and include the bearer token
        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA
        if (token != null) {
            headers.setBearerAuth(token)
        }

        // Test the API for "create message"
        val responseCreateMessage = restTemplate.exchange(
            "/chat/1/messages",
            HttpMethod.POST,
            HttpEntity(formData, headers),
            ProblemDetail::class.java
        )
        Assertions.assertEquals(HttpStatus.FORBIDDEN, responseCreateMessage.statusCode)
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `create message non existing ticket`() {
        dataInsert()
        `create issue`()
        // login
        val token = loginFun(customer)
        Assertions.assertNotNull(token)

        // Create a random file
        val fileContent = "Random file content"
        val tempFile = File.createTempFile("test", ".txt")
        tempFile.deleteOnExit()
        Files.write(tempFile.toPath(), fileContent.toByteArray(), StandardOpenOption.WRITE)

        // Create form data with the file and text parts
        val filePart = FileSystemResource(tempFile)
        val textPart = "Some message text"
        val formData = LinkedMultiValueMap<String, Any>()
        formData.add("file", filePart)
        formData.add("text", textPart)

        // Create request headers and include the bearer token
        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA
        if (token != null) {
            headers.setBearerAuth(token)
        }

        // Test the API for "create message"
        val responseCreateMessage = restTemplate.exchange(
            "/chat/100000/messages",
            HttpMethod.POST,
            HttpEntity(formData, headers),
            ProblemDetail::class.java
        )
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseCreateMessage.statusCode)
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `get message unauthorized user`() {
        dataInsert()
       `create message`()

        // Create request headers and include the bearer token
        val headers = HttpHeaders()



        val response = restTemplate.exchange(
            "/chat/1/messages",
            HttpMethod.GET,
            HttpEntity( null, headers ),
            ProblemDetail::class.java
        )

        // Verify the response
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)


    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `get message forbidden user`() {
        dataInsert()
        `create message`()
        val token = loginFun(customer2)
        Assertions.assertNotNull(token)
        // Create request headers and include the bearer token
        val headers = HttpHeaders()
        if (token != null) {
            headers.setBearerAuth(token)
        }


        val response = restTemplate.exchange(
            "/chat/1/messages",
            HttpMethod.GET,
            HttpEntity( null, headers ),
            ProblemDetail::class.java
        )

        // Verify the response
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.statusCode)


    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `get message non existing ticket`() {
        dataInsert()
        `create message`()
        val token = loginFun(customer)
        Assertions.assertNotNull(token)
        // Create request headers and include the bearer token
        val headers = HttpHeaders()
        if (token != null) {
            headers.setBearerAuth(token)
        }


        val response = restTemplate.exchange(
            "/chat/10000/messages",
            HttpMethod.GET,
            HttpEntity( null, headers ),
            ProblemDetail::class.java
        )

        // Verify the response
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode)


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