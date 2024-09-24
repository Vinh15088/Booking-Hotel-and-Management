package com.Seo.SeoHotel.security;

import com.Seo.SeoHotel.service.CustomUserDetailsService;
import com.Seo.SeoHotel.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        if(authHeader == null || authHeader.isBlank()) {
            // skip and continue the filter chain
            filterChain.doFilter(request, response);
            return;
        }

        // remove the "Bearer " prefix to get jwt
        jwtToken = authHeader.substring(7);

        // extract username (email) from the token
        userEmail = jwtUtils.extractUsername(jwtToken);

        // if email not null and securitycontext does not have authentication
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // load userdetails from db
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

            // valid token
            if(jwtUtils.isValidToken(jwtToken, userDetails)) {
                // create secutiryContext to hold the authentication infor
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                // create an authentication token with user details and authorities (roles)
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // set request details for the authentication object
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // set the authentication token
                securityContext.setAuthentication(token);

                SecurityContextHolder.setContext(securityContext);
            }
        }

        // continue filter chain after process jwt
        filterChain.doFilter(request, response);
    }
}
