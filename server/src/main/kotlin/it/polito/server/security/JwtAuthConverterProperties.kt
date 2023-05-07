package it.polito.server.security

import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated


@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "jwt.auth.converter")
class JwtAuthConverterProperties{
    private val resourceId: String? = null
    private val principalAttribute: String? = null

    fun getPrincipalAttribute() = principalAttribute
    fun getResourceId() = resourceId
}