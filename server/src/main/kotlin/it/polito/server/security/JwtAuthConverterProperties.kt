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
