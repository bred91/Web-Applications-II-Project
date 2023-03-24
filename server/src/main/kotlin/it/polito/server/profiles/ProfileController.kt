package it.polito.server.profiles
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfileController(private val profileService: IProfileService) {

    @GetMapping("/api/profiles/{email}")
    fun getProfile(@PathVariable email:String):ProfileDTO? {
        return profileService.getProfile(email)
    }

    @PostMapping("/api/profiles/")
    fun createProfile(@RequestBody profile: ProfileDTO):Boolean {
        return profileService.createProfile(profile)
    }

    @PutMapping("/api/profiles/{email}")
    fun updateProfile(@PathVariable email:String, @RequestBody profile:ProfileDTO):Any? {

        return profileService.updateProfile(email, profile)
    }

}


