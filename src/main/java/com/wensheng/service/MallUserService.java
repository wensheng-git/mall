package com.wensheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wensheng.entity.MallUser;
import com.wensheng.entity.formEntity.UserLoginForm;
import com.wensheng.entity.formEntity.UserRegisterForm;
import com.wensheng.resposeVo.ResponseVo;

/**
* @author 86159
* @description 针对表【mall_user】的数据库操作Service
* @createDate 2022-10-02 09:57:26
*/
public interface MallUserService extends IService<MallUser> {
    public ResponseVo<MallUser> queryByUsername(UserLoginForm userLoginFrom);
    public ResponseVo<MallUser> addUser(UserRegisterForm userRegisterFrom);
}
