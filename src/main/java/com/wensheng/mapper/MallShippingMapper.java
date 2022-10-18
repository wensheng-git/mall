package com.wensheng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wensheng.entity.MallShipping;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
* @author 86159
* @description 针对表【mall_shipping】的数据库操作Mapper
* @createDate 2022-10-02 09:57:26
* @Entity com.wensheng.entity.MallShipping
*/
@Repository
public interface MallShippingMapper extends BaseMapper<MallShipping> {
   int  insertByPrimaryKey(MallShipping mallShipping);
   int deleteByIdAndUid(@Param("uid") Integer uid, @Param("id") Integer id);
   int updateByPrimaryKeySelective(MallShipping mallShipping);
   List<MallShipping> selectByUserId(@Param("userId") Integer userId);
   MallShipping selectByIdAndUserId(@Param("id") Integer id, @Param("userId") Integer userId);

   List<MallShipping> selectByIdSet(@Param("idSet") Set<Integer> idSet);
}




