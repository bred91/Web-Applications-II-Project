package it.polito.server.profiles

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

class ProfileServiceTest {

    @Test
    fun `get all profiles`() {
        // Arrange
        val repo = mockk<IProfileRepository>()
        every { repo.findAll() } answers {
            listOf(
                Profile().apply {
                    this.email = "email1"
                    this.username = "username1"
                    this.name = "name1"
                    this.surname = "surname1"
                },
                Profile().apply {
                    this.email = "email2"
                    this.username = "username2"
                    this.name = "name2"
                    this.surname = "surname2"
                }
            )
        }

        val service = ProfileService(repo)

        // Act
        val dtos = service.getProfiles()

        // Assert
        assert(dtos.size == 2)
        assertEquals(dtos[0].email, "email1")
        assertEquals(dtos[0].username, "username1")
        assertEquals(dtos[0].name, "name1")
        assertEquals(dtos[0].surname, "surname1")
        assertEquals(dtos[1].email, "email2")
        assertEquals(dtos[1].username, "username2")
        assertEquals(dtos[1].name, "name2")
        assertEquals(dtos[1].surname, "surname2")
    }

    @Test
    fun `search a profile from its email`() {
        // Arrange
        val repo = mockk<IProfileRepository>()
        every {repo.findByIdOrNull("email")} answers {
            Profile().apply{
                this.email = "email"
                this.username = "username"
                this.name = "name"
                this.surname = "surname"
            }
        }

        val service = ProfileService(repo)

        // Act
        val dto = service.getProfile("email")

        // Assert
        assert(dto != null)
        assertEquals(dto?.email, "email")
        assertEquals(dto?.username, "username")
        assertEquals(dto?.name, "name")
        assertEquals(dto?.surname, "surname")
    }

    @Test
    fun `create (add) a new profile`() {
        // Arrange
        val repo = mockk<IProfileRepository>()
        every { repo.existsById("email")} answers {false}
        every { repo.save(any()) } answers { firstArg() }

        val service = ProfileService(repo)

        // Act
        service.createProfile(ProfileDTO("email", "username", "name", "surname"))

        // Assert
        // TODO: how to assert that the save method has been called?
        // no assert needed, the mockk will throw an exception if the save method is not called

    }

    @Test
    fun `update a profile`() {
        // Arrange
        val repo = mockk<IProfileRepository>()
        every { repo.findByIdOrNull("email")} answers {
            Profile().apply {
                this.email = "email"
                this.username = "username"
                this.name = "name"
                this.surname = "surname"
            }
        }
        every { repo.save(any()) } answers { firstArg() }

        val service = ProfileService(repo)

        // Act
        val dto = service.updateProfile("email",ProfileDTO("email", "username", "name", "surname"))

        // Assert
        assert(dto != null)
        assertEquals(dto?.email, "email")
        assertEquals(dto?.username, "username")
        assertEquals(dto?.name, "name")
        assertEquals(dto?.surname, "surname")
    }
}