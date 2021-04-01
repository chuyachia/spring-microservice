package com.chuya.userservice.configuration;

import com.chuya.common.filter.JwtTokenFilter;
import com.chuya.common.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserConfiguration {
    @Bean("templateWithAuthAndLB")
    @LoadBalanced
    public RestTemplate getRestTemplate(@Qualifier("templateWithAuth") RestTemplate restTemplate) {
        return restTemplate;
    }

    @Bean
    JwtTokenFilter jwtTokenFilter(@Value("${jwt.secret}") String secret, @Value("${jwt.validity-millis}") Integer validityMillis) {
        return new JwtTokenFilter(new JwtUtils(secret, validityMillis));
    }
}
