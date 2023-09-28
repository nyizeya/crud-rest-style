package com.example.demo.security.utility;

import com.example.demo.model.entity.Instructor;
import com.example.demo.model.entity.Token;
import com.example.demo.model.entity.dto.DataTablesOutput;
import com.example.demo.model.service.InstructorService;
import com.example.demo.model.service.TokenService;
import com.example.demo.security.exception.InvalidTokenException;
import com.example.demo.security.model.AuthenticationRequest;
import com.example.demo.security.model.SecurityUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtilityService jwtService;
    private final InstructorService instructorService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public ResponseEntity<DataTablesOutput<Map<String, Map<String, Object>>>> authenticate(AuthenticationRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        authenticationManager.authenticate(authenticationToken);

        Instructor user = instructorService.loadInstructorByUsername(request.getUsername());
        String accessToken = jwtService.generateToken(new SecurityUser(user));
        Date accessTokenExpiration = jwtService.extractExpiration(accessToken);
        String refreshToken = jwtService.generateRefreshToken(new SecurityUser(user));
        Date refreshTokenExpiration = jwtService.extractExpiration(refreshToken);

        revokeAllUserToken(user);
        saveUserToken(user, accessToken);

        Map<String, Map<String, Object>> map = new HashMap<>();
        Map<String, Object> accessTokenMap = new HashMap<>();
        accessTokenMap.put("token", accessToken);
        accessTokenMap.put("accessTokenExpiration", accessTokenExpiration);

        Map<String, Object> refreshTokenMap = new HashMap<>();
        refreshTokenMap.put("token", refreshToken);
        refreshTokenMap.put("refreshTokenExpiration", refreshTokenExpiration);


        map.put("accessToken", accessTokenMap);
        map.put("refreshToken", refreshTokenMap);

        DataTablesOutput<Map<String, Map<String, Object>>> dataTablesOutput = DataTablesOutput.<Map<String, Map<String, Object>>>builder()
                .data(Lists.newArrayList(map))
                .build();

        return ResponseEntity.ok(dataTablesOutput);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (null == authHeader || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = jwtService.extractTokenFromAuthHeader(authHeader);

        try {
            userEmail = jwtService.extractUsername(refreshToken);


            if (null != userEmail) {
                Instructor instructor = instructorService.loadInstructorByUsername(userEmail);
                UserDetails userDetails = new SecurityUser(instructor);

                if (jwtService.isTokenValid(refreshToken, userDetails)) {
                    String accessToken = jwtService.generateToken(userDetails);
                    revokeAllUserToken(instructor);
                    saveUserToken(instructor, accessToken);

                    Map<String, Object> data = new HashMap<>();
                    data.put("accessToken", accessToken);
                    data.put("refreshToken", refreshToken);

                    new ObjectMapper().writeValue(response.getOutputStream(), data);
                }
            }

        } catch (InvalidTokenException | SignatureException | MalformedJwtException | ExpiredJwtException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write("invalid token");
        }

    }

    private void saveUserToken(Instructor user, String jwtToken) {
        Token token = Token.builder()
                .instructor(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();

        tokenService.saveToken(token);
    }

    private void revokeAllUserToken(Instructor user) {
        List<Token> validTokens = tokenService.findAllValidTokenByUser(user.getId());

        if (validTokens.isEmpty()) return;

        validTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenService.saveAllToken(validTokens);
    }
}
