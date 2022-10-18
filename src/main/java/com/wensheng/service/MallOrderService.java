package com.wensheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.wensheng.entity.MallOrder;
import com.wensheng.resposeVo.OrderVo;
import com.wensheng.resposeVo.ResponseVo;

/**
* @author 86159
* @description 针对表【mall_order】的数据库操作Service
* @createDate 2022-10-02 09:57:26
*/
public interface MallOrderService extends IService<MallOrder> {
    ResponseVo<OrderVo> create(Integer uid, Integer shippingId);

    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);

    ResponseVo<OrderVo> detail(Integer uid, Long orderNo);

    ResponseVo cancel(Integer uid, Long orderNo);

    void paid(Long orderNo);
}
