package com.wensheng.entity.formEntity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginForm {
    /*
    * NotNUll:判断不能为空
    * NotEmpty:用于集合的判断
    * NotBlank:用于String的判断..传递String的空格过来会视为非法
    * */
    @NotBlank()
    private String username;
    @NotBlank()
    private String password;
}
