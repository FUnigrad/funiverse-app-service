package com.unigrad.funiverseappservice.security.filter;

import com.unigrad.funiverseappservice.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        final String domain = request.getHeader("Origin");

        // todo need to authorize services
        if ("3.1.47.236:30001".equals(domain)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null) {
            jwt = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiV09SS1NQQUNFX0FETUlOIiwidXNlcm5hbWUiOiJ0aGFuaC5idWkuYmFvQGdtYWlsLmNvbSIsIndvcmtzcGFjZUlkIjoyLCJzdWIiOiJ0aGFuaC5idWkuYmFvQGdtYWlsLmNvbSIsImlhdCI6MTY3OTg4NzgzNSwiZXhwIjoxNjgwMDYwNjM1fQ.0wVnQJocMe3qq--AYFAJo1gp-ojSm8FZgKFSpWySmM0";
        } else {
            jwt = authHeader.substring(7);
        }
        userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}