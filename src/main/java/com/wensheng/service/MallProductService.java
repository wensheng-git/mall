package com.wensheng.service;

import com.github.pagehelper.PageInfo;
import com.wensheng.entity.MallProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wensheng.resposeVo.ResponseVo;

/**
* @author 86159
* @description 针对表【mall_product】的数据库操作Service
* @createDate 2022-10-02 09:57:26
*/
public interface MallProductService extends IService<MallProduct> {
   public ResponseVo<PageInfo> queryList(Integer id, Integer pageNum, Integer  pageSize);
   public ResponseVo<MallProduct> queryById(Integer id);
}
