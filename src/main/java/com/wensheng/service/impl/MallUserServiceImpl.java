package com.wensheng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wensheng.entity.formEntity.UserRegisterForm;
import com.wensheng.resposeVo.ResponseVo;
import com.wensheng.entity.MallUser;
import com.wensheng.entity.formEntity.UserLoginForm;
import com.wensheng.service.MallUserService;
import com.wensheng.mapper.MallUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

import static com.wensheng.enums.ResponseEnum.*;

/**
* @author 86159
* @description 针对表【mall_user】的数据库操作Service实现
* @createDate 2022-10-02 09:57:26
*/
@Service
public class MallUserServiceImpl extends ServiceImpl<MallUserMapper, MallUser>
    implements MallUserService{
    @Autowired
    MallUserMapper mallUserMapper;

    public ResponseVo<MallUser> queryByUsername(UserLoginForm userLoginFrom){
        MallUser user = new MallUser();
        BeanUtils.copyProperties(userLoginFrom,user);

        MallUser mallUser = mallUserMapper.selectByUsername(user.getUsername());
        // md5加密 摘要算法.md5(密码.getBytes(标准编码UTF-8))
        String password= DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
        if (mallUser!=null&&password.equalsIgnoreCase(mallUser.getPassword())){
            mallUser.setPassword("*********");
            return new ResponseVo(mallUser, SUCCESS.getStatus());
        }
        return new ResponseVo(USERNAME_OR_PASSWORD_ERROR.getStatus(), USERNAME_OR_PASSWORD_ERROR.getMsg());
    }

    public ResponseVo<MallUser> addUser(UserRegisterForm userRegisterFrom){
        MallUser user=new MallUser();
        BeanUtils.copyProperties(userRegisterFrom,user);
        // 1:先判断用户是否存在
        MallUser mallUser = mallUserMapper.selectByUsername(user.getUsername());
        if (mallUser!=null) return new ResponseVo<>(USERNAME_EXIST.getStatus(),USERNAME_EXIST.getMsg()) ;
        // 2:成功插入
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        // 用户角色DB默认普通用户,若为特殊用户user.setRole(0);
        mallUserMapper.insert(user);
        return new ResponseVo<>(SUCCESS.getStatus(),SUCCESS.getMsg());
    }
}




