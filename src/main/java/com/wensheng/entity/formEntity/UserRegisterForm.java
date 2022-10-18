package com.wensheng.entity.formEntity;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserRegisterForm {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @Email
    private String email;
}
