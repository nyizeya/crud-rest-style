package com.example.demo.model.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CheckPasswordRequest {

    private Long id;

    @NotBlank(message = "password must not be blank")
    private String password;
}
