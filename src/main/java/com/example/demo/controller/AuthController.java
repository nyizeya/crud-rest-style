package com.example.demo.controller;

import com.example.demo.model.entity.dto.CheckEmailRequest;
import com.example.demo.model.entity.dto.CheckPhoneRequest;
import com.example.demo.model.entity.dto.DataTablesOutput;
import com.example.demo.model.service.InstructorService;
import com.example.demo.security.model.AuthenticationRequest;
import com.example.demo.security.utility.AuthenticationService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final InstructorService instructorService;

    @PostMapping("authenticate")
    public ResponseEntity<DataTablesOutput<Map<String, Map<String, Object>>>> authenticate(
            @Valid @RequestBody AuthenticationRequest request,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            DataTablesOutput<Map<String, Map<String, Object>>> dataTablesOutput =
                    DataTablesOutput.<Map<String, Map<String, Object>>>builder()
                            .error("invalid authentication request")
                            .build();

            return new ResponseEntity<>(dataTablesOutput, BAD_REQUEST);
        }

        return authenticationService.authenticate(request);

    }

    @PostMapping("refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @PostMapping("check-email")
    public ResponseEntity<DataTablesOutput<Boolean>> checkEmail(
            @Valid @RequestBody CheckEmailRequest request,
            BindingResult bindingResult
    ) {
        DataTablesOutput<Boolean> dataTablesOutput = new DataTablesOutput<>();

        if (bindingResult.hasErrors()) {
            dataTablesOutput.setError("invalid check email request");
            return new ResponseEntity<>(dataTablesOutput, BAD_REQUEST);
        }

        boolean result = instructorService.checkIfEmailAlreadyExists(request.getEmail());

        dataTablesOutput.setData(Lists.newArrayList(result));

        return ResponseEntity.ok(dataTablesOutput);
    }

    @PostMapping("check-phone")
    public ResponseEntity<DataTablesOutput<Boolean>> checkPhone(
            @Valid @RequestBody CheckPhoneRequest request
    ) {
        DataTablesOutput<Boolean> dataTablesOutput = new DataTablesOutput<>();

        boolean result = instructorService.checkIfPhoneAlreadyExists(request.getPhone());

        dataTablesOutput.setData(Lists.newArrayList(result));

        return ResponseEntity.ok(dataTablesOutput);
    }

}
