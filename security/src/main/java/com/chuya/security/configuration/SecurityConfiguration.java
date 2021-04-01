package com.chuya.security.configuration;

import com.chuya.common.jwt.JwtUtils;
import com.chuya.common.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {

        UserDetails user = User.withUsername("user")
                .password("{noop}1234")
                .authorities(getAuthoritiesFromRole(Role.USER))
                .build();

        UserDetails admin = User.withUsername("admin")
                .password("{noop}1234")
                .authorities(getAuthoritiesFromRole(Role.ADMIN))
                .build();

        UserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(user);
        userDetailsManager.createUser(admin);

        return userDetailsManager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/authenticate").permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceBean());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtUtils jwtUtils(@Value("${jwt.secret}") String secret, @Value("${jwt.validity-millis}") Integer validityMillis) {
        return new JwtUtils(secret, validityMillis);
    }

    private List<GrantedAuthority> getAuthoritiesFromRole(Role role) {
        return role.authorities.stream()
                .map(a -> new SimpleGrantedAuthority(a))
                .collect(Collectors.toList());
    }
}
