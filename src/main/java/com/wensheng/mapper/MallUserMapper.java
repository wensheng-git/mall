package com.wensheng.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.wensheng.entity.MallUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author 86159
* @description 针对表【mall_user】的数据库操作Mapper
* @createDate 2022-10-02 09:57:26
* @Entity com.wensheng.entity.MallUser
*/
@Repository
public interface MallUserMapper extends BaseMapper<MallUser> {
    MallUser selectByUsername(@Param("username") String username);
}




