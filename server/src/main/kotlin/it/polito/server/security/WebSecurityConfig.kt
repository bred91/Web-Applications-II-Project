package it.polito.server.security


import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class WebSecurityConfig @Autowired constructor(private val jwtAuthConverter: JwtAuthConverter){


    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests()
            .requestMatchers(HttpMethod.POST, "/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/signup").permitAll()
            .requestMatchers(HttpMethod.POST, "/logout2").permitAll()
            .requestMatchers(HttpMethod.GET, "/").permitAll()
            .requestMatchers(HttpMethod.POST, "/createExpert").hasRole(MANAGER)
            .requestMatchers(HttpMethod.GET, "/manager").hasAnyRole(MANAGER)
            .requestMatchers(HttpMethod.GET, "/expert").hasAnyRole(MANAGER, EXPERT)
            .requestMatchers(HttpMethod.GET, "/customer").hasAnyRole(CLIENT)
            .requestMatchers(HttpMethod.GET, "/ws").hasAnyRole(CLIENT, MANAGER, EXPERT)
            .anyRequest().permitAll()

        http.oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthConverter)

        http.csrf().disable()
        return http.build()
    }

    companion object {
        const val MANAGER = "Manager"
        const val EXPERT = "Expert"
        const val CLIENT = "Client"
    }
}