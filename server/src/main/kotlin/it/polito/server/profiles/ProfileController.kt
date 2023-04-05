package it.polito.server.profiles
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Validated
@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
class ProfileController(private val profileService: IProfileService) {



    @GetMapping("/API/profiles")
    fun getProfiles():List<ProfileDTO> {
        return profileService.getProfiles()
    }
    @GetMapping("/API/profiles/{email}")
    fun getProfile(@PathVariable email:String):ProfileDTO? {
        return profileService.getProfile(email)
    }




    @PostMapping("/API/profiles/")
    @ResponseStatus(HttpStatus.CREATED)
    // todo cambiare boolean
    fun createProfile(@Valid @RequestBody profile: ProfileDTO):Boolean {
        return profileService.createProfile(profile)
    }

    @PutMapping("/API/profiles/{email}")
    fun updateProfile(@PathVariable email:String, @Valid @RequestBody profile:ProfileDTO):Any? {

        return profileService.updateProfile(email, profile)
    }



}


