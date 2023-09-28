package com.example.demo.model.entity.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class CheckEmailRequest {
    private Long id;

    @Email(message = "invalid email")
    private String email;
}
