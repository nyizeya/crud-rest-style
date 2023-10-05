package com.example.demo.security.filter;

import com.example.demo.model.service.TokenService;
import com.example.demo.security.exception.InvalidTokenException;
import com.example.demo.security.utility.JwtUtilityService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNullApi;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class InitialJwtFilter extends OncePerRequestFilter {

    private final JwtUtilityService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String jwtToken;
        String userEmail;

        if (null == authHeader || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);

        try {
            userEmail = jwtService.extractUsername(jwtToken);

            if (null != userEmail && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                boolean isAccessToken = jwtService.extractTokenRole(jwtToken).equals("ACCESS_TOKEN");
                boolean isValid = false;

                if (isAccessToken) {
                    isValid = tokenService.findByToken(jwtToken)
                        .map(t -> !t.isExpired() && !t.isRevoked())
                        .orElse(false);
                }

                if (jwtService.isTokenValid(jwtToken, userDetails) && isValid) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                    ); // Authentication instance with 3 parameters means the instance is marked as authenticated

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        } catch (InvalidTokenException | SignatureException | MalformedJwtException | ExpiredJwtException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
//            response.getWriter().write("invalid token");
            filterChain.doFilter(request, response);
        }

        filterChain.doFilter(request, response);

    }
}
