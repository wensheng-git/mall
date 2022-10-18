package com.wensheng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wensheng.entity.MallCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 86159
* @description 针对表【mall_category】的数据库操作Mapper
* @createDate 2022-10-02 09:57:26
* @Entity com.wensheng.entity.MallCategory
*/
@Repository
public interface MallCategoryMapper extends BaseMapper<MallCategory> {
    List<MallCategory> selectAll();
}




