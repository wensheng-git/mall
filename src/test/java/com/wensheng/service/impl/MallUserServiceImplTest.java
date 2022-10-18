package com.wensheng.service.impl;

import com.wensheng.entity.formEntity.UserRegisterForm;
import com.wensheng.resposeVo.ResponseVo;
import com.wensheng.entity.MallUser;
import com.wensheng.entity.formEntity.UserLoginForm;
import com.wensheng.service.MallUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class MallUserServiceImplTest {
    @Autowired
    MallUserService mallUserService;

    @Test
    void queryByUsername() {
//        log.info(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
        UserLoginForm userLoginFrom = new UserLoginForm();
        userLoginFrom.setUsername("文生");
        userLoginFrom.setPassword("123456");
        ResponseVo<MallUser> mallUserResponseJson = mallUserService.queryByUsername(userLoginFrom);
        log.info("{}",mallUserResponseJson);
    }

    @Test
    void addUser(){
        UserRegisterForm userRegisterFrom=new UserRegisterForm();
        userRegisterFrom.setUsername("Jason");
        userRegisterFrom.setPassword("123456");
        ResponseVo<MallUser> mallUserResponseJson = mallUserService.addUser(userRegisterFrom);
        log.info("{}",mallUserResponseJson);
    }
}