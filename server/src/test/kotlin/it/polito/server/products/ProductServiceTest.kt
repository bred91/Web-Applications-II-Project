package it.polito.server.products
import it.polito.server.profiles.IProfileRepository
import it.polito.server.profiles.Profile
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
import java.util.*


@Testcontainers
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class ProductServiceTest {

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
     * Test the API for "create product"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `create product`(){

        val responseCreateProduct = restTemplate.postForEntity("/API/products",
            ProductDTO(
                ean = "1", "IPhone12", "Apple"),
            ProductDTO::class.java
            );
        Assertions.assertEquals(HttpStatus.CREATED, responseCreateProduct.statusCode)

    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `create a duplicate product`(){

        `create product`()

        val responseCreateProduct = restTemplate.postForEntity("/API/products",
            ProductDTO(
                ean = "1", "IPhone12", "Apple"),
            ProblemDetail::class.java
        );
        Assertions.assertEquals(HttpStatus.CONFLICT, responseCreateProduct.statusCode)

    }

    /**
     * Test the API for "get product"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `get product`(){

        `create product`()
        val responseGetProduct = restTemplate.getForEntity("/API/products/1",
            ProductDTO::class.java);
        Assertions.assertEquals(HttpStatus.OK, responseGetProduct.statusCode)
        Assertions.assertEquals("1", responseGetProduct.body?.ean)
        Assertions.assertEquals("IPhone12", responseGetProduct.body?.name)
        Assertions.assertEquals("Apple", responseGetProduct.body?.brand)


    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `get product non existing`(){

        val responseGetProduct = restTemplate.getForEntity("/API/products/1",
            ProblemDetail::class.java);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetProduct.statusCode)

    }
    /**
     * Test the API for "update product"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `update product`(){

        `create product`()
        val responseUpdateProduct = restTemplate.exchange("/API/products/1",
            HttpMethod.PUT,
            HttpEntity(
                ProductDTO(ean = "1", "IPhone 12", "Apple")),
            ProductDTO::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, responseUpdateProduct.statusCode)
        Assertions.assertEquals("1", responseUpdateProduct.body?.ean)
        Assertions.assertEquals("IPhone 12", responseUpdateProduct.body?.name)
        Assertions.assertEquals("Apple", responseUpdateProduct.body?.brand)


    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `update a non existing product`(){
        val responseUpdateProduct = restTemplate.exchange("/API/products/100",
            HttpMethod.PUT,
            HttpEntity(
                ProductDTO(ean = "100", "IPhone 12", "Apple")),
            ProblemDetail::class.java
        )
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateProduct.statusCode)

    }

    /**
     * Test the API for "create purchase"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `create purchase`(){
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

        val responseGetProduct = restTemplate.getForEntity("/API/products/1",
            ProductDTO::class.java);

        val responseCreatePurchase = restTemplate.postForEntity("/API/purchases/profiles/baba@gmail.com/products/1",
            PurchaseDTO(
                        id=1,
                        customerEmail = "baba@gmail.com",
                        product = responseGetProduct.body!!,
                        purchaseDate = Date(),
                        warrantyCode = "123456789",
                        expiringDate = Date("2024/12/31")),
            PurchaseDTO::class.java
        );
        Assertions.assertEquals(HttpStatus.CREATED, responseCreatePurchase.statusCode)

    }

    /**
     * Test the API for "create purchase"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `create purchase with a non existing customer`(){


        productRepository.save(Product().apply{
            id = 1.toString();
            brand = "Apple";
            name = "iPhone 12";
        })

        val responseGetProduct = restTemplate.getForEntity("/API/products/1",
            ProductDTO::class.java);

        val responseCreatePurchase = restTemplate.postForEntity("/API/purchases/profiles/baba@gmail.com/products/1",
            PurchaseDTO(
                id=1,
                customerEmail = "baba@gmail.com",
                product = responseGetProduct.body!!,
                purchaseDate = Date(),
                warrantyCode = "123456789",
                expiringDate = Date("2024/12/31")),
            ProblemDetail::class.java
        );
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseCreatePurchase.statusCode)

    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `create purchase with a non existing product`(){
        profileRepository.save(Profile().apply {
            email = "baba@gmail.com";
            name = "John";
            phoneNumber = "123456789";
            surname = "Smith";
            username = "johnsmith";})


        val responseCreatePurchase = restTemplate.postForEntity("/API/purchases/profiles/baba@gmail.com/products/1",
            PurchaseDTO(
                id=1,
                customerEmail = "baba@gmail.com",
                product = ProductDTO(
                    ean = "1", "IPhone12", "Apple"),
                purchaseDate = Date(),
                warrantyCode = "123456789",
                expiringDate = Date("2024/12/31")),
            ProblemDetail::class.java
        );
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseCreatePurchase.statusCode)

    }

    /**
     * Test the API for "get purchase"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `get purchase`(){
        `create purchase`()

        val responseGetPurchase = restTemplate.getForEntity("/API/purchases/1",
            PurchaseDTO::class.java
        );
        Assertions.assertEquals(HttpStatus.OK, responseGetPurchase.statusCode)
        Assertions.assertEquals("baba@gmail.com", responseGetPurchase.body?.customerEmail)
        Assertions.assertEquals("1", responseGetPurchase.body?.product?.ean)
        Assertions.assertEquals("123456789", responseGetPurchase.body?.warrantyCode)


    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `get a non existing purchase`(){

        val responseGetPurchase = restTemplate.getForEntity("/API/purchases/100",
            ProblemDetail::class.java
        );
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetPurchase.statusCode)


    }

    /**
     * Test the API for "update purchase"
     */
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `update purchase`(){
        `create purchase`()

        val responseUpdatePurchase = restTemplate.exchange("/API/purchases/1",
            HttpMethod.PUT,
            HttpEntity(PurchaseDTO(
                id=1,
                customerEmail = "baba@gmail.com",
                product = ProductDTO(
                    ean = "1", "IPhone12", "Apple"),
                purchaseDate = Date(),
                warrantyCode = "123456787",
                expiringDate = Date("2024/12/31"))),
            PurchaseDTO::class.java
        );
        Assertions.assertEquals(HttpStatus.OK, responseUpdatePurchase.statusCode)
        Assertions.assertEquals("baba@gmail.com", responseUpdatePurchase.body?.customerEmail)
        Assertions.assertEquals("1", responseUpdatePurchase.body?.product?.ean)
        Assertions.assertEquals("123456787", responseUpdatePurchase.body?.warrantyCode)


    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `update a non existing purchase `(){
        `create purchase`()

        val responseUpdatePurchase = restTemplate.exchange("/API/purchases/100",
            HttpMethod.PUT,
            HttpEntity(PurchaseDTO(
                id=100,
                customerEmail = "baba@gmail.com",
                product = ProductDTO(
                    ean = "1", "IPhone12", "Apple"),
                purchaseDate = Date(),
                warrantyCode = "123456787",
                expiringDate = Date("2024/12/31"))),
            ProblemDetail::class.java
        );
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdatePurchase.statusCode)

    }





}