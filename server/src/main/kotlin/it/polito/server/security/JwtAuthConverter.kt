package it.polito.server.security

import org.springframework.context.annotation.Bean
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.stereotype.Component
import java.util.stream.Collectors
import java.util.stream.Stream


@Component
class JwtAuthConverter(private val properties: JwtAuthConverterProperties) :
    Converter<Jwt?, AbstractAuthenticationToken?> {
    private val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val authorities: Collection<GrantedAuthority> = Stream.concat(
            jwtGrantedAuthoritiesConverter.convert(jwt)!!.stream(),
            extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet())
        return JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt))
    }

    private fun getPrincipalClaimName(jwt: Jwt): String {
        var claimName: String? = JwtClaimNames.SUB
        if (properties.getPrincipalAttribute() != null) {
            claimName = properties.getPrincipalAttribute()
        }
        return jwt.getClaim(claimName)
    }

    @Bean
    fun jwtAuthenticationConverter (): JwtAuthenticationConverter {
    val converter = JwtAuthenticationConverter()
    converter.setJwtGrantedAuthoritiesConverter{
        jwt: Jwt -> jwt
            .getClaim<String>("roles")
            .split(",")
            .map{GrantedAuthority{it}}
    }
    return converter
    }

    /*private fun extractResourceRoles(jwt: Jwt): Collection<GrantedAuthority> {
        val resourceAccess = jwt.getClaim<Map<String, Any>>("resource_access")
        var resource: Map<String?, Any?>
        var resourceRoles: Collection<String>

        if (resourceAccess == null
            || resourceAccess[properties.getResourceId()] as kotlin.collections.MutableMap<kotlin.String?, kotlin.Any?>?. also {
                resource = it
            } == null || resource["roles"] as kotlin.collections.MutableCollection<kotlin.String?>?. also {
                resourceRoles = it
            } == null) {
            return setOf<Any>()
        }


        return resourceRoles.stream()
            .map<SimpleGrantedAuthority> { role: String ->
                SimpleGrantedAuthority(
                    "ROLE_$role"
                )
            }
            .collect(Collectors.toSet<SimpleGrantedAuthority>())
    }*/
}