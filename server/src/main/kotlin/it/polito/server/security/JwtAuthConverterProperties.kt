package it.polito.server.security

import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated


@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "jwt.auth.converter")
data class JwtAuthConverterProperties (var resourceId: String? = null, var principalAttribute: String? = null)
    /*@set:ConfigurationProperties(prefix = "jwt.auth.converter.resource-id")
    private var resourceId: String? = null
        set(value) {
            field = value
        }
    @set:ConfigurationProperties(prefix = "jwt.auth.converter.principal-attribute")
    private var principalAttribute: String? = null
        set(value) {
            field = value
        }

    fun getPrincipalAttribute() = principalAttribute
    fun getResourceId() = resourceId*/
//}