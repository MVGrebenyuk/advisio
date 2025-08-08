package ru.advisio.core.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KeycloakService {

    private static final String KEYCLOAK_URL = "http://localhost:8080/realms/advisio_realm/protocol/openid-connect/token";
    private static final String CLIENT_ID = "advisio-spring-app";
    private static final String CLIENT_SECRET = "your-client-secret";

    public static String getToken(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);
        body.add("username", username);
        body.add("password", password);
        body.add("grant_type", "password");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                KEYCLOAK_URL,
                request,
                TokenResponse.class
        );

        return response.getBody().getAccess_token();
    }

    private static class TokenResponse {
        private String access_token;

        // Геттеры и сеттеры
        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }
    }
}
