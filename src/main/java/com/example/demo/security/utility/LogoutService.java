package com.example.demo.security.utility;

import com.example.demo.model.entity.Token;
import com.example.demo.model.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final JwtUtilityService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");

        if (null == authHeader || !authHeader.startsWith("Bearer ")) {
            return;
        }

        try {
            String token = authHeader.substring(7);
            String userEmail = jwtService.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(token, userDetails)) {
                 Token currentToken = tokenService.findByToken(token).orElse(null);

                if (null != currentToken) {
                    currentToken.setRevoked(true);
                    currentToken.setExpired(true);
                    tokenService.saveToken(currentToken);
                    SecurityContextHolder.clearContext();
                }
            }

        } catch (SignatureException | MalformedJwtException | ExpiredJwtException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            try {
                response.getWriter().write("invalid token");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
