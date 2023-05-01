package it.polito.server

import io.mockk.*
import it.polito.server.profiles.*
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull



import it.polito.server.profiles.exception.DuplicateProfileException
import it.polito.server.profiles.exception.ProfileNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach


class ProfileServiceTest {

    private lateinit var profileRepository: IProfileRepository
    private lateinit var profileService: ProfileService

    @BeforeEach
    fun setUp() {
        profileRepository = mockk<IProfileRepository>()
        profileService = ProfileService(profileRepository)
    }

    @Test
    fun `getProfiles should return a list of ProfileDTOs`() {
        // Arrange
        val profile_1 = Profile()
        val profile_2 = Profile()
        profile_1.email = "test1@test.com"
        profile_2.email = "test2@test.com"
        val profiles = listOf(profile_1, profile_2)
        every { profileRepository.findAll() } returns profiles

        // Act
        val result = profileService.getProfiles()

        // Assert
        assertEquals(profiles.size, result.size)
        assertEquals(profiles[0].email, result[0].email)
        assertEquals(profiles[1].email, result[1].email)
    }

    @Test
    fun `getProfile should return a ProfileDTO if the profile exists`() {
        // Arrange
        val email = "test@test.com"
        val profile = Profile()
        profile.email = email
        every { profileRepository.findByIdOrNull(email) } returns profile

        // Act
        val result = profileService.getProfile(email)

        // Assert
        assertEquals(profile.email, result!!.email)
    }

    @Test
    fun `getProfile should throw a ProfileNotFoundException if the profile does not exist`() {
        // Arrange
        val email = "test@test.com"
        every { profileRepository.findByIdOrNull(email) } returns null

        // Act & Assert
        assertThrows(ProfileNotFoundException::class.java) { profileService.getProfile(email) }
    }

    @Test
    fun `createProfile should save the profile if it does not exist`() {
        // Arrange
        val email = "test@test.com"
        val profileDTO = ProfileDTO(email = email, name = "", username = "", surname = "")
        val profile = Profile()
        profile.email = email
        every { profileRepository.existsById(email) } returns false
        every { profileRepository.save(any<Profile>()) } returns profile

        // Act
        profileService.createProfile(profileDTO)

        // Assert
        verify { profileRepository.save(any<Profile>()) }
    }

    @Test
    fun `createProfile should throw a DuplicateProfileException if the profile already exists`() {
        // Arrange
        val email = "test@test.com"
        val profileDTO = ProfileDTO(email = email, name = "", surname = "", username = "")
        every { profileRepository.existsById(email) } returns true

        // Act & Assert
        assertThrows(DuplicateProfileException::class.java) { profileService.createProfile(profileDTO) }
    }


    @Test
    fun `updateProfile should update the profile if it exists`() {
        // Arrange
        val email = "test@test.com"
        val profileDTO = ProfileDTO(email = email, name = "John", surname = "Doe", username = "")

        every {
            profileRepository.findByIdOrNull(email)?.toDTO()
        } returns ProfileDTO(
            email = email,
            name = "John",
            surname = "",
            username = ""
        )

        every {
            profileRepository.save(any<Profile>())
        } returns profileDTO.toEntity()

        // Act
        val result = profileService.updateProfile(email, profileDTO)

        // Assert
        verify { profileRepository.save(any<Profile>()) }
        assertEquals(profileDTO.name, result?.name)
        assertEquals(profileDTO.surname, result?.surname)
    }




}
