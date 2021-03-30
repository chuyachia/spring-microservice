package com.chuya.common.filter;

import com.chuya.common.jwt.JwtUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JwtTokenFilter extends OncePerRequestFilter {
    public JwtTokenFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String header = httpServletRequest.getHeader("Authorization");

        if (header != null) {
            String token = header.substring(7);
            String username = jwtUtils.getSubject(token);
            String entitlement = jwtUtils.getClaim(token, "entitlement", String.class);
            List<GrantedAuthority> authorities = Arrays.asList(entitlement.split(","))
                    .stream()
                    .map(r-> new SimpleGrantedAuthority(r))
                    .collect(Collectors.toList());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}