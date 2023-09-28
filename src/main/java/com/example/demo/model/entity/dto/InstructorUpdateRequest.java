package com.example.demo.model.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorUpdateRequest {

    private Long id;

    private String name;

    private String email;

    private String phone;

}
