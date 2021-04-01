package com.chuya.common.filter;

import com.chuya.common.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
    public JwtTokenFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer")) {
            try {
                String token = header.substring(7);
                String username = jwtUtils.getSubject(token);
                String entitlement = jwtUtils.getClaim(token, "entitlement", String.class);
                List<GrantedAuthority> authorities = Arrays.asList(entitlement.split(","))
                        .stream()
                        .map(r -> new SimpleGrantedAuthority(r))
                        .collect(Collectors.toList());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            } catch (RuntimeException exception) {
                sendError(httpServletResponse, exception.getMessage());
            }
        } else {
            sendError(httpServletResponse, "Missing token in request header");
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void sendError(HttpServletResponse httpServletResponse, String logMessage) throws IOException {
        log.error(logMessage);
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
