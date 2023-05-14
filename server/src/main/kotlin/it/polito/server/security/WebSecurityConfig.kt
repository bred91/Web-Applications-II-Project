package it.polito.server.security


import lombok.RequiredArgsConstructor
import org.keycloak.adapters.springsecurity.authentication.KeycloakLogoutHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.ClientRegistrations
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class WebSecurityConfig @Autowired constructor(private val jwtAuthConverter: JwtAuthConverter){
    //private val jwtAuthConverter: JwtAuthConverter? = null

    @Bean
    fun clientRepository():ClientRegistrationRepository  {

        val keycloak = keycloakClientRegistration()
        return InMemoryClientRegistrationRepository(keycloak);
    }


    @Bean
    fun keycloakClientRegistration():ClientRegistration {
        return ClientRegistration.withRegistrationId("SpringBootKeycloak")
            .clientId("springboot-keycloak-client")
            .clientSecret("--generated--")
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .issuerUri("http://144.24.191.138:8081/realms/SpringBootKeycloak")
            .authorizationUri("http://144.24.191.138:8081/realms/SpringBootKeycloak/protocol/openid-connect/auth")
            .userInfoUri("http://144.24.191.138:8081/realms/SpringBootKeycloak/protocol/openid-connect/userinfo")
            .tokenUri("http://144.24.191.138:8081/realms/SpringBootKeycloak/protocol/openid-connect/token")
            .build()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests().requestMatchers( HttpMethod.POST, "/login").permitAll()
            .requestMatchers(HttpMethod.GET, "/").permitAll()
            .requestMatchers(HttpMethod.GET, "/manager").hasRole(MANAGER)
            .requestMatchers(HttpMethod.GET, "/expert").hasAnyRole(MANAGER, EXPERT)
            .requestMatchers(HttpMethod.GET, "/customer").hasAnyRole(CLIENT)
            .anyRequest().permitAll().and().oauth2Login()


        http.oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthConverter)

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.csrf().disable()
        return http.build()

    }
    companion object {
        const val MANAGER = "Manager"
        const val EXPERT = "Expert"
        const val CLIENT = "Client"
    }
}