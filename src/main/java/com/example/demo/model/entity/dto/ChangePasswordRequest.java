package com.example.demo.model.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

    private Long id;

    @NotBlank(message = "current password must not be blank")
    private String currentPassword;

    @NotBlank(message = "new password must not be blank")
    private String newPassword;

    @NotBlank(message = "confirm password must not be blank")
    private String confirmPassword;

}
