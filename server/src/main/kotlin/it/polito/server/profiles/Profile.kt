import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

@Entity
@Table(name = "profiles")
class Profile {
    @Id
    @Email(message="Not a valid email address")
    var email:String=""
    @NotBlank(message="Not a valid username")
    var username:String=""
    @NotBlank(message="Not a valid name")
    var name:String=""
    @NotBlank(message="Not a valid surname")
    var surname:String=""
}