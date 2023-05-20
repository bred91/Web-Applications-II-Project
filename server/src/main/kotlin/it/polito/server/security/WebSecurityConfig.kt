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
//private val jwtAuthConverter: JwtAuthConverter? = null

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests()
            .requestMatchers(HttpMethod.POST, "/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/signup").permitAll()
            .requestMatchers(HttpMethod.GET, "/").permitAll()
            .requestMatchers(HttpMethod.GET, "/manager").hasAnyRole(MANAGER)
            .requestMatchers(HttpMethod.GET, "/expert").hasAnyRole(MANAGER, EXPERT)
            .requestMatchers(HttpMethod.GET, "/customer").hasAnyRole(CLIENT)
            .anyRequest().permitAll()
            //.and().oauth2Login()

        http.oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthConverter)

        //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.csrf().disable()

        //http.formLogin().permitAll()
        return http.build()
    }

    companion object {
        //const val ADMIN = "Admin"
        const val MANAGER = "Manager"
        const val EXPERT = "Expert"
        const val CLIENT = "Client"
    }
}