package com.wensheng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wensheng.entity.MallProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
* @author 86159
* @description 针对表【mall_product】的数据库操作Mapper
* @createDate 2022-10-02 09:57:26
* @Entity com.wensheng.entity.MallProduct
*/
@Repository
public interface MallProductMapper extends BaseMapper<MallProduct> {
  List<MallProduct> selectByCategoryIdSet(@Param("categoryIdSet") Set<Integer> idSet);

  MallProduct selectById(@Param("id") Integer id);

  List<MallProduct> selectByIdSet(@Param("productIdSet") Set<Integer> idSet);
}




