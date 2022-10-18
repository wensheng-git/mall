package com.wensheng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wensheng.entity.MallOrderItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
* @author 86159
* @description 针对表【mall_order_item】的数据库操作Mapper
* @createDate 2022-10-02 09:57:26
* @Entity com.wensheng.entity.MallOrderItem
*/
@Repository
public interface MallOrderItemMapper extends BaseMapper<MallOrderItem> {
   int batchInsert(@Param("orderItemList") List<MallOrderItem> orderItemList);

   List<MallOrderItem> selectByOrderNoSet(@Param("orderNoSet") Set orderNoSet);
}




