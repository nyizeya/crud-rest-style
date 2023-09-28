package com.example.demo.model.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckPhoneRequest {
    private Long id;
    private String phone;
}
