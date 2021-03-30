package com.chuya.departmentservice.configuration;

import com.chuya.common.filter.JwtTokenFilter;
import com.chuya.common.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DepartmentConfiguration {
    @Bean
    JwtTokenFilter jwtTokenFilter(@Value("${jwt.secret}") String secret, @Value("${jwt.validity-millis}") Integer validityMillis) {
        return new JwtTokenFilter(new JwtUtils(secret, validityMillis));
    }
}
