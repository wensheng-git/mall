package com.wensheng.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wensheng.entity.MallOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 86159
* @description 针对表【mall_order】的数据库操作Mapper
* @createDate 2022-10-02 09:57:26
* @Entity com.wensheng.entity.MallOrder
*/
@Repository
public interface MallOrderMapper extends BaseMapper<MallOrder> {
    int insertSelective(MallOrder mallOrder);

    List<MallOrder> selectByUserId(@Param("userId") Integer userId);

    MallOrder selectByOrderNo(@Param("orderNo") Long orderNo);

    int updateSelective(MallOrder mallOrder);
}




