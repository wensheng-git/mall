package com.wensheng.controller;

import com.wensheng.entity.MallUser;
import com.wensheng.entity.formEntity.UserLoginForm;
import com.wensheng.entity.formEntity.UserRegisterForm;
import com.wensheng.resposeVo.ResponseVo;
import com.wensheng.service.MallUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.wensheng.consts.MallConst.CURRENT_USER;
import static com.wensheng.enums.ResponseEnum.SUCCESS;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    MallUserService mallUserService;

    @PostMapping("/login")
    public ResponseVo<MallUser> Login(
            @Valid @RequestBody UserLoginForm userLoginForm,
            HttpSession session)  {
        // 1:TODO:参数校验异常处理统一
        // ps
       /*if (bindingResult.hasErrors()){
           log.info("{}",bindingResult.getFieldError().getField()+bindingResult.getFieldError().getDefaultMessage());
           return new ResponseJson<>(PARAM_ERROR.getStatus(),
                   PARAM_ERROR.getMsg()
                           +bindingResult.get
                           +bindingResult.getFieldError().getDefaultMessage());
       }*/
        // 2:session存入:判断登入状态的请求
        // TODO:session是存内存==>优化;存redis+token
        ResponseVo<MallUser> mallUserResponseJson = mallUserService.queryByUsername(userLoginForm);
        session.setAttribute(CURRENT_USER,mallUserResponseJson.getData());

        return  mallUserResponseJson;
    }


    @PostMapping("/register")
    public ResponseVo<MallUser> register(@Valid @RequestBody UserRegisterForm userRegisterFrom){
       // 1:TODO:统一参数异常处理
         return mallUserService.addUser(userRegisterFrom);
    }
    @GetMapping()
    public ResponseVo<MallUser> getUserInfo(HttpSession session){
        MallUser userInfo = (MallUser)session.getAttribute(CURRENT_USER);
      // TODO:很多请求都需要去判断登入的状态========>统一拦截该类型的请求
      // if (userInfo==null) return new ResponseJson<>(NEED_LOGIN.getStatus(),NEED_LOGIN.getMsg());
        return new ResponseVo<>(SUCCESS.getStatus(),userInfo);
    }

    @PostMapping("/logout")
    public ResponseVo<MallUser> logOut(HttpSession session){
        session.removeAttribute(CURRENT_USER);
        // 测试服务端错误异常处理
        // int i=1/0;
        return new ResponseVo<>(SUCCESS.getStatus(),SUCCESS.getMsg());
    }

}
