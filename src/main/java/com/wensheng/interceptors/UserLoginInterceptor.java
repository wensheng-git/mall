package com.wensheng.interceptors;

import com.wensheng.consts.MallConst;
import com.wensheng.entity.MallUser;
import com.wensheng.exception.UserLoginException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        MallUser userInfo = (MallUser)session.getAttribute(MallConst.CURRENT_USER);
        if (userInfo==null) throw new UserLoginException();
        return true; // 返回true表示放行
    }
}
