package com.temm.skillify.security;



import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authHeader); // Log the header
    
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No JWT token found in the header.");
            filterChain.doFilter(request, response);
            return;
        }
    
        final String jwt = authHeader.substring(7);
        System.out.println("Extracted JWT token: " + jwt); // Log the extracted token
    
        final String userEmail = jwtService.extractUsername(jwt);
        System.out.println("Extracted user email: " + userEmail); // Log the extracted email
    
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            System.out.println("Loaded user details: " + userDetails.getUsername()); // Log the loaded user
    
            if (jwtService.isTokenValid(jwt, userDetails)) {
                System.out.println("JWT token is valid."); // Log token validation
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication object set in SecurityContext."); // Log successful authentication
            } else {
                System.out.println("JWT token is invalid."); // Log invalid token
            }
        }
    
        filterChain.doFilter(request, response);
    }
}