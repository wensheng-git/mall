package com.wensheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.wensheng.entity.MallShipping;
import com.wensheng.entity.formEntity.ShippingForm;
import com.wensheng.resposeVo.ResponseVo;

import java.util.Map;

/**
* @author 86159
* @description 针对表【mall_shipping】的数据库操作Service
* @createDate 2022-10-02 09:57:26
*/
public interface MallShippingService extends IService<MallShipping> {
    public ResponseVo<Map<String,Integer>> addShipping(Integer uid,ShippingForm shippingForm);

    ResponseVo delete(Integer uid, Integer shippingId);

    ResponseVo update(Integer uid, Integer shippingId, ShippingForm form);

    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);

}
