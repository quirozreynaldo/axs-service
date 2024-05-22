package com.keysolbo.axsservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Configuration
public class WebClientConfiguration {
    @Value("${axs.service.urlbase}")
    private String urlBase;

    @Value("${surveymonkey.service.urlbase}")
    private String surveyMonkeyApiUrlBase;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
        .baseUrl(urlBase)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
    }
    @Bean
    public WebClient webClientApi() {
        return WebClient.builder()
        .baseUrl(surveyMonkeyApiUrlBase)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
    }
}
