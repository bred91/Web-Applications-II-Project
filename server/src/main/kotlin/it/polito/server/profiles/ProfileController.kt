package it.polito.server.profiles
import jakarta.validation.Valid
import jakarta.validation.executable.ValidateOnExecution
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Validated
@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
class ProfileController(private val profileService: IProfileService) {



    @GetMapping("/api/profiles")
    fun getProfiles():List<ProfileDTO> {
        return profileService.getProfiles()
    }
    @GetMapping("/api/profiles/{email}")
    fun getProfile(@PathVariable email:String):ProfileDTO? {
        return profileService.getProfile(email)
    }




    @PostMapping("/api/profiles/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProfile(@Valid @RequestBody profile: ProfileDTO):Boolean {
        return profileService.createProfile(profile)
    }

    @PutMapping("/api/profiles/{email}")
    fun updateProfile(@PathVariable email:String, @Valid @RequestBody profile:ProfileDTO):Any? {

        return profileService.updateProfile(email, profile)
    }



}

