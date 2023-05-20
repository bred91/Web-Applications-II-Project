package it.polito.server.security

import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KeycloakConfig {
    @Value("\${keycloak.serverUrl}")
    private lateinit var serverUrl: String

    @Value("\${keycloak.realm}")
    private lateinit var realm: String

    @Value("\${keycloak.clientId}")
    private lateinit var clientId: String

    @Value("\${keycloak.username}")
    private lateinit var username: String

    @Value("\${keycloak.password}")
    private lateinit var password: String

    @Bean
    fun keycloakAdminClient(): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm(realm)
            .clientId(clientId)
            .username(username)
            .password(password)
            .grantType(OAuth2Constants.PASSWORD).build()

    }
}
