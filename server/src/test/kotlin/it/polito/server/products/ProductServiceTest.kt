package it.polito.server.products

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

/*
* NOTE:
* Although not explicitly required in the Lab 02 specification,
* we wrote a series of tests to ensure the proper functioning
* of the methods of the service classes,
* following a test-driven approach.
 */

class ProductServiceTest {

    @Test
    fun `get all products`() {
        // Arrange
        val repo = mockk<IProductRepository>()
        every { repo.findAll() } answers {
            listOf(
                Product().apply {
                    this.id = "ean1"
                    this.name = "name1"
                    this.brand = "brand1"
                },
                Product().apply {
                    this.id = "ean2"
                    this.name = "name2"
                    this.brand = "brand2"
                }
            )
        }

        val service = ProductService(repo)

        // Act
        val dtos = service.getAll()

        // Assert
        assert(dtos.size == 2)
        assertEquals(dtos[0].ean, "ean1")
        assertEquals(dtos[0].name, "name1")
        assertEquals(dtos[0].brand, "brand1")
        assertEquals(dtos[1].ean, "ean2")
        assertEquals(dtos[1].name, "name2")
        assertEquals(dtos[1].brand, "brand2")
    }

    @Test
    fun `search a product from its ean`() {
        // Arrange
        val repo = mockk<IProductRepository>()
        every { repo.findByIdOrNull("ean")} answers {
            Product().apply {
                this.id = "ean"
                this.name = "name"
                this.brand = "brand"
            }
        }

        val service = ProductService(repo)

        // Act
        val dto = service.getProduct("ean")

        // Assert
        assert(dto != null)
        assertEquals(dto?.ean, "ean")
        assertEquals(dto?.name, "name")
        assertEquals(dto?.brand, "brand")
    }


    /*@Test
    fun `create (add) a new product`() {
        // Arrange
        val repo = mockk<IProductRepository>()
        every { repo.existsById("ean")} answers { false }
        every { repo.save(any()) } answers { firstArg() }

        val service = ProductService(repo)

        // Act
        service.createProduct(ProductDTO("ean", "name", "brand"))

        // Assert
    }*/

    @Test
    fun `update a product`() {
        // Arrange
        val repo = mockk<IProductRepository>()
        every { repo.findByIdOrNull("ean")} answers {
            Product().apply {
                this.id = "ean"
                this.name = "name"
                this.brand = "brand"
            }
        }
        every { repo.save(any()) } answers { firstArg() }

        val service = ProductService(repo)

        // Act
        val dto = service.updateProduct("ean", ProductDTO("ean", "name2", "brand2"))

        // Assert
        assert(dto != null)
        assertEquals(dto?.ean, "ean")
        assertEquals(dto?.name, "name2")
        assertEquals(dto?.brand, "brand2")
    }
}