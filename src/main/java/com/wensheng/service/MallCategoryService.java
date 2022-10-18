package com.wensheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wensheng.entity.MallCategory;
import com.wensheng.resposeVo.CategoryVo;
import com.wensheng.resposeVo.ResponseVo;

import java.util.List;
import java.util.Set;

/**
* @author 86159
* @description 针对表【mall_category】的数据库操作Service
* @createDate 2022-10-02 09:57:26
*/
public interface MallCategoryService extends IService<MallCategory> {
    public ResponseVo<List<CategoryVo>> selectAll();
    public void queryIdSet(Integer id,Set<Integer> result);
}
