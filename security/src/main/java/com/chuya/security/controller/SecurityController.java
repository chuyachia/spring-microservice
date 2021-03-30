package com.chuya.security.controller;

import com.chuya.common.jwt.JwtUtils;
import com.chuya.security.model.GetTokenRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class SecurityController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/authenticate")
    public String getToken(@RequestBody GetTokenRequest getTokenRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(getTokenRequest.getUsername(), getTokenRequest.getPassword()));

        User authenticatedUser = (User) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        String authoritiesString = authenticatedUser.getAuthorities()
                .stream()
                .map(g -> g.getAuthority())
                .collect(Collectors.joining(","));
        claims.put("entitlement", authoritiesString);

        String jwtToken = jwtUtils.generateToken(claims, authenticatedUser.getUsername());

        return jwtToken;
    }
}
