package com.chuya.security.controller;

import com.chuya.common.jwt.JWTUtils;
import com.chuya.security.model.GetTokenRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
public class SecurityController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    @PostMapping("/authenticate")
    public String getToken(@RequestBody GetTokenRequest getTokenRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(getTokenRequest.getUsername(), getTokenRequest.getPassword()));
        } catch (AuthenticationException ex) {
            throw new RuntimeException();
        }

        User authenticatedUser = (User) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("entitlement", authenticatedUser.getAuthorities().stream().map(g -> g.getAuthority()));

        String jwtToken = jwtUtils.generateToken(claims, authenticatedUser.getUsername());

        return jwtToken;
    }
}
