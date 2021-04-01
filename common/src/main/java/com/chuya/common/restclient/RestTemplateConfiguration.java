package com.chuya.common.restclient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class RestTemplateConfiguration {
    @Bean
    public ClientHttpRequestInterceptor authInterceptor() {
        return (httpRequest, bytes, clientHttpRequestExecution) -> {
            String authorizationHeader =
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                            .getRequest().getHeader(HttpHeaders.AUTHORIZATION);

            httpRequest.getHeaders().set(HttpHeaders.AUTHORIZATION, authorizationHeader);
            return clientHttpRequestExecution.execute(httpRequest, bytes);
        };
    }

    @Bean("templateWithAuth")
    public RestTemplate getRestTemplate(ClientHttpRequestInterceptor authInterceptor) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .additionalInterceptors(authInterceptor)
                .build();

        return restTemplate;
    }
}
