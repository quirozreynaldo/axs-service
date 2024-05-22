package com.keysolbo.axsservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.http.HttpHeaders;

import com.keysolbo.axsservice.model.Credential;
import com.keysolbo.axsservice.model.Token;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class TokenService {

    @Autowired
    private WebClient webClient;
    @Value("${axs.service.token}")
    private String urlToken;
    @Value("${axs.service.username}")
    private String userName;
    @Value("${axs.service.password}")
    private String password;

    public Token getToken2() {
        Token token = new Token();
        Credential credential = new Credential();
        credential.setUserName(userName);
        credential.setPassword(password);
        log.info("token: {}", credential);
        try {
            token = webClient
                    .post()
                    .uri(urlToken)
                    .body(Mono.just(credential), Credential.class)
                    .retrieve()
                    .bodyToMono(Token.class)
                    .block();
            log.info("token: {}", token);
        } catch (WebClientResponseException e) {
            log.error("Error al consumir el servicio. Código de error: {}", e.getStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            // Capturar otros errores inesperados
            log.error("Error inesperado al consumir el servicio {}", e.getMessage());
            throw e;
        }
        return token;
    }

    public Token getToken() {
        Token token = new Token();
        Credential credential = new Credential();
        credential.setUserName(userName);
        credential.setPassword(password);
        log.info("token: {}", credential);
        String authorizationHeaderValue = null;
        try {
            ClientResponse clientResponse = webClient
                    .post()
                    .uri(urlToken)
                    .body(Mono.just(credential), Credential.class)
                    .exchange()
                    .block();

            if (clientResponse != null) {
                HttpHeaders headers = clientResponse.headers().asHttpHeaders();
                authorizationHeaderValue = headers.getFirst(HttpHeaders.AUTHORIZATION);
            }
            // Aquí puedes hacer lo que necesites con el valor de Authorization
            if (authorizationHeaderValue != null) {
                log.info("Valor del encabezado Authorization: {}", authorizationHeaderValue);
                token.setAccessToken(authorizationHeaderValue);
            }

        } catch (WebClientResponseException e) {
            log.error("Error al consumir el servicio. Código de error: {}", e.getStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            // Capturar otros errores inesperados
            log.error("Error inesperado al consumir el servicio {}", e.getMessage());
            throw e;
        }

        return token;
    }
}
