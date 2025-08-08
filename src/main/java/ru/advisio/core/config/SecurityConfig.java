package ru.advisio.core.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import ru.advisio.core.utils.converters.KeycloakJwtAuthenticationConverter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(WHITELIST)
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(keycloakJwtAuthenticationConverter())
                        )
                );
        return http.build();
    }

    @Bean
    public KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter() {
        return new KeycloakJwtAuthenticationConverter();
    }

    private static final String[] WHITELIST = {
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/*/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/*/swagger-ui/**",
            "/*/swagger-ui.html",
            "/webjars/swagger-ui/**",
            "/swagger-resources/**",
            "/swagger-resources",
            "/*/swagger-resources/**",
            "/*/swagger-resources",
            "/*/home.html",
            "/*/home",
            "/*/home/**",
            "/", "/home", "/*/get-token", "/get-token", "/static/**",
            // -- Actuator (если используется)
            "/actuator",
            "/actuator/**"
    };
}