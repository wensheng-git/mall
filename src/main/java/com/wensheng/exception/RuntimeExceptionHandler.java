package com.wensheng.exception;

import com.wensheng.entity.MallUser;
import com.wensheng.resposeVo.ResponseVo;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.wensheng.enums.ResponseEnum.*;

@RestControllerAdvice
public class RuntimeExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVo<MallUser> notValidMethodArgumentExceptionHandler(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        return new ResponseVo<>(PARAM_ERROR.getStatus(),fieldError.getField()+fieldError.getDefaultMessage());

    }

    @ExceptionHandler(UserLoginException.class)
    public ResponseVo<MallUser> userLoginException(){
        return new ResponseVo<>(NEED_LOGIN.getStatus(),NEED_LOGIN.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public ResponseVo<MallUser> UnKnownException(Exception e){
        System.out.println(e.getMessage());
        e.printStackTrace();
        // 未知的异常都用该处理统一处理,前者打出堆栈信息
        return new ResponseVo<>(ERROR.getStatus(),ERROR.getMsg());
    }


}
